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

import java.util.Locale;

public class TpaCommandListener implements Listener {



    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        String message = event.getMessage().trim().toLowerCase(Locale.ROOT).substring(1);

        // check tpa
        String command = TpaForm.config().tpaCommand();
        String playerName = processCommand(message, command);
        TpaMode mode = TpaMode.TPA;

        // check tpa here
        if (TpaForm.config().enableTpaHere() && (playerName == null || playerName.contains(" "))) {
            command = TpaForm.config().tpaHereCommand();
            playerName = processCommand(message, command);
            mode = TpaMode.TPAHERE;
        }

        // check player name
        if (playerName == null || playerName.contains(" ") || playerName.isEmpty()) return;
        if (event.getPlayer().getName().equalsIgnoreCase(playerName)) return;

        // completion
        if (TpaForm.config().completion()) {
            playerName = PlayerUtils.completionPlayer(playerName);
            event.setMessage("/" + command + " " + playerName);
        }

        // get player
        Player player = PlayerUtils.getPlayerExtract(playerName);
        if (player == null) return;

        // send form
        new TpaReceiveForm(player,event.getPlayer(),null,mode).send();
    }



    private String processCommand(String message,String command){
        if (!message.startsWith(command.toLowerCase(Locale.ROOT))) return null;
        return message.substring(command.length()).trim();
    }
}
