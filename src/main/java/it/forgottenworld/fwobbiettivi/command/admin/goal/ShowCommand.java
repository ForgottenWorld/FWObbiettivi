package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShowCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.SHOW_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.SHOW_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName/town>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_SHOW;
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
        // TODO Pagination

        if (Goals.isGoal(args[1])){
            // Goals
            // List of Towns
            sender.sendMessage(ChatFormatter.chatHeader());
            sender.sendMessage(ChatFormatter.formatSuccessMessageNoPrefix(Messages.TOWNS_WITH_GOAL) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
            for (Town t : TownGoals.getTownFromGoal(Goals.getGoalFromString(args[1]))){
                sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + t.getName()) + " - " + (TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), t).isActive() ? ChatFormatter.formatSuccessMessageNoPrefix("ENABLED"):ChatFormatter.formatErrorMessageNoPrefix("DISABLED")));
            }
            sender.sendMessage(ChatFormatter.chatFooter());
        } else if (TownyUtil.isTown(args[1])){
            // Towns
            // List of Goals
            sender.sendMessage(ChatFormatter.chatHeader());
            sender.sendMessage(ChatFormatter.formatSuccessMessageNoPrefix(Messages.GOALS_IN_TOWN) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
            for (Goal g : TownGoals.getGoalFromTown(TownyUtil.getTownFromString(args[1]))){
                sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + g.getName()) + " - " + (TownGoals.getTownGoalFromGoalAndTown(g, TownyUtil.getTownFromString(args[1])).isActive() ? ChatFormatter.formatSuccessMessageNoPrefix("ENABLED"):ChatFormatter.formatErrorMessageNoPrefix("DISABLED")));
            }
            sender.sendMessage(ChatFormatter.chatFooter());
        } else {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_OR_GOAL_FOUND));
        }

    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<String>();

        // FIXME
        if (args.length == 2) {
            for (Goal goal : Goals.getObbiettivi()) {
                if (goal.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(goal.getName());
            }

            Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
            TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
            for (int i = 0; i < towns.length; i++) {
                if (towns[i].getName().toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(towns[i].getName());
            }
        }

        return result;
    }
}
