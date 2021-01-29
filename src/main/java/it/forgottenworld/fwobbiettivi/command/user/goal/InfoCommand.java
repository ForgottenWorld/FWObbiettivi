package it.forgottenworld.fwobbiettivi.command.user.goal;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.INFO_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.INFO_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_INFO;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // TODO info command
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
