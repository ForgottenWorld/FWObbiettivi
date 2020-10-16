package it.forgottenworld.fwobbiettivi.command;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.WorldCoord;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
                    if(!playerAdd.hasPermission(Permissions.PERM_ADD)){
                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                        return true;
                    }

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
                        if(ConfigUtil.DEBUG)
                            FWObbiettivi.debug(FWObbiettivi.instance.obbiettivi.toString());

                        boolean founded = false;
                        for (Iterator<Goal> it = FWObbiettivi.instance.obbiettivi.iterator(); it.hasNext(); ) {
                            Goal goal = it.next();
                            if (goal.getName().equals(args[1])){
                                founded = true;
                                // Check if the Goal is alredy present in Town
                                for (TownGoals tg: FWObbiettivi.instance.obbiettiviInTown) {
                                    if (tg.getTown().equals(town) && tg.getGoal().equals(goal)){
                                        playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_ALREADY_PRESENT));
                                        return true;
                                    }
                                }

                                // Adding Goal to that Town
                                FWObbiettivi.instance.obbiettiviInTown.add(new TownGoals(town, goal, loc));
                                if(ConfigUtil.DEBUG)
                                    FWObbiettivi.debug(FWObbiettivi.instance.obbiettiviInTown.toString());

                                // Rename plot to Goal name
                                try {
                                    WorldCoord.parseWorldCoord(playerAdd.getLocation()).getTownBlock().setName(goal.getName());
                                    // Saving new plot name
                                    TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(playerAdd.getLocation()).getTownBlock());
                                } catch (NotRegisteredException e) {

                                }
                                Chest chestState = (Chest) b.getState();
                                chestState.setCustomName("FWChest");
                                b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.instance, Boolean.TRUE));
                                playerAdd.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_ADDED) + " " + ChatColor.GOLD + args[1]);

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

                case CommandTypes.EDIT_COMMAND:
                    // TODO Edit a Goal
                    break;

                case CommandTypes.GUI_COMMAND:
                    // Check if the sender is a player
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerGui = (Player) sender;
                    // Do you have the permissions?
                    if(!playerGui.hasPermission(Permissions.PERM_GUI)){
                        playerGui.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                        return true;
                    }

                    FWObbiettivi.instance.map.put(playerGui, new GoalsGUI());
                    FWObbiettivi.instance.map.get(playerGui).setPlayer(playerGui);
                    FWObbiettivi.instance.map.get(playerGui).openGUI(GUIUtil.GOALS_STEP);
                    return true;

                case CommandTypes.HELP_COMMAND:
                    // Send list of available commands
                    sender.sendMessage(ChatFormatter.helpMessage());
                    return true;

                case CommandTypes.MOVE_COMMAND:
                    // TODO Move Goal
                    break;

                case CommandTypes.PAY_COMMAND:
                    // TODO Pay all Goals
                    return true;

                case CommandTypes.RELOAD_COMMAND:
                    // Reload the server config.yml
                    // Do you have the permissions?
                    if(!sender.hasPermission(Permissions.PERM_RELOAD)){
                        sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                        return true;
                    }

                    FWObbiettivi.info("Saving infos...");
                    FWObbiettivi.saveData();
                    FWObbiettivi.info("Data saved");

                    FWObbiettivi.info("Loading infos...");
                    FWObbiettivi.loadData();
                    FWObbiettivi.info("Data loades");

                    FWObbiettivi.instance.reloadConfig();
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
                    if(!playerRemove.hasPermission(Permissions.PERM_REMOVE)){
                        playerRemove.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                        return true;
                    }

                    // Get Goal from Location
                    Location locRemove = playerRemove.getLocation();

                    // Check if the location is in a Town
                    Town townRemove = null;

                    // TODO Fixing Internal server error
                    // Check if the Goal exist in this plot
                    if(ConfigUtil.DEBUG)
                        FWObbiettivi.debug(FWObbiettivi.instance.obbiettiviInTown.toString());

                    for (TownGoals townGoalsRemove:FWObbiettivi.instance.obbiettiviInTown){
                        try {
                            if(WorldCoord.parseWorldCoord(locRemove).getTownBlock() == WorldCoord.parseWorldCoord(townGoalsRemove.getLocation()).getTownBlock()){
                                // Rename plot to Goal name
                                try {
                                    WorldCoord.parseWorldCoord(playerRemove.getLocation()).getTownBlock().setName("");
                                    // Saving new plot name
                                    TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(playerRemove.getLocation()).getTownBlock());
                                } catch (NotRegisteredException e) {

                                }
                                Block bRemove = townGoalsRemove.getLocation().getBlock();
                                ((Chest) bRemove.getState()).setCustomName("FWChest");
                                bRemove.removeMetadata("goalchest", FWObbiettivi.instance);
                                playerRemove.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_REMOVED));

                                // Removing Goal to that Town
                                FWObbiettivi.instance.obbiettiviInTown.remove(townGoalsRemove);

                                // Saving
                                FWObbiettivi.saveData();
                                break;
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

                case CommandTypes.TP_COMMAND:
                    // TODO Teleport to a Goal
                    // Teleport a player to a goal
                    if (!(sender instanceof Player)){
                        sender.sendMessage(Messages.NO_CONSOLE);
                        return true;
                    }

                    Player playerTp = (Player) sender;

                    // Do you have the permissions?
                    if(!playerTp.hasPermission(Permissions.PERM_TP)){
                        playerTp.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                        return true;
                    }

                    Location locationTp = null;

                    if(args.length > 2) {
                        for (TownGoals townGoalsTp : FWObbiettivi.instance.obbiettiviInTown) {
                            if (args[1].equals(townGoalsTp.getGoal().getName()) && args[2].equals(townGoalsTp.getTown().getName())) {
                                locationTp = townGoalsTp.getLocation();
                                playerTp.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TELEPORTED_TO) + " " + ChatColor.GOLD + townGoalsTp.getGoal().getName());
                                playerTp.teleport(locationTp);
                                // Teleport success
                                return true;
                            }
                        }
                    } else {
                        playerTp.sendMessage(ChatFormatter.formatErrorMessage(Messages.TELEPORTED_MISSING_INFO));
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
        String argsIndex = "";

        if(suggestions.isEmpty()) {
            if (sender.hasPermission(Permissions.PERM_ADD))
                suggestions.add(CommandTypes.ADD_COMMAND);

            if (sender.hasPermission(Permissions.PERM_CREATE))
                suggestions.add(CommandTypes.CREATE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_DELETE))
                suggestions.add(CommandTypes.DELETE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_EDIT))
                suggestions.add(CommandTypes.EDIT_COMMAND);

            if (sender.hasPermission(Permissions.PERM_GUI))
                suggestions.add(CommandTypes.GUI_COMMAND);

            suggestions.add(CommandTypes.HELP_COMMAND);

            if (sender.hasPermission(Permissions.PERM_MOVE))
                suggestions.add(CommandTypes.MOVE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_PAY))
                suggestions.add(CommandTypes.PAY_COMMAND);

            if (sender.hasPermission(Permissions.PERM_RELOAD))
                suggestions.add(CommandTypes.RELOAD_COMMAND);

            if (sender.hasPermission(Permissions.PERM_REMOVE))
                suggestions.add(CommandTypes.REMOVE_COMMAND);

            if (sender.hasPermission(Permissions.PERM_TP))
                suggestions.add(CommandTypes.TP_COMMAND);
        }

        if(args.length == 1){
            List<String> result = new ArrayList<String>();
            for (String a : suggestions){
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }

            return result;
        }

        if(args.length == 2){
            List<String> result = new ArrayList<String>();
            switch (args[1]){
                // TODO Goals suggestion
                case CommandTypes.ADD_COMMAND:
                case CommandTypes.TP_COMMAND:
                    for (Goal goal: FWObbiettivi.instance.obbiettivi){
                        result.add(goal.getName());
                    }
                    break;
            }

            return result;
        }

        if(args.length == 3){
            List<String> result = new ArrayList<String>();
            switch (args[2]){
                case CommandTypes.TP_COMMAND:
                    Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
                    TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
                    for (int i = 0; i < towns.length; i++) {
                        if (towns[i].getName().toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(towns[i].getName());
                    }
                    break;
            }

            return result;
        }

        return null;
    }

}
