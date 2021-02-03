package it.forgottenworld.fwobbiettivi.command.admin.goal;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
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
import me.architetto.fwfortress.fortress.FortressService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
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
        return "[obName] [town]";
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
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            // Pay all type of goal in all town
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
                        HashMap<Material, Integer> oldOutputAmount = new HashMap<>();

                        if (!townGoals.getGoal().getRewardMultiplierPlugin().equals("")) {
                            String multiplierPluginName = townGoals.getGoal().getRewardMultiplierPlugin();
                            switch (multiplierPluginName) {
                                case "FWFortress":
                                    int multiplier = FortressService.getInstance().getAmountOfFortressOwnedByTown(townGoals.getTown().getName());
                                    if (multiplier > 0) {
                                        for (ItemStack outIs : townGoals.getGoal().getReward()) {
                                            oldOutputAmount.put(outIs.getType(), outIs.getAmount());
                                            int amount = outIs.getAmount() * multiplier;
                                            outIs.setAmount(amount);
                                        }
                                    } else {
                                        FWObbiettivi.info(Messages.GOAL_NOT_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                                        return;
                                    }
                                    break;
                            }
                        }

                        townGoals.pay();

                        if (!townGoals.getGoal().getRewardMultiplierPlugin().equals("")) {
                            for (ItemStack outIs : townGoals.getGoal().getReward()) {
                                outIs.setAmount(oldOutputAmount.get(outIs.getType()));
                            }

                        }

                        FWObbiettivi.info(Messages.GOAL_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                    }
                } else {
                    FWObbiettivi.info(Messages.GOAL_IS_DISABLE + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                }
            }
        } else if (args.length == 2) {
            // Pay that goal in all town
            if (Goals.isGoal(args[1])){
                // List of Towns
                for (Town t : TownGoals.getTownFromGoal(Goals.getGoalFromString(args[1]))){
                    TownGoal townGoals = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), t);

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
                            HashMap<Material, Integer> oldOutputAmount = new HashMap<>();

                            if (!townGoals.getGoal().getRewardMultiplierPlugin().equals("")) {
                                String multiplierPluginName = townGoals.getGoal().getRewardMultiplierPlugin();
                                switch (multiplierPluginName) {
                                    case "FWFortress":
                                        int multiplier = FortressService.getInstance().getAmountOfFortressOwnedByTown(townGoals.getTown().getName());
                                        if (multiplier > 0) {
                                            for (ItemStack outIs : townGoals.getGoal().getReward()) {
                                                oldOutputAmount.put(outIs.getType(), outIs.getAmount());
                                                int amount = outIs.getAmount() * multiplier;
                                                outIs.setAmount(amount);
                                            }
                                        } else {
                                            FWObbiettivi.info(Messages.GOAL_NOT_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                                            return;
                                        }
                                        break;
                                }
                            }

                            townGoals.pay();

                            if (!townGoals.getGoal().getRewardMultiplierPlugin().equals("")) {
                                for (ItemStack outIs : townGoals.getGoal().getReward()) {
                                    outIs.setAmount(oldOutputAmount.get(outIs.getType()));
                                }

                            }

                            FWObbiettivi.info(Messages.GOAL_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                        }
                    } else {
                        FWObbiettivi.info(Messages.GOAL_IS_DISABLE + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                    }

                }
            } else {
                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + " " + args[1]);
            }

        } else if (args.length == 3) {
            // Pay that goal in that town
            if (Goals.isGoal(args[1]) && TownyUtil.isTown(args[2])){
                TownGoal townGoals = TownGoals.getTownGoalFromGoalAndTown(Goals.getGoalFromString(args[1]), TownyUtil.getTownFromString(args[2]));

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
                        HashMap<Material, Integer> oldOutputAmount = new HashMap<>();

                        if (!townGoals.getGoal().getRewardMultiplierPlugin().equals("")) {
                            String multiplierPluginName = townGoals.getGoal().getRewardMultiplierPlugin();
                            switch (multiplierPluginName) {
                                case "FWFortress":
                                    int multiplier = FortressService.getInstance().getAmountOfFortressOwnedByTown(townGoals.getTown().getName());
                                    if (multiplier > 0) {
                                        for (ItemStack outIs : townGoals.getGoal().getReward()) {
                                            oldOutputAmount.put(outIs.getType(), outIs.getAmount());
                                            int amount = outIs.getAmount() * multiplier;
                                            outIs.setAmount(amount);
                                        }
                                    } else {
                                        FWObbiettivi.info(Messages.GOAL_NOT_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                                        return;
                                    }
                                    break;
                            }
                        }

                        townGoals.pay();

                        if (!townGoals.getGoal().getRewardMultiplierPlugin().equals("")) {
                            for (ItemStack outIs : townGoals.getGoal().getReward()) {
                                outIs.setAmount(oldOutputAmount.get(outIs.getType()));
                            }

                        }

                        FWObbiettivi.info(Messages.GOAL_PAID + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                    }
                } else {
                    FWObbiettivi.info(Messages.GOAL_IS_DISABLE + " " + townGoals.getGoal().getName() + " - " + townGoals.getTown().getName());
                }
            } else {
                sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + " " + args[1]);
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
