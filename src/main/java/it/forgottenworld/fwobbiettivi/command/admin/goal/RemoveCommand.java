package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.RewardPermissions;
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

public class RemoveCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.REMOVE_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.REMOVE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName> <town>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_REMOVE;
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
        Player player = (Player) sender;

        if (TownGoals.getObbiettiviInTown().isEmpty()) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
            return;
        }

        // Check if a goal exist in that town
        if (args.length == 2) {
            ArrayList<Goal> goals = TownGoals.getGoalFromTown(TownyUtil.getTownFromLocation(player.getLocation()));

            if (goals == null) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                return;
            }

            for (Goal g : goals) {
                if (g.getRequiredGoals().contains(args[1])) {
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_REMOVE_FAILED));
                    return;
                }
            }
            // Check if the Goal exist in this plot
            if (TownyUtil.isInTown(player.getLocation())) {
                if (GoalAreaManager.isOnTownGoal(player.getLocation().getChunk()) &&
                        GoalAreaManager.getListTownGoalFromChunk(player.getLocation().getChunk()).contains(TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromLocation(player.getLocation())))) {
                    List<TownGoal> townGoals = GoalAreaManager.getListTownGoalFromChunk(player.getLocation().getChunk());
                    TownGoal tgRemove = null;
                    for (TownGoal tg:townGoals) {
                        if (tg.getGoal().getName().equals(args[1])) {
                            tgRemove = tg;
                        }
                    }

                    if (tgRemove != null) {
                        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tgRemove.getGoal().getName()));
                        TownGoals.removeTownGoal(tgRemove);

                        // Remove perms if it has them
                        if (!tgRemove.getGoal().getRewardPermissions().isEmpty()) {
                            for (String s : tgRemove.getGoal().getRewardPermissions()) {
                                RewardPermissions.removePermission(player, s);
                            }
                        }
                    }

                } else {
                    // Not in a goal plot
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
                }
            } else {
                // Not in a town
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
            }
        } else {
            ArrayList<Goal> goals = TownGoals.getGoalFromTown(TownyUtil.getTownFromString(args[2]));

            if (goals == null) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                return;
            }

            for (Goal g : goals) {
                if (g.getRequiredGoals().contains(args[1])) {
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_REMOVE_FAILED));
                    return;
                }
            }

            if (TownyUtil.getTownFromString(args[2]) == null) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_FOUND));
                return;
            }
            if (!TownGoals.containsTownGoal(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]))) {
                // Enable failed
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                return;
            }
            TownGoal tg = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]));
            player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tg.getGoal().getName()));
            TownGoals.removeTownGoal(tg);

            // Remove perms if it has them
            if (!tg.getGoal().getRewardPermissions().isEmpty()) {
                for (String s : tg.getGoal().getRewardPermissions()) {
                    RewardPermissions.removePermission(player, s);
                }
            }
        }
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
