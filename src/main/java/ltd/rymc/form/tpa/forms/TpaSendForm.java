package ltd.rymc.form.tpa.forms;

import ltd.rymc.form.tpa.TpaForm;
import ltd.rymc.form.tpa.form.RCustomForm;
import ltd.rymc.form.tpa.form.RForm;
import ltd.rymc.form.tpa.utils.ArraysUtils;
import ltd.rymc.form.tpa.utils.InputUtils;
import ltd.rymc.form.tpa.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;

public class TpaSendForm extends RCustomForm {

    private final String[] playerNameList;
    public TpaSendForm(Player player, RForm previousForm) {
        super(player, previousForm);
        playerNameList = getPlayerNameList();
        title(TpaForm.lang().title());
        dropdown(TpaForm.lang().playerList(), playerNameList);
        input(TpaForm.lang().inputTips(), TpaForm.lang().inputTips1());
        toggle(TpaForm.lang().tpaMode());
    }

    private String[] getPlayerNameList(){
        String[] players = PlayerUtils.getOnlinePlayerNameList();
        players = ArraysUtils.rotate(players, 1);
        players = ArraysUtils.arraysFilter(players, bukkitPlayer.getName());
        players[0] = TpaForm.lang().selectPlayers();
        return players;
    }

    @Override
    public void onValidResult(CustomForm form, CustomFormResponse response) {

        String command = (response.asToggle(2) ? TpaForm.config().tpaHereCommands().get(0) : TpaForm.config().tpaCommands().get(0)) + " ";
        String input = response.asInput(1);

        String playerName;
        if (InputUtils.checkInput(input) && !input.trim().contains(" ")) playerName = input;
        else if (response.asDropdown(0) != 0) playerName = playerNameList[response.asDropdown(0)];
        else return;

        Bukkit.dispatchCommand(bukkitPlayer, command + PlayerUtils.completionPlayer(playerName));
    }
}
