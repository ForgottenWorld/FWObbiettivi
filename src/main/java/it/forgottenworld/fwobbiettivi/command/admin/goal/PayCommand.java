package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.PAY_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.PAY_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        // TODO
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_PAY;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return true;
    }

    @Override
    public int getArgsRequired() {
        // TODO
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        for (TownGoal townGoals : TownGoals.getObbiettiviInTown()) {
            // Get the block chest at the coords
            Block block = townGoals.getLocation().getBlock();

            // Chest not found, I disable the goal
            if (block.getType() != Material.CHEST) {
                FWObbiettivi.info(Messages.DISABLE_GOAL + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                townGoals.setActive(false);
                TownGoals.save();
            }

            Chest fwchest = (Chest) block.getState();

            if (townGoals.isActive()) {
                // Check if the inventory contains items
                if (fwchest.getBlockInventory().isEmpty()) {
                    // Inventory empty
                    FWObbiettivi.info(Messages.GOAL_NOT_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                } else {
                    townGoals.pay();
                    FWObbiettivi.info(Messages.GOAL_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                }
            } else {
                FWObbiettivi.info(Messages.GOAL_IS_DISABLE + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
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
