package ltd.rymc.form.tpa.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import ltd.rymc.form.tpa.TpaForm;
import ltd.rymc.form.tpa.forms.TpaSendForm;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("tpaui|tform")
@Description("TpaForm Form菜单")
public class TpaFormCommand extends BaseCommand {

    @Default
    public void main(CommandSender sender){
        if (!(sender instanceof Player)) return;

        Player player = ((Player) sender);
        new TpaSendForm(player,null).send();
    }

    @Subcommand("reload")
    @CommandPermission("tpaform.reload")
    public void reload(CommandSender sender){
        TpaForm.reload();
        sender.sendMessage((sender instanceof Player ? TpaForm.lang().prefix() : "") + TpaForm.lang().reload());
    }
}
