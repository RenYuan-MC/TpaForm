package ltd.rymc.form.tpa.forms;

import ltd.rymc.form.tpa.TpaForm;
import ltd.rymc.form.tpa.event.TpaFormReceiveEvent;
import ltd.rymc.form.tpa.form.RForm;
import ltd.rymc.form.tpa.form.RSimpleForm;
import ltd.rymc.form.tpa.tpa.TpaMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;

public class TpaReceiveForm extends RSimpleForm {

    private final TpaMode mode;
    private final Player fromPlayer;

    public TpaReceiveForm(Player player, Player fromPlayer, RForm previousForm, TpaMode mode) {
        super(player, previousForm);
        this.mode = mode;
        this.fromPlayer = fromPlayer;

        title(TpaForm.lang().accept());
        content(String.format(TpaMode.getDescription(mode),fromPlayer.getName()));
        buttons(
                ButtonComponent.of(TpaForm.lang().accept(), FormImage.Type.PATH, TpaForm.config().acceptIcon()),
                ButtonComponent.of(TpaForm.lang().deny(), FormImage.Type.PATH, TpaForm.config().denyIcon())
        );

    }

    @Override
    @SuppressWarnings("ConstantValue")
    public void onValidResult(SimpleForm form, SimpleFormResponse response) {
        int id = response.clickedButtonId();
        String command = id == 0 ? TpaMode.getAcceptCommand(mode) : TpaMode.getDenyCommand(mode);
        Bukkit.dispatchCommand(bukkitPlayer,String.format(command,fromPlayer.getName()));
    }

    @Override
    public void send(){
        if (player == null) return;
        TpaFormReceiveEvent event = new TpaFormReceiveEvent(bukkitPlayer, fromPlayer, mode);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        player.sendForm(builder);
    }
}
