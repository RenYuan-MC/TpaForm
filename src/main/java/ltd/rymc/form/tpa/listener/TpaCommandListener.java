package ltd.rymc.form.tpa.listener;

import ltd.rymc.form.tpa.TpaForm;
import ltd.rymc.form.tpa.forms.TpaReceiveForm;
import ltd.rymc.form.tpa.tpa.TpaMode;
import ltd.rymc.form.tpa.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Locale;

public class TpaCommandListener implements Listener {



    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        String message = event.getMessage().trim().toLowerCase(Locale.ROOT).substring(1);

        // check tpa
        List<String> commands = TpaForm.config().tpaCommands();
        TpaPlayer tpaPlayer = processCommand(message, commands);
        TpaMode mode = TpaMode.TPA;

        // check tpa here
        if (TpaForm.config().enableTpaHere() && (tpaPlayer == null || tpaPlayer.checkPlayerName())) {
            commands = TpaForm.config().tpaHereCommands();
            tpaPlayer = processCommand(message, commands);
            mode = TpaMode.TPAHERE;
        }

        // check player name
        if (tpaPlayer == null || tpaPlayer.checkPlayerName()) return;
        if (event.getPlayer().getName().equalsIgnoreCase(tpaPlayer.playerName)) return;
        String playerName = tpaPlayer.playerName;

        // completion
        if (TpaForm.config().completion()) {
            playerName = PlayerUtils.completionPlayer(playerName);
            event.setMessage("/" + tpaPlayer.command + " " + playerName);
        }

        // get player
        Player player = PlayerUtils.getPlayerExtract(playerName);
        if (player == null) return;

        // send form
        new TpaReceiveForm(player,event.getPlayer(),null,mode).send();
    }



    private TpaPlayer processCommand(String message, List<String> commands){
        for (String command : commands){
            if (!message.startsWith(command.toLowerCase(Locale.ROOT))) continue;
            return new TpaPlayer(message.substring(command.length()).trim(), command);
        }
        return null;
    }

    private static class TpaPlayer {

        private final String playerName;
        private final String command;

        private TpaPlayer(String playerName, String command){
            this.playerName = playerName;
            this.command = command;
        }

        private boolean checkPlayerName(){
            return playerName.contains(" ") || playerName.isEmpty();
        }
    }
}