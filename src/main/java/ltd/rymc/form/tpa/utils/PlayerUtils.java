package ltd.rymc.form.tpa.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class PlayerUtils {

    public static String[] translateToNameList(List<Player> players) {
        String[] playerNames = new String[players.size()];
        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            playerNames[i] = players.get(i).getName();
        }
        return playerNames;
    }

    public static Player getPlayerExtract(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) return player;
        }
        return null;
    }

    public static String[] getOnlinePlayerNameList() {
        return translateToNameList(new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public static String completionPlayer(String playerName){
        if (PlayerUtils.getPlayerExtract(playerName) != null){
            return playerName;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase(Locale.ROOT).startsWith(playerName)) return playerName;
        }

        return playerName;
    }
}
