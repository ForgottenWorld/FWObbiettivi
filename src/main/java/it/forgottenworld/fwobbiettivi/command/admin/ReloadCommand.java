package it.forgottenworld.fwobbiettivi.command.admin;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.RELOAD_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.RELOAD_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_RELOAD;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return true;
    }

    @Override
    public int getArgsRequired() {
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        FWObbiettivi.info("Saving infos...");
        FWObbiettivi.saveData();
        FWObbiettivi.info("Data saved");

        FWObbiettivi.info("Loading infos...");
        FWObbiettivi.loadData();
        FWObbiettivi.info("Data loades");

        FWObbiettivi.getInstance().reloadConfig();
        FWObbiettivi.info(Messages.CONFIG_RELOAD);
        if (sender instanceof Player) {
            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOAD));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
