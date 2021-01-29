package it.forgottenworld.fwobbiettivi.command.admin.goal;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.CREATE_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.CREATE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_CREATE;
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
        // TODO Create a new Goal
        Player player = (Player) sender;

        FWObbiettivi.getInstance().map.put(player, new GoalsGUI());
        FWObbiettivi.getInstance().map.get(player).setPlayer(player);
        FWObbiettivi.getInstance().map.get(player).openGUI(GUIUtil.GOALS_NEW_STEP, args[1]);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
