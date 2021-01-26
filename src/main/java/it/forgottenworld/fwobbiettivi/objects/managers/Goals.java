package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;

public class Goals {

    static ArrayList<Goal> obbiettivi = new ArrayList<Goal>();

    public static Goal getGoalFromString(String name){
        for (Goal g: obbiettivi){
            if (name.equals(g.getName()))
                return g;
        }
        return null;
    }

    public static ArrayList<Goal> getObbiettivi() {
        return obbiettivi;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save() {
    }

    public static void load() {
        obbiettivi.clear();
        ConfigurationSection sec = FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal");

        if (sec != null) {
            for (String goal : sec.getKeys(false)) {
                ArrayList<String> requiredGoal = new ArrayList<String>();
                requiredGoal.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".requiredGoals"));

                // Required Zenar
                double requiredZenar = FWObbiettivi.getInstance().getConfig().getDouble("goal." + goal + ".requiredZenar");

                // Required Objects
                ArrayList<String> requiredObjectsMaterial = new ArrayList<String>();
                requiredObjectsMaterial.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".requiredObject"));

                ArrayList<Integer> requiredObjectsQuantity = new ArrayList<Integer>();
                requiredObjectsQuantity.addAll(FWObbiettivi.getInstance().getConfig().getIntegerList("goal." + goal + ".requiredQuantity"));

                ArrayList<ItemStack> requiredObjects = new ArrayList<ItemStack>();
                for (int i = 0; i < requiredObjectsMaterial.size(); i++) {
                    requiredObjects.add(new ItemStack(Material.getMaterial(requiredObjectsMaterial.get(i)),
                            requiredObjectsQuantity.get(i)));
                }

                // Check if the Branch exist
                Branch ramo = null;
                for (Iterator<Branch> it = Branches.getRami().iterator(); it.hasNext(); ) {
                    Branch branch = it.next();
                    if (branch.getName().equals(FWObbiettivi.getInstance().getConfig().getString("goal." + goal + ".branch"))) {
                        // Adding Goal to that Town
                        ramo = branch;
                    } else {
                        FWObbiettivi.info(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + ChatFormatter.formatWarningMessage(FWObbiettivi.getInstance().getConfig().getString("goal." + goal + ".branch")));
                    }
                }

                // Number of Plot
                int numPlot = FWObbiettivi.getInstance().getConfig().getInt("goal." + goal + ".plot");

                // Payment materials
                ArrayList<String> paymentsMaterial = new ArrayList<String>();
                paymentsMaterial.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".payment"));

                ArrayList<Integer> paymentsQuantity = new ArrayList<Integer>();
                paymentsQuantity.addAll(FWObbiettivi.getInstance().getConfig().getIntegerList("goal." + goal + ".paymentQuantity"));

                ArrayList<ItemStack> payment = new ArrayList<ItemStack>();

                for (int i = 0; i < paymentsMaterial.size(); i++) {
                    payment.add(new ItemStack(Material.getMaterial(paymentsMaterial.get(i)),
                            paymentsQuantity.get(i)));
                }

                // Reward materials
                ArrayList<String> rewardsMaterial = new ArrayList<String>();
                rewardsMaterial.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".reward"));
                ArrayList<Integer> rewardsQuantity = new ArrayList<Integer>();
                rewardsQuantity.addAll(FWObbiettivi.getInstance().getConfig().getIntegerList("goal." + goal + ".rewardQuantity"));
                ArrayList<ItemStack> reward = new ArrayList<ItemStack>();

                // Reward Zenar
                double rewardZenar = FWObbiettivi.getInstance().getConfig().getDouble("goal." + goal + ".rewardZenar");

                for (int i = 0; i < rewardsMaterial.size(); i++) {
                    reward.add(new ItemStack(Material.getMaterial(rewardsMaterial.get(i)),
                            rewardsQuantity.get(i)));
                }

                // Description
                ArrayList<String> description = new ArrayList<String>();
                description.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".description"));

                obbiettivi.add(new Goal(goal, ramo, numPlot, requiredGoal, requiredZenar, requiredObjects, payment, reward, rewardZenar, description));
            }
        }
    }
}
