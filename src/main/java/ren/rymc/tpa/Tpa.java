package ren.rymc.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import ren.rymc.tpa.metrics.Metrics;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class Tpa extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    private static Tpa instance;
    private final List<String> list = Collections.singletonList("reload");
    private final HashMap<String,String> langList = new HashMap<>();
    private final HashMap<String,String> configList = new HashMap<>();
    private FileConfiguration lang;
    private FileConfiguration defaultLang;
    private FileConfiguration config;
    private FileConfiguration defaultConfig;
    private boolean completionToggle = true;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadConfig();
        loadCommand();
        Bukkit.getPluginManager().registerEvents(this, this);
        if (config.getBoolean("metrics",defaultConfig.getBoolean("metrics"))){
            Metrics metrics = new Metrics(this, 1234);
            metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        }
    }

    private void loadConfig(){
        langList.clear();
        configList.clear();
        config = getConfig();
        File file = new File(getInstance().getDataFolder(),"lang.yml");
        if (!file.exists()) getInstance().saveResource("lang.yml", false);
        lang = YamlConfiguration.loadConfiguration(file);
        defaultConfig = getResourceConfig("config.yml");
        defaultLang = getResourceConfig("lang.yml");
        if (defaultLang == null || defaultConfig == null) {
            getInstance().getLogger().severe("插件加载配置文件时发送错误,你是否加载了完整的插件?");
            getInstance().getLogger().severe("The plugin sent an error when loading the configuration file. Did you use the complete plugin?");
            Bukkit.getPluginManager().disablePlugin(getInstance());
            return;
        }
        loadLang("title");
        loadLang("select-players");
        loadLang("player-list");
        loadLang("input-tips");
        loadLang("input-tips1");
        loadLang("tpa-mode");
        loadLang("tpa");
        loadLang("tpa-here");
        loadLang("accept");
        loadLang("deny");
        loadLang("reload");
        loadLang("prefix");
        loadConfig("tpa-command");
        loadConfig("tpa-here-command");
        loadConfig("accept-command");
        loadConfig("deny-command");
        loadConfig("accept-form-icon");
        loadConfig("deny-form-icon");

        completionToggle = config.getBoolean("auto-completion-player-name",defaultConfig.getBoolean("auto-completion-player-name"));
    }

    private void loadCommand(){
        PluginCommand tpaui = getCommand("tpaui");
        if (tpaui == null){
            getInstance().getLogger().severe("插件加载命令时发送错误,你是否加载了完整的插件?");
            getInstance().getLogger().severe("The plugin sent an error when loading commands. Did you use the complete plugin?");
            Bukkit.getPluginManager().disablePlugin(getInstance());
            return;
        }
        tpaui.setExecutor(this);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && (sender instanceof Player)){
            Player player = ((Player) sender);
            if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) return true;
            sendTpaMenu(player);
        }else if(args.length != 0 && args[0].equals("reload")){
            if (!sender.hasPermission("tpaui.admin")) return true;
            saveDefaultConfig();
            loadConfig();
            sender.sendMessage((sender instanceof Player ? langList.get("prefix") : "") + langList.get("reload"));
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return list;
        return null;
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
        if (!(messages[0].equals("/" + configList.get("tpa-command")) || messages[0].equals("/" + configList.get("tpa-here-command")))) return;
        if (completionToggle) {
            for (String playerName : arraysFilter(getOnlinePlayerNameList(), "")) {
                if (!playerName.toLowerCase(Locale.ROOT).contains(messages[1])) continue;
                if (event.getPlayer().getName().toLowerCase(Locale.ROOT).equals(messages[1])) continue;
                messages[1] = playerName;
            }
            StringBuilder newMessage = new StringBuilder();
            for(String command : messages) newMessage.append(command).append(" ");
            event.setMessage(String.valueOf(newMessage).trim());
        }
        sendTpaApplicationMenu(messages[1],event.getPlayer().getName(),messages[0]);
    }

    private void sendTpaMenu(Player player) {
        UUID uuid = player.getUniqueId();
        if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) return;
        String[] playerNameList = arraysFilter(getOnlinePlayerNameList(),player.getName());
        playerNameList[0] = langList.get("select-players");
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                CustomForm.builder()
                        .title(langList.get("title"))
                        .dropdown(langList.get("player-list"),playerNameList)
                        .input(langList.get("input-tips"), langList.get("input-tips1"))
                        .toggle(langList.get("tpa-mode"))
                        .responseHandler((f, r) -> {
                            CustomFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                String command = response.getToggle(2) ? configList.get("tpa-here-command") + " " : configList.get("tpa-command") + " ";
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
        if (tpaMode.equals("/" + configList.get("tpa-command"))) tpaMode = langList.get("tpa");
        if (tpaMode.equals("/" + configList.get("tpa-here-command"))) tpaMode = langList.get("tpa-here");
        FloodgateApi.getInstance().getPlayer(uuid).sendForm(
                SimpleForm.builder()
                        .title(langList.get("title"))
                        .content(String.format(tpaMode,formPlayer))
                        .button(langList.get("accept"),FormImage.Type.PATH,configList.get("accept-form-icon"))
                        .button(langList.get("deny"),FormImage.Type.PATH,configList.get("deny-form-icon"))
                        .responseHandler((f, r) -> {
                            SimpleFormResponse response = f.parseResponse(r);
                            if (response.isCorrect()) {
                                String command = response.getClickedButtonId() == 0 ? configList.get("accept-command") : configList.get("deny-command");
                                Bukkit.dispatchCommand(player,command);
                            }
                        })
        );
    }

    private String[] getOnlinePlayerNameList() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String[] playerNames = new String[onlinePlayers.size() + 1];
        playerNames[0] = "";
        int i = 1;
        for (Player player : onlinePlayers) {
            playerNames[i++] = player.getName();
        }
        return playerNames;
    }

    private String[] arraysFilter(String[] arr,String fil) {
        return Arrays.stream(arr).filter(s -> !fil.equals(s)).toArray(String[]::new);
    }

    public static Tpa getInstance() {
        return instance;
    }

    private FileConfiguration getResourceConfig(String yamlFileName){
        InputStream resource = getInstance().getResource(yamlFileName);
        if (resource == null) return null;
        return YamlConfiguration.loadConfiguration(new InputStreamReader(resource, StandardCharsets.UTF_8));

    }

    private void loadLang(String path){
        langList.put(path,lang.getString(path,defaultLang.getString(path)));
    }

    private void loadConfig(String path){
        configList.put(path,config.getString(path,defaultConfig.getString(path)));
    }

}
