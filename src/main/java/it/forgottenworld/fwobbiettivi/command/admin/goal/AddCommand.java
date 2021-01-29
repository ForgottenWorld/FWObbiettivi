package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.utility.*;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddCommand extends SubCommand {

    @Override
    public String getName() {
        return GoalCommandNames.ADD_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.ADD_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_ADD;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    public void perform(CommandSender sender, String[] args) {
        // Adding a Goal to a existing Town
        Player player = (Player) sender;

        // Save location of chest
        Block b = player.getTargetBlock(null, 5);
        if (b.getType() != Material.CHEST) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_CHEST));
            return;
        } else {
            Chest chest = (Chest) b.getState();
            if (chest.getInventory() instanceof DoubleChestInventory) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_DOUBLE_CHEST));
                return;
            }
        }

        // Check if chest is already a goal
        if (!GoalAreaManager.getListTownGoalFromChunk(b.getChunk()).isEmpty() &&
                !GoalAreaManager.getListTownGoalFromChunk(b.getChunk()).stream().filter(c -> c.getLocation().equals(b.getLocation())).collect(Collectors.toList()).isEmpty()){
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_ALREADY_PRESENT_HERE));
            return;
        }

        Location loc = b.getLocation();

        // Check if the location is in a Town
        if (!TownyUtil.isInTown(b.getLocation())) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
            return;
        }

        Town town = TownyUtil.getTownFromLocation(b.getLocation());
        if (town == null) {
            // Not in a town
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
            return;
        }

        Goal goal = Goals.getGoalFromString(args[1]);
        if (goal == null) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
            return;
        }

        if (GoalAreaManager.getChunks().get(player.getLocation().getChunk()) != null) {
            if (GoalAreaManager.getListTownGoalFromChunk(player.getLocation().getChunk()).size() + (GoalAreaManager.getTreasuryCurrentlyFromChunk(player.getLocation().getChunk()) == null ? 0 : 1) >= ConfigUtil.MAX_GOAL_IN_CHUNK) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.MAX_GOAL_IN_CHUNK));
                return;
            }
        }

        // Check if the Goal is already present in Town
        if (TownGoals.containsTownGoal(goal, TownyUtil.getTownFromLocation(player.getLocation()))) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_ALREADY_PRESENT));
            return;
        }

        // Check if a Treasury is already present in Town
        if (!ConfigUtil.MULTI_GOAL) {
            if (GoalAreaManager.isOnTreasury(player.getLocation().getChunk())) {
                player.sendMessage(ChatFormatter.formatErrorMessage(Messages.TREASURY_ALREADY_PRESENT));
                return;
            }
        }

        // Adding Goal to that Town
        TownGoal tg = new TownGoal(town, goal, loc);

        // Check if town has required zenar
        if (goal.getRequiredZenar() > 0) {
            try {
                if (town.getAccount().getHoldingBalance() <= goal.getRequiredZenar()) {
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ENOUGH_MONEY) + " " + ChatFormatter.formatWarningMessageNoPrefix(town.getName()));
                    return;
                }
            } catch (EconomyException e) {
                e.printStackTrace();
            }
        }

        // Check if the town has required goals and if they are active
        List<String> neededGoals = tg.getGoal().getRequiredGoals();
        for (String s : neededGoals){
            if (!s.equals(ConfigUtil.TREASURY)){
                if (!TownGoals.containsTownGoal(Goals.getGoalFromString(s), town) ||
                        !TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(s), town).isActive()){
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_FATHER_GOAL) + " " + ChatFormatter.formatWarningMessageNoPrefix(s));
                    return;
                }
            } else {
                if (Treasuries.getFromTown(town) == null){
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TREASURY));
                    return;
                }
            }
        }

        // Check objects
        Treasury tes = Treasuries.getFromTown(town);

        if (!goal.getRequiredObjects().isEmpty()) {
            for (ItemStack is : goal.getRequiredObjects()) {
                int quantity = is.getAmount();
                for (int i = 0; i < tes.getTreasuryChestInventory().getSize(); i++) {
                    if (quantity > 0 && tes.getTreasuryChestInventory().getItem(i) != null && tes.getTreasuryChestInventory().getItem(i).getType() == is.getType())
                        if (quantity >= tes.getTreasuryChestInventory().getItem(i).getAmount()) {
                            quantity -= tes.getTreasuryChestInventory().getItem(i).getAmount();
                        } else {
                            quantity = 0;
                        }
                }
                if (quantity > 0){
                    player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_REQUEST_OBJECT));
                    return;
                }
            }
        }

        TownGoals.addTownGoal(tg);
        // Adding chunk to the chunk control system
        GoalAreaManager.addChunk(loc.getChunk(), tg);

        if (tg.getGoal().getNumPlot() > 1)
            GoalAreaManager.getInstance().putGoalAreaCreation(player.getUniqueId(), tg);

        // Rename plot to Goal name
        TownyUtil.renamePlot(b.getLocation(), goal.getName(), false);

        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_ADDED) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));

        b.getWorld().playSound(b.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        GoalAreaManager.spawnEffectAtBlock(b.getLocation());

        if (goal.getNumPlot() > 1)
            player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix("1/" + goal.getNumPlot()));

        // At the end
        // Get objects
        if (!goal.getRequiredObjects().isEmpty()) {
            for (ItemStack is : goal.getRequiredObjects()) {
                int quantity = is.getAmount();
                for (int i = 0; i < tes.getTreasuryChestInventory().getSize(); i++) {
                    if (quantity > 0 && tes.getTreasuryChestInventory().getItem(i) != null && tes.getTreasuryChestInventory().getItem(i).getType() == is.getType())
                        if (quantity >= tes.getTreasuryChestInventory().getItem(i).getAmount()) {
                            quantity -= tes.getTreasuryChestInventory().getItem(i).getAmount();
                            tes.getTreasuryChestInventory().setItem(i, null);
                        } else {
                            int newAmount = tes.getTreasuryChestInventory().getItem(i).getAmount() - quantity;
                            quantity = 0;
                            tes.getTreasuryChestInventory().setItem(i, new ItemStack(is.getType(), newAmount));
                        }
                }
            }
            player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TOWN_OBJECTS_WITHDRAWN));
        }

        // Withdraw zenar
        if (goal.getRequiredZenar() > 0) {
            try {
                town.getAccount().withdraw(goal.getRequiredZenar(), "Goal creation payment.");
                player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TOWN_MONEY_WITHDRAWN));
            } catch (EconomyException e) {
                e.printStackTrace();
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
