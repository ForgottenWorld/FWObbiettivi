package it.forgottenworld.fwobbiettivi.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.WorldCoord;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.*;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class GoalsCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length >= 1){
            switch (args[0].toLowerCase()){
                case CommandTypes.ADD_COMMAND:
                    // Adding a Goal to a existing Town
                    // Check if the sender is a player
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerAdd = (Player) sender;

                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_ADD))
                        return true;

                    // Save location of chest
                    Block b = playerAdd.getTargetBlock(null, 5);
                    if(b.getType() != Material.CHEST){
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_CHEST));
                        return true;
                    }else{
                        Chest chest = (Chest) b.getState();
                        if (chest.getInventory() instanceof DoubleChestInventory){
                            playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_DOUBLE_CHEST));
                            return true;
                        }
                    }

                    Location loc = b.getLocation();

                    // Check if the location is in a Town
                    Town town = null;
                    try {
                        // Try to get the town where the player is Standing
                        town = WorldCoord.parseWorldCoord(playerAdd.getLocation()).getTownBlock().getTown();
                    } catch (NotRegisteredException e) {
                        // Not in a town
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        return true;
                    }

                    // Check if the Goal exist
                    if(args.length > 1){
                        boolean founded = false;
                        for (Iterator<Goal> it = FWObbiettivi.getInstance().obbiettivi.iterator(); it.hasNext(); ) {
                            Goal goal = it.next();
                            if (goal.getName().equals(args[1])){
                                founded = true;
                                // Check if the Goal is alredy present in Town
                                for (TownGoals tg: FWObbiettivi.getInstance().obbiettiviInTown) {
                                    if (tg.getTown().equals(town) && tg.getGoal().equals(goal)){
                                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_ALREADY_PRESENT));
                                        return true;
                                    }
                                }

                                // Adding Goal to that Town
                                TownGoals tg = new TownGoals(town, goal, loc);
                                FWObbiettivi.getInstance().obbiettiviInTown.add(tg);
                                // Adding chunk to the chunk control system
                                FWObbiettivi.getInstance().chunks.put(new Pair<>(loc.getChunk().getX(), loc.getChunk().getZ()), tg);
                                GoalAreaManager.getInstance().putGoalAreaCreation(playerAdd.getUniqueId(), tg);

                                // Rename plot to Goal name
                                try {
                                    WorldCoord.parseWorldCoord(playerAdd.getLocation()).getTownBlock().setName(goal.getName());
                                    // Saving new plot name
                                    TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(playerAdd.getLocation()).getTownBlock());
                                } catch (NotRegisteredException e) {

                                }
                                Chest chestState = (Chest) b.getState();
                                chestState.setCustomName("FWChest");
                                b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));
                                playerAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_ADDED) + " " + ChatColor.GOLD + args[1]);
                                if (goal.getNumPlot() > 1)
                                    playerAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix("1/" + goal.getNumPlot()));

                                // Saving
                                FWObbiettivi.saveData();
                                break;
                            }
                        }
                        if(!founded){
                            playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + " " + ChatColor.GOLD + args[1]);
                            return true;
                        }
                    } else {
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL));
                        return true;
                    }
                    break;

                case CommandTypes.CREATE_COMMAND:
                    // TODO Create a new Goal
                    break;

                case CommandTypes.DELETE_COMMAND:
                    // TODO Delete a Goal
                    break;

                case CommandTypes.DISABLE_COMMAND:
                    // Disabling a Goal to a existing Town
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerDisable = (Player) sender;

                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_DISABLE))
                        return true;

                    // Check if a goal exist in that town
                    if(args.length > 2) {
                        for (TownGoals townGoalsDisable : FWObbiettivi.getInstance().obbiettiviInTown) {
                            if (args[1].equals(townGoalsDisable.getGoal().getName()) && args[2].equals(townGoalsDisable.getTown().getName())) {
                                // Disable success
                                playerDisable.sendMessage(ChatFormatter.formatSuccessMessage(Messages.DISABLE_GOAL) + " " + ChatColor.GOLD + townGoalsDisable.getGoal().getName() + " - " + townGoalsDisable.getTown().getName());
                                townGoalsDisable.setActive(false);
                                // Removing chunk from chunk control system
                                FWObbiettivi.getInstance().chunks.remove(new Pair<>(townGoalsDisable.getLocation().getChunk().getX(), townGoalsDisable.getLocation().getChunk().getZ()));
                                return true;
                            }
                        }
                    } else {
                        playerDisable.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                    // Disable failed
                    playerDisable.sendMessage(ChatFormatter.formatErrorMessage(Messages.DISABLE_FAILED));
                    break;

                case CommandTypes.EDIT_COMMAND:
                    // TODO Edit a Goal
                    break;

                case CommandTypes.ENABLE_COMMAND:
                    // Enabling a Goal to a existing Town
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerEnable = (Player) sender;

                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_ENABLE))
                        return true;

                    // Check if a goal exist in that town
                    if(args.length > 2) {
                        for (TownGoals townGoalsEnable : FWObbiettivi.getInstance().obbiettiviInTown) {
                            if (args[1].equals(townGoalsEnable.getGoal().getName()) && args[2].equals(townGoalsEnable.getTown().getName())) {
                                // Enable success
                                playerEnable.sendMessage(ChatFormatter.formatSuccessMessage(Messages.ENABLE_GOAL) + " " + ChatColor.GOLD + townGoalsEnable.getGoal().getName() + " - " + townGoalsEnable.getTown().getName());
                                townGoalsEnable.setActive(true);
                                // Adding chunk to the chunk control system
                                FWObbiettivi.getInstance().chunks.put(new Pair<>(townGoalsEnable.getLocation().getChunk().getX(), townGoalsEnable.getLocation().getChunk().getZ()), townGoalsEnable);
                                return true;
                            }
                        }
                    } else {
                        playerEnable.sendMessage(ChatFormatter.formatErrorMessage(Messages.MISSING_INFO));
                        return true;
                    }

                    // Enable failed
                    playerEnable.sendMessage(ChatFormatter.formatErrorMessage(Messages.ENABLE_FAILED));
                    break;

                case CommandTypes.GUI_COMMAND:
                    // Check if the sender is a player
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerGui = (Player) sender;
                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_GUI))
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
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_LIST))
                        return true;

                    // TODO List Goals Pagination
                    sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOALS_LIST));
                    for (Goal g: FWObbiettivi.getInstance().obbiettivi) {
                        sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + g.getName()));
                    }

                    break;

                case CommandTypes.MOVE_COMMAND:
                    // TODO Move Goal
                    break;

                case CommandTypes.PAY_COMMAND:
                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_PAY))
                        return true;

                    for(TownGoals townGoals:FWObbiettivi.getInstance().obbiettiviInTown){
                        // Get the block chest at the coords
                        Block block = townGoals.getLocation().getBlock();

                        // Chest not found, I disable the goal
                        if (block.getType() != Material.CHEST) {
                            FWObbiettivi.info(Messages.DISABLE_GOAL + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                            townGoals.setActive(false);
                            return true;
                        }

                        Chest fwchest = (Chest) block.getState();

                        // Check if the inventory contains items
                        if(fwchest.getBlockInventory().isEmpty()){
                            // Inventory empty
                            FWObbiettivi.info(Messages.GOAL_NOT_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                            return true;
                        }

                        if (townGoals.isActive()) {
                            townGoals.pay();
                            FWObbiettivi.info(Messages.GOAL_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                        } else {
                            FWObbiettivi.info(Messages.GOAL_IS_DISABLE + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                        }

                        // Saving
                        FWObbiettivi.saveData();
                    }
                    return true;

                case CommandTypes.RELOAD_COMMAND:
                    // Reload the server config.yml
                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_RELOAD))
                        return true;

                    FWObbiettivi.info("Saving infos...");
                    FWObbiettivi.saveData();
                    FWObbiettivi.info("Data saved");

                    FWObbiettivi.info("Loading infos...");
                    FWObbiettivi.loadData();
                    FWObbiettivi.info("Data loades");

                    FWObbiettivi.getInstance().reloadConfig();
                    FWObbiettivi.info(Messages.CONFIG_RELOAD);
                    if (sender instanceof Player){
                        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.CONFIG_RELOAD));
                        return true;
                    }
                    return true;

                case CommandTypes.REMOVE_COMMAND:
                    // Removing a Goal to a existing Town
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerRemove = (Player) sender;

                    // Do you have the permissions?
                    if(!Permissions.playerHasPermission(sender, Permissions.PERM_REMOVE))
                        return true;

                    // Get Goal from Location
                    Location locRemove = playerRemove.getLocation();

                    // Check if the location is in a Town
                    Town townRemove = null;

                    // Check if the Goal exist in this plot
                    for (TownGoals townGoalsRemove:FWObbiettivi.getInstance().obbiettiviInTown){
                        try {
                            if(WorldCoord.parseWorldCoord(locRemove).getTownBlock() == WorldCoord.parseWorldCoord(townGoalsRemove.getLocation()).getTownBlock()){

                                Block bRemove = townGoalsRemove.getLocation().getBlock();

                                if (bRemove.getType() == Material.CHEST) {
                                    ((Chest) bRemove.getState()).setCustomName("Chest");
                                    bRemove.removeMetadata("goalchest", FWObbiettivi.getInstance());
                                }

                                Iterator<Map.Entry<Pair<Integer, Integer>, TownGoals>> it = FWObbiettivi.getInstance().chunks.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry<Pair<Integer, Integer>, TownGoals> entry = it.next();
                                    if (townGoalsRemove.equals(entry.getValue())){
                                        try {
                                            WorldCoord.parseWorldCoord(new Location(playerRemove.getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16))).getTownBlock().setName("");
                                            // Saving new plot name
                                            TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(playerRemove.getLocation()).getTownBlock());
                                        } catch (NotRegisteredException e) {}
                                        it.remove();
                                    }
                                }

                                // Removing Goal to that Town
                                FWObbiettivi.getInstance().obbiettiviInTown.remove(townGoalsRemove);

                                playerRemove.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_REMOVED));

                                // Saving
                                FWObbiettivi.saveData();
                                return true;
                            }else{
                                // Not in a goal plot
                                playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
                                return true;
                            }
                        } catch (NotRegisteredException e) {
                            // Not in a town
                            playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                            return true;
                        }
                    }
                    break;

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
                        for (TownGoals townGoalsTp : FWObbiettivi.getInstance().obbiettiviInTown) {
                            if (args[1].equals(townGoalsTp.getGoal().getName()) && args[2].equals(townGoalsTp.getTown().getName())) {
                                locationTp = townGoalsTp.getLocation();
                                playerTp.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TELEPORTED_TO) + " " + ChatColor.GOLD + townGoalsTp.getGoal().getName());
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
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

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

            if (sender.hasPermission(Permissions.PERM_MOVE))
                suggestions.add(CommandTypes.MOVE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_PAY))
                suggestions.add(CommandTypes.PAY_COMMAND);

            if (sender.hasPermission(Permissions.PERM_RELOAD))
                suggestions.add(CommandTypes.RELOAD_COMMAND);

            if (sender.hasPermission(Permissions.PERM_REMOVE))
                suggestions.add(CommandTypes.REMOVE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_SHOW))
                suggestions.add(CommandTypes.SHOW_COMMAND);

            if (sender.hasPermission(Permissions.PERM_TP))
                suggestions.add(CommandTypes.TP_COMMAND);
        }

        List<String> result = new ArrayList<String>();
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
                case CommandTypes.TP_COMMAND:
                    for (Goal goal: FWObbiettivi.getInstance().obbiettivi){
                        if(goal.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(goal.getName());
                    }
                    break;
            }

            return result;
        }

        if(args.length == 3){
            switch (args[0]){
                case CommandTypes.DISABLE_COMMAND:
                case CommandTypes.ENABLE_COMMAND:
                case CommandTypes.TP_COMMAND:
                    Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
                    TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
                    for (int i = 0; i < towns.length; i++) {
                        if (towns[i].getName().toLowerCase().startsWith(args[2].toLowerCase()))
                            result.add(towns[i].getName());
                    }
                    break;
            }

            return result;
        }

        return null;
    }

}
