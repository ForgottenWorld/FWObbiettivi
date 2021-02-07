package it.forgottenworld.fwobbiettivi.command.admin.goal;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.DELETE_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.DELETE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_DELETE;
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
        // TODO Delete a Goal
        Player player = (Player) sender;
        Goal goal = Goals.getGoalFromString(args[1]);

        if (!Goals.containsGoal(goal)) {
            // TODO
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.BRANCH_NOT_EXIST));
            return;
        }

        // Check if exist town with this goal
        if (!TownGoals.getTownFromGoal(goal).isEmpty()) {
            // TODO
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.BRANCH_NOT_EMPTY));
            return;
        }

        // Delete Goal
        Goals.removeGoal(goal);
        // TODO
        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.BRANCH_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<String>();

        if (args.length == 2) {
            for (Goal goal : Goals.getObbiettivi()) {
                if (goal.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(goal.getName());
            }
        }

        return result;
    }
}
