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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GoalsCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args.length){
            case 0:

                break;
            case 1:
                switch (args[0].toLowerCase()){
                    case CommandTypes.ADD_COMMAND:
                        // Adding a Goal to a existing Town
                        // Check if the sender is a player
                        if (!(sender instanceof Player)){
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }

                        Player playerAdd = (Player) sender;

                        // Save location
                        Location loc = playerAdd.getLocation();

                        // Check if the location is in a Town
                        Town town = null;
                        try {
                            // Try to get the town where the player is Standing
                            town = WorldCoord.parseWorldCoord(playerAdd.getLocation()).getTownBlock().getTown();
                        } catch (NotRegisteredException e) {
                            // Not in a town
                            e.printStackTrace();
                            playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        }

                        // Check if the Goal exist
                        boolean exist = false;
                        for (Iterator<Goal> it = FWObbiettivi.instance.obbiettivi.iterator(); it.hasNext(); ) {
                            Goal goal = it.next();
                            if (goal.getName() == args[1]){
                                exist = true;
                                // Adding Goal to that Town
                                FWObbiettivi.instance.obbiettiviInTown.add(new TownGoals(town, goal, loc));
                            } else {
                                playerAdd.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL) + ChatFormatter.formatWarningMessage(args[1]));
                                return true;
                            }
                        }
                        break;

                    case CommandTypes.CREATE_COMMAND:
                        // Create a new Goal
                        break;

                    case CommandTypes.DELETE_COMMAND:
                        // Delete a Goal
                        break;

                    case CommandTypes.EDIT_COMMAND:
                        // Edit a Goal
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

//                        FWObbiettivi.instance.gui.setSteps(new ArrayList<Integer>());
//                        FWObbiettivi.instance.gui.setPlayer(player);
//                        FWObbiettivi.instance.gui.openGUI(GUIUtil.GOALS_STEP);
                        FWObbiettivi.instance.map.put(playerGui, new GoalsGUI());
                        FWObbiettivi.instance.map.get(playerGui).setPlayer(playerGui);
                        FWObbiettivi.instance.map.get(playerGui).openGUI(GUIUtil.GOALS_STEP);
                        return true;

                    case CommandTypes.HELP_COMMAND:
                        // Send list of available commands
                        sender.sendMessage(ChatFormatter.helpMessage());
                        return true;

                    case CommandTypes.MOVE_COMMAND:
                        // Move Goal
                        break;

                    case CommandTypes.PAY_COMMAND:
                        // Pay all Goals
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
                        return true;

                    case CommandTypes.REMOVE_COMMAND:
                        // Remove a Goal from a Town
                        break;

                    case CommandTypes.TP_COMMAND:
                        // Teleport to a Goal
                        break;
                }
                break;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        String argsIndex = "";

        switch (args.length){
            case 1:
                if (sender.hasPermission(Permissions.PERM_GUI))
                    suggestions.add(CommandTypes.GUI_COMMAND);

                suggestions.add(CommandTypes.HELP_COMMAND);

                if (sender.hasPermission(Permissions.PERM_PAY))
                    suggestions.add(CommandTypes.PAY_COMMAND);

                if (sender.hasPermission(Permissions.PERM_RELOAD))
                    suggestions.add(CommandTypes.RELOAD_COMMAND);

                break;
            case 2:
                break;
        }

        return NameUtil.filterByStart(suggestions, argsIndex);
    }

}
