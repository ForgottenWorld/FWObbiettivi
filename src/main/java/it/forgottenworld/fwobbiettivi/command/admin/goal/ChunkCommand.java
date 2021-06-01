package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChunkCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.CHUNK_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.CHUNK_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "add/remove <obName> <town> <nPlot>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_CHUNK;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 5;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (Integer.parseInt(args[4]) == 0) {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_ZERO_ALLOW));
            return;
        }
        if (Goals.isGoal(args[2]) && TownyUtil.isTown(args[3])) {
            Goal g = Goals.getGoalFromString(args[2]);
            Town t = TownyUtil.getTownFromString(args[3]);
            TownGoal tg = TownGoals.getTownGoalFromGoalAndTown(g, t);
            Player p = (Player) sender;

            if (args[1].equalsIgnoreCase("add")) {
                GoalAreaManager.getInstance().putGoalAreaEdit(p.getUniqueId(), tg, Integer.parseInt(args[4]));
                p.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix(GoalAreaManager.getChunksFromTownGoal(tg).size() + "/" + (tg.getGoal().getNumPlot() + Integer.parseInt(args[4]))));
            } else if (args[1].equalsIgnoreCase("remove")) {
                if ((GoalAreaManager.getChunksFromTownGoal(tg).size() - Integer.parseInt(args[4])) > tg.getGoal().getNumPlot()) {
                    // ok
                    Chunk c = p.getChunk();
                    
                } else {
                    p.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix(GoalAreaManager.getChunksFromTownGoal(tg).size() + "/" + tg.getGoal().getNumPlot()));
                }
            }
        } else {
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_OR_GOAL_FOUND));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
