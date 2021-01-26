package it.forgottenworld.fwobbiettivi.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class GoalsCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length >= 1){
            switch (args[0].toLowerCase()) {
                case CommandTypes.ADD_COMMAND:
                    // Adding a Goal to a existing Town
                    // Check if the sender is a player
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerAdd = (Player) sender;

                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_ADD))
                        return true;

                    // Save location of chest
                    Block b = playerAdd.getTargetBlock(null, 5);
                    if (b.getType() != Material.CHEST) {
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_CHEST));
                        return true;
                    } else {
                        Chest chest = (Chest) b.getState();
                        if (chest.getInventory() instanceof DoubleChestInventory) {
                            playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_DOUBLE_CHEST));
                            return true;
                        }
                    }

                    // Check if chest is already a goal
                    if (!GoalAreaManager.getListTownGoalFromChunk(b.getChunk()).isEmpty() &&
                            !GoalAreaManager.getListTownGoalFromChunk(b.getChunk()).stream().filter(c -> c.getLocation().equals(b.getLocation())).collect(Collectors.toList()).isEmpty()){

                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_ALREADY_PRESENT_HERE));
                        return true;
                    }

                    Location loc = b.getLocation();

                    // Check if the location is in a Town
                    if (!TownyUtil.isInTown(b.getLocation())) {
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        return true;
                    }

                    Town town = TownyUtil.getTownFromLocation(b.getLocation());
                    if (town == null) {
                        // Not in a town
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        return true;
                    }

                    // Check if the Goal exist
                    if (args.length > 1) {
                        boolean founded = false;
                        Goal goal = Goals.getGoalFromString(args[1]);
                        if (goal == null) {
                            playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
                            return true;
                        }

                        if (GoalAreaManager.getChunks().get(playerAdd.getLocation().getChunk()) != null) {
                            if (GoalAreaManager.getListTownGoalFromChunk(playerAdd.getLocation().getChunk()).size() + (GoalAreaManager.getTreasuryCurrentlyFromChunk(playerAdd.getLocation().getChunk()) == null ? 0 : 1) >= ConfigUtil.MAX_GOAL_IN_CHUNK) {
                                playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.MAX_GOAL_IN_CHUNK));
                                return true;
                            }
                        }

                        // Check if the Goal is already present in Town
                        if (TownGoals.containsTownGoal(goal, TownyUtil.getTownFromLocation(playerAdd.getLocation()))) {
                            playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_ALREADY_PRESENT));
                            return true;
                        }

                        // Check if a Treasury is already present in Town
                        if (!ConfigUtil.MULTI_GOAL) {
                            if (GoalAreaManager.isOnTreasury(playerAdd.getLocation().getChunk())) {
                                playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.TREASURY_ALREADY_PRESENT));
                                return true;
                            }
                        }

                        // Adding Goal to that Town
                        TownGoal tg = new TownGoal(town, goal, loc);

                        // Check if town has required zenar
                        try {
                            if (town.getAccount().getHoldingBalance() <= goal.getRequiredZenar()){
                                playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_ENOUGH_MONEY) + " " + ChatFormatter.formatWarningMessageNoPrefix(town.getName()));
                                return true;
                            }
                        } catch (EconomyException e) {
                            e.printStackTrace();
                        }

                        // Check if the town has required goals and if they are active
                        List<String> neededGoals = tg.getGoal().getRequiredGoals();
                        for (String s : neededGoals){
                            if (!s.equals(ConfigUtil.TREASURY)){
                                if (!TownGoals.containsTownGoal(Goals.getGoalFromString(s), town) &&
                                        !TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(s), town).isActive()){
                                    playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_FATHER_GOAL) + ChatFormatter.formatWarningMessageNoPrefix(s));
                                    return true;
                                }
                            } else {
                                if (Treasuries.getFromTown(town) == null){
                                    playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TREASURY));
                                    return true;
                                }
                            }
                        }

                        // Check objects
                        Treasury tes = Treasuries.getFromTown(town);

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
                                playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_REQUEST_OBJECT));
                                return true;
                            }
                        }

                        TownGoals.addTownGoal(tg);
                        // Adding chunk to the chunk control system
                        GoalAreaManager.addChunk(loc.getChunk(), tg);

                        if (tg.getGoal().getNumPlot() > 1)
                            GoalAreaManager.getInstance().putGoalAreaCreation(playerAdd.getUniqueId(), tg);

                        // Rename plot to Goal name
                        TownyUtil.renamePlot(b.getLocation(), goal.getName(), false);

                        playerAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_ADDED) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));

                        b.getWorld().playSound(b.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        GoalAreaManager.spawnEffectAtBlock(b.getLocation());

                        if (goal.getNumPlot() > 1)
                            playerAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix("1/" + goal.getNumPlot()));

                        // At the end
                        // Get objects
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

                        // Withdraw zenar
                        try {
                            town.getAccount().withdraw(goal.getRequiredZenar(), "Goal creation payment.");
                            playerAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TOWN_MONEY_WITHDRAWN));
                        } catch (EconomyException e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL));
                        return true;
                    }

                case CommandTypes.CREATE_COMMAND:
                    // TODO Create a new Goal
                    break;

                case CommandTypes.DELETE_COMMAND:
                    // TODO Delete a Goal
                    break;

                case CommandTypes.DISABLE_COMMAND:
                    // Disabling a Goal to a existing Town
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerDisable = (Player) sender;

                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_DISABLE))
                        return true;

                    // Check if a goal exist in that town
                    if (args.length > 2) {
                        if (!TownGoals.containsTownGoal(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]))) {
                            // Disable failed
                            playerDisable.sendMessage(ChatFormatter.formatErrorMessage(Messages.DISABLE_FAILED));
                            return true;
                        }
                        TownGoal townGoalsDisable = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]));
                        // Disable success
                        playerDisable.sendMessage(ChatFormatter.formatSuccessMessage(Messages.DISABLE_GOAL) + " " + ChatFormatter.formatWarningMessageNoPrefix(townGoalsDisable.getGoal().getName() + " - " + townGoalsDisable.getTown().getName()));
                        townGoalsDisable.setActive(false);
                        
                        return true;
                    } else {
                        playerDisable.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                case CommandTypes.EDIT_COMMAND:
                    // TODO Edit a Goal
                    break;

                case CommandTypes.ENABLE_COMMAND:
                    // Enabling a Goal to a existing Town
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerEnable = (Player) sender;

                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_ENABLE))
                        return true;

                    // Check if a goal exist in that town
                    if (args.length > 2) {
                        if (!TownGoals.containsTownGoal(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]))) {
                            // Enable failed
                            playerEnable.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                            return true;
                        }
                        TownGoal townGoalsEnable = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]));
                        // Enable success
                        playerEnable.sendMessage(ChatFormatter.formatSuccessMessage(Messages.ENABLE_GOAL) + " " + ChatFormatter.formatWarningMessageNoPrefix(townGoalsEnable.getGoal().getName() + " - " + townGoalsEnable.getTown().getName()));
                        townGoalsEnable.setActive(true);

                        return true;
                    } else {
                        playerEnable.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                case CommandTypes.GUI_COMMAND:
                    // Check if the sender is a player
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerGui = (Player) sender;
                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_GUI))
                        return true;

                    FWObbiettivi.getInstance().map.put(playerGui, new GoalsGUI());
                    FWObbiettivi.getInstance().map.get(playerGui).setPlayer(playerGui);
                    FWObbiettivi.getInstance().map.get(playerGui).openGUI(GUIUtil.GOALS_STEP);
                    return true;

                case CommandTypes.HELP_COMMAND:
                    // Send list of available commands
                    sender.sendMessage(ChatFormatter.helpMessage());
                    return true;

                case CommandTypes.LIST_COMMAND:
                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_LIST))
                        return true;

                    // TODO List Goals Pagination
                    sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOALS_LIST));
                    for (Goal g : Goals.getObbiettivi()) {
                        sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + g.getName()));
                    }

                    break;

                case CommandTypes.PAY_COMMAND:
                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_PAY))
                        return true;

                    for (TownGoal townGoals : TownGoals.getObbiettiviInTown()) {
                        // Get the block chest at the coords
                        Block block = townGoals.getLocation().getBlock();

                        // Chest not found, I disable the goal
                        if (block.getType() != Material.CHEST) {
                            FWObbiettivi.info(Messages.DISABLE_GOAL + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                            townGoals.setActive(false);
                            TownGoals.save();
                            return true;
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
                    return true;

                case CommandTypes.RELOAD_COMMAND:
                    // Reload the server config.yml
                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_RELOAD))
                        return true;

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
                        return true;
                    }
                    return true;

                case CommandTypes.REMOVE_COMMAND:
                    // Removing a Goal to a existing Town
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerRemove = (Player) sender;

                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_REMOVE))
                        return true;

                    if (TownGoals.getObbiettiviInTown().isEmpty()) {
                        playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
                        return true;
                    }

                    // Check if the location is in a Town
                    Town townRemove = null;

                    // Check if a goal exist in that town
                    if (args.length <= 1) {
                        playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    } else if (args.length == 2) {
                        // Check if the Goal exist in this plot
                        if (TownyUtil.isInTown(playerRemove.getLocation())) {
                            if (GoalAreaManager.isOnTownGoal(playerRemove.getLocation().getChunk())) {
                                List<TownGoal> townGoals = GoalAreaManager.getListTownGoalFromChunk(playerRemove.getLocation().getChunk());
                                TownGoal tgRemove = null;
                                for (TownGoal tg:townGoals) {
                                    if (tg.getGoal().getName().equals(args[1])) {
                                        tgRemove = tg;
                                    }
                                }

                                if (tgRemove != null) {
                                    playerRemove.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tgRemove.getGoal().getName()));
                                    TownGoals.removeTownGoal(tgRemove);
                                }

                                return true;
                            } else {
                                // Not in a goal plot
                                playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
                                return true;
                            }
                        } else {
                            // Not in a town
                            playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                            return true;
                        }
                    } else if (args.length == 3) {
                        if (!TownGoals.containsTownGoal(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]))) {
                            // Enable failed
                            playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                            return true;
                        }
                        TownGoal tg = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]));
                        playerRemove.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tg.getGoal().getName()));
                        TownGoals.removeTownGoal(tg);
                        return true;
                    } else {
                        playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.TOO_MANY_INFO));
                        return true;
                    }

                case CommandTypes.SHOW_COMMAND:
                    // TODO Show Goal
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_SHOW))
                        return true;

                    Player playerShow = (Player) sender;

                    // Check if args[1] is a Town or a Goal
                    if (args.length == 2){

                    } else {
                        playerShow.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                    // Print the results: Goal or Town

                    break;

                case CommandTypes.STATUS_COMMAND:
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerStatus = (Player) sender;

                    // Do you have the permissions?
                    if (!Permissions.playerHasPermission(sender, Permissions.PERM_STATUS))
                        return true;

                    if (TownGoals.getObbiettiviInTown().isEmpty()) {
                        playerStatus.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
                        return true;
                    }

                    // Check if the location is in a Town
                    Town townStatus = null;

                    // Check if a goal exist in that town
                    // FIXME database.ylm
                    playerStatus.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TOO_MANY_INFO));
                    for (TownGoal tg : GoalAreaManager.getListTownGoalFromChunk(playerStatus.getLocation().getChunk())){
                        if (tg.isActive()) {
                            playerStatus.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + tg.getGoal().getName()) + " - " + ChatFormatter.formatSuccessMessageNoPrefix("ENABLED"));
                        } else {
                            playerStatus.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + tg.getGoal().getName()) + " - " + ChatFormatter.formatErrorMessageNoPrefix("DISABLED"));
                        }
                    }
                    return true;

                case CommandTypes.TP_COMMAND:
                    // Teleport a player to a goal
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerTp = (Player) sender;

                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_TP))
                        return true;

                    Location locationTp = null;

                    if(args.length > 2) {
                        for (TownGoal townGoalsTp : TownGoals.getObbiettiviInTown()) {
                            if (args[1].equals(townGoalsTp.getGoal().getName()) && args[2].equals(townGoalsTp.getTown().getName())) {
                                locationTp = townGoalsTp.getLocation();
                                playerTp.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TELEPORTED_TO) + " " + ChatFormatter.formatWarningMessageNoPrefix(townGoalsTp.getGoal().getName()));
                                playerTp.teleport(locationTp);
                                // Teleport success
                                return true;
                            }
                        }
                    } else {
                        playerTp.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                    // Teleport failed
                    playerTp.sendMessage(ChatFormatter.formatErrorMessage(Messages.TELEPORTED_FAILED));
                    break;

                case CommandTypes.TREASURY_COMMAND:
                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_TREASURY))
                        return true;

                    if (args.length < 2){
                        sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case CommandTypes.TREASURY_ADD_COMMAND:
                            // Teleport a player to a goal
                            if (!(sender instanceof Player)){
                                sender.sendMessage(Messages.NO_CONSOLE);
                                return true;
                            }

                            Player playerTesAdd = (Player) sender;

                            // Do you have the permissions?
                            if(!Permissions.playerHasPermission(sender, Permissions.PERM_TREASURY_ADD))
                                return true;

                            // Save location of chest
                            Block bTesAdd = playerTesAdd.getTargetBlock(null, 5);
                            if (bTesAdd.getType() != Material.CHEST) {
                                playerTesAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_CHEST));
                                return true;
                            } else {
                                Chest chest = (Chest) bTesAdd.getState();
                                if (!(chest.getInventory() instanceof DoubleChestInventory)) {
                                    playerTesAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_SINGLE_CHEST));
                                    return true;
                                }

                                if (!TownyUtil.isInTown(bTesAdd.getLocation())){
                                    // Not in a town
                                    playerTesAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                                    return true;
                                }

                                Chest right = (Chest)((DoubleChest) chest.getInventory().getHolder()).getRightSide();
                                Chest left = (Chest)((DoubleChest) chest.getInventory().getHolder()).getLeftSide();

                                if (!Treasuries.isSameChunk(right.getLocation(), left.getLocation())){
                                    // Not same location
                                    playerTesAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NOT_SAME_LOC));
                                    return true;
                                }

                                Town townTes = TownyUtil.getTownFromLocation(bTesAdd.getLocation());
                                if (townTes == null) {
                                    // Not in a town
                                    playerTesAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                                    return true;
                                }

                                if (args.length > 2) {
                                    sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.TOO_MANY_INFO));
                                    return true;
                                }

                                // Check if the Treasury exist
                                if (args.length == 2) {
                                    // Check if the Goal is alredy present in Town
                                    if (Treasuries.containsTreasury(TownyUtil.getTownFromLocation(playerTesAdd.getLocation()))) {
                                        playerTesAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.TREASURY_ALREADY_PRESENT));
                                        return true;
                                    }

                                    // Adding Treaury to that Town
                                    Treasury tes = new Treasury(ConfigUtil.getTreasuryName(), townTes, right.getLocation(), left.getLocation(), ConfigUtil.getTreasuryNumPlot());
                                    Treasuries.addTreasury(tes);
                                    // Adding chunk to the chunk control system
                                    GoalAreaManager.addChunkTes(right.getLocation().getChunk(), tes);
                                    GoalAreaManager.getInstance().putTesAreaCreation(playerTesAdd.getUniqueId(), tes);

                                    // Rename plot to Treasury name
                                    TownyUtil.renamePlot(playerTesAdd.getLocation(), tes.getName(), false);

                                    playerTesAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_ADDED) + " " + ChatFormatter.formatWarningMessageNoPrefix(townTes.getName()));

                                    bTesAdd.getWorld().playSound(bTesAdd.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    GoalAreaManager.spawnEffectAtBlock(bTesAdd.getLocation());

                                    if (tes.getNumPlot() > 1)
                                        playerTesAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix("1/" + tes.getNumPlot()));

                                    return true;
                                }
                            }
                            break;

                        case CommandTypes.TREASURY_OPEN_COMMAND:
                            // Teleport a player to a goal
                            if (!(sender instanceof Player)){
                                sender.sendMessage(Messages.NO_CONSOLE);
                                return true;
                            }

                            // Do you have the permissions?
                            if(!Permissions.playerHasPermission(sender, Permissions.PERM_TREASURY_OPEN))
                                return true;

                            if (args.length > 3){
                                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.TOO_MANY_INFO));
                                return true;
                            }

                            if (args.length < 3){
                                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                                return true;
                            }

                            try {
                                Treasury t = Treasuries.getFromTown(TownyAPI.getInstance().getDataSource().getTown(args[2]));

                                if (t == null){
                                    sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TREASURY) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[2]));
                                } else {
                                    t.openChest((Player) sender);
                                }
                            } catch (NotRegisteredException e) {
                                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_FOUND));
                            }
                            break;

                        case CommandTypes.TREASURY_REMOVE_COMMAND:
                            // Removing a Goal to a existing Town
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(Messages.NO_CONSOLE);
                                return true;
                            }

                            Player playerTesRemove = (Player) sender;

                            // Do you have the permissions?
                            if(!Permissions.playerHasPermission(sender, Permissions.PERM_TREASURY_REMOVE))
                                return true;

                            if (args.length > 3){
                                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.TOO_MANY_INFO));
                                return true;
                            }

                            // Check if the location is in a Town
                            Town townTesRemove = null;

                            // Check if a goal exist in that town
                            if (args.length == 2){
                                // Check if the Goal exist in this plot
                                if (TownyUtil.isInTown(playerTesRemove.getLocation())) {
                                    if (GoalAreaManager.isOnTreasury(playerTesRemove.getLocation().getChunk())) {
                                        Treasury tes = GoalAreaManager.getTreasuryCurrentlyFromChunk(playerTesRemove.getLocation().getChunk());
                                        playerTesRemove.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tes.getTown().getName()));
                                        Treasuries.removeTreasury(tes);
                                        return true;
                                    } else {
                                        // Not in a goal plot
                                        playerTesRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
                                        return true;
                                    }
                                } else {
                                    // Not in a town
                                    playerTesRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                                    return true;
                                }
                            }else if(args.length <= 3) {
                                if (!TownGoals.containsTownGoal(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]))) {
                                    // Enable failed
                                    playerTesRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                                    return true;
                                }
                                Treasury tes = Treasuries.getFromTown(TownyUtil.getTownFromString(args[2]));
                                playerTesRemove.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tes.getTown().getName()));
                                Treasuries.removeTreasury(tes);
                                return true;
                            } else {
                                playerTesRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.TOO_MANY_INFO));
                                return true;
                            }

                        case CommandTypes.TREASURY_SHOW_COMMAND:
                            // Do you have the permissions?
                            if(!Permissions.playerHasPermission(sender, Permissions.PERM_TREASURY_SHOW))
                                return true;

                            sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_LIST));
                            sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix(Treasuries.getPrintableList()));
                            break;

                        case CommandTypes.TREASURY_TP_COMMAND:
                            // Teleport a player to a goal
                            if (!(sender instanceof Player)){
                                sender.sendMessage(Messages.NO_CONSOLE);
                                return true;
                            }

                            // Do you have the permissions?
                            if(!Permissions.playerHasPermission(sender, Permissions.PERM_TREASURY_TP))
                                return true;

                            Player playerTesTp = (Player) sender;

                            Location locationTesTp = null;

                            if(args.length > 2) {
                                for (Treasury tes : Treasuries.getTreasuries()) {
                                    if (args[2].equals(tes.getTown().getName())) {
                                        locationTesTp = tes.getLocationChestRight();
                                        playerTesTp.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TELEPORTED_TO) + " " + ChatFormatter.formatWarningMessageNoPrefix(tes.getTown().getName()));
                                        playerTesTp.teleport(locationTesTp);
                                        // Teleport success
                                        return true;
                                    }
                                }
                            } else {
                                playerTesTp.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                                return true;
                            }

                            // Teleport failed
                            playerTesTp.sendMessage(ChatFormatter.formatErrorMessage(Messages.TELEPORTED_FAILED));

                            break;
                    }
                    break;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        List<String> result = new ArrayList<String>();

        // TODO suggestions command

        if(suggestions.isEmpty()) {
            if (sender.hasPermission(Permissions.PERM_ADD))
                suggestions.add(CommandTypes.ADD_COMMAND);

            if (sender.hasPermission(Permissions.PERM_CREATE))
                suggestions.add(CommandTypes.CREATE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_DELETE))
                suggestions.add(CommandTypes.DELETE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_DISABLE))
                suggestions.add(CommandTypes.DISABLE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_EDIT))
                suggestions.add(CommandTypes.EDIT_COMMAND);

            if (sender.hasPermission(Permissions.PERM_ENABLE))
                suggestions.add(CommandTypes.ENABLE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_GUI))
                suggestions.add(CommandTypes.GUI_COMMAND);

            suggestions.add(CommandTypes.HELP_COMMAND);

            if (sender.hasPermission(Permissions.PERM_LIST))
                suggestions.add(CommandTypes.LIST_COMMAND);

            if (sender.hasPermission(Permissions.PERM_PAY))
                suggestions.add(CommandTypes.PAY_COMMAND);

            if (sender.hasPermission(Permissions.PERM_RELOAD))
                suggestions.add(CommandTypes.RELOAD_COMMAND);

            if (sender.hasPermission(Permissions.PERM_REMOVE))
                suggestions.add(CommandTypes.REMOVE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_SHOW))
                suggestions.add(CommandTypes.SHOW_COMMAND);

            if (sender.hasPermission(Permissions.PERM_STATUS))
                suggestions.add(CommandTypes.STATUS_COMMAND);

            if (sender.hasPermission(Permissions.PERM_TP))
                suggestions.add(CommandTypes.TP_COMMAND);

            if (sender.hasPermission(Permissions.PERM_TREASURY))
                suggestions.add(CommandTypes.TREASURY_COMMAND);
        }

        if(args.length == 1){
            for (String a : suggestions){
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }

            return result;
        }

        if(args.length == 2){
            switch (args[0]){
                case CommandTypes.ADD_COMMAND:
                case CommandTypes.DISABLE_COMMAND:
                case CommandTypes.ENABLE_COMMAND:
                case CommandTypes.REMOVE_COMMAND:
                case CommandTypes.TP_COMMAND:
                    for (Goal goal: Goals.getObbiettivi()){
                        if(goal.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(goal.getName());
                    }
                    break;
                case CommandTypes.TREASURY_COMMAND:
                    List<String> arguments = new ArrayList<String>();
                    if (sender.hasPermission(Permissions.PERM_TREASURY_ADD))
                        arguments.add(CommandTypes.TREASURY_ADD_COMMAND);

                    if (sender.hasPermission(Permissions.PERM_TREASURY_OPEN))
                        arguments.add(CommandTypes.TREASURY_OPEN_COMMAND);

                    if (sender.hasPermission(Permissions.PERM_TREASURY_REMOVE))
                        arguments.add(CommandTypes.TREASURY_REMOVE_COMMAND);

                    if (sender.hasPermission(Permissions.PERM_TREASURY_SHOW))
                        arguments.add(CommandTypes.TREASURY_SHOW_COMMAND);

                    if (sender.hasPermission(Permissions.PERM_TREASURY_TP))
                        arguments.add(CommandTypes.TREASURY_TP_COMMAND);

                    for (String a : arguments){
                        if(a.toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(a);
                    }

                    break;
            }

            return result;
        }

        if(args.length == 3){
            switch (args[0]){
                case CommandTypes.DISABLE_COMMAND:
                case CommandTypes.ENABLE_COMMAND:
                case CommandTypes.REMOVE_COMMAND:
                case CommandTypes.TP_COMMAND:
                    Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
                    TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
                    for (int i = 0; i < towns.length; i++) {
                        if (towns[i].getName().toLowerCase().startsWith(args[2].toLowerCase()))
                            result.add(towns[i].getName());
                    }
                    break;
            }

            for (String a : result){
                if(a.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(a);
            }

            return result;
        }

        return null;
    }

}
