package ren.rymc.tpa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public final class Tpa extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler( priority = EventPriority.LOWEST )
    public void TpaMainMenuTask(PlayerCommandPreprocessEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        String message = event.getMessage().trim().toLowerCase(Locale.ROOT);
        String[] messages = message.split(" ");
        if (!messages[0].equals("/tpaui")) return;
        if (Bukkit.getOnlinePlayers().size() <= 1) return;
        event.setCancelled(true);
        sendTpaMenu(event.getPlayer());
    }

    @EventHandler( priority = EventPriority.LOWEST )
    public void TpaCompletionAndApplication(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().trim().toLowerCase(Locale.ROOT);
        String[] messages = message.split(" ");
        if (messages.length <= 1) return;
        if (!(messages[0].equals("/tpa") || messages[0].equals("/tpahere"))) return;
        for (String playerName : arraysFilter(getOnlinePlayerNameList(),"")) {
            if (!playerName.toLowerCase(Locale.ROOT).contains(messages[1])) continue;
            if (event.getPlayer().getName().toLowerCase(Locale.ROOT).equals(messages[1])) continue;
            messages[1] = playerName;
        }
        sendTpaApplicationMenu(messages[1],event.getPlayer().getName(),messages[0]);
        StringBuilder newMessage = new StringBuilder();
        for(String command : messages) newMessage.append(command).append(" ");
        event.setMessage(String.valueOf(newMessage).trim());
    }

    private void sendTpaMenu(Player player) {
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        String[] playerNameList = arraysFilter(getOnlinePlayerNameList(),player.getName());
        playerNameList[0] = "请选择玩家";
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                CustomForm.builder()
                        .title("传送菜单")
                        .dropdown("玩家列表",playerNameList)
                        .input("如果上方找起来过于麻烦可以使用这个输入框", "无需输入完整玩家名")
                        .toggle("模式(开为传送到你这,关为传送到你选择的玩家)")
                        .responseHandler((f, r) -> {
                            CustomFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                String command = response.getToggle(2) ? "tpahere " : "tpa ";
                                String input = response.getInput(1);
                                if (input != null && !input.trim().equals("") && !input.trim().contains(" ")){
                                    Bukkit.dispatchCommand(player,command + input);
                                }else if(response.getDropdown(0) != 0){
                                    Bukkit.dispatchCommand(player,command + playerNameList[response.getDropdown(0)]);
                                }
                            }
                        })
        );
    }

    private void sendTpaApplicationMenu(String applicationPlayer, String formPlayer, String tpaMode) {
        Player player = Bukkit.getPlayerExact(applicationPlayer);
        if (player == null) return;
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        tpaMode = tpaMode.replace("/tpa","%s 想要传送到你这");
        tpaMode = tpaMode.replace("/tpahere", "%s 想要让你传送到他那");
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                SimpleForm.builder()
                        .title("传送菜单")
                        .content(String.format(tpaMode,formPlayer))
                        .button("同意传送",FormImage.Type.PATH,"textures/ui/realms_green_check.png")
                        .button("拒绝传送",FormImage.Type.PATH,"textures/ui/realms_red_x.png")
                        .responseHandler((f, r) -> {
                            SimpleFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                String command = response.getClickedButtonId() == 0 ? "tpaccept" : "tpadeny";
                                Bukkit.dispatchCommand(player,command);
                            }
                        })
        );
    }

    private static String[] getOnlinePlayerNameList() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String[] playerNames = new String[onlinePlayers.size() + 1];
        playerNames[0] = "";
        int i = 1;
        for (Player player : onlinePlayers) {
            playerNames[i++] = player.getName();
        }
        return playerNames;
    }

    public String[] arraysFilter(String[] arr,String fil) {
        return Arrays.stream(arr).filter(s -> !fil.equals(s)).toArray(String[]::new);
    }

}
