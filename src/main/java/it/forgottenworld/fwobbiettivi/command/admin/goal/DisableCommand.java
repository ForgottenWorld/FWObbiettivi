package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
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

public class DisableCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.DISABLE_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.DISABLE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName> <town>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_DISABLE;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 3;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!TownGoals.containsTownGoal(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]))) {
            // Disable failed
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.DISABLE_FAILED));
            return;
        }

        TownGoal townGoalsDisable = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]));
        // Disable success
        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.DISABLE_GOAL) + " " + ChatFormatter.formatWarningMessageNoPrefix(townGoalsDisable.getGoal().getName() + " - " + townGoalsDisable.getTown().getName()));
        townGoalsDisable.setActive(false);
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

        if (args.length == 3){
            Town[] towns = TownGoals.getTownFromGoal(Goals.getGoalFromString(args[1])).toArray(new Town[0]);
            TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
            for (int i = 0; i < towns.length; i++) {
                if (towns[i].getName().toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(towns[i].getName());
            }
        }

        return result;
    }
}
