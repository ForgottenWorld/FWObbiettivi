package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Goals {

    static ArrayList<Goal> obbiettivi = new ArrayList<Goal>();

    public static void addGoal(Goal goal){
        if (containsGoal(goal))
            return;

        obbiettivi.add(goal);

        save();
    }

    public static void removeGoal(Goal goal){
        obbiettivi.remove(goal);

        save();
    }

    public static Goal getGoalFromString(String name){
        for (Goal g: obbiettivi){
            if (name.equals(g.getName()))
                return g;
        }
        return null;
    }

    public static boolean isGoal(String name){
        if (getGoalFromString(name) == null)
            return false;

        return getObbiettivi().contains(getGoalFromString(name));
    }

    public static ArrayList<Goal> getGoalsFromBranch(Branch branch){
        ArrayList<Goal> goals = new ArrayList<>();

        for (Goal g : getObbiettivi()) {
            if (g.getBranch().equals(branch)) {
                goals.add(g);
            }
        }

        return goals;
    }

    public static ArrayList<Goal> getObbiettivi() {
        return obbiettivi;
    }

    public static boolean containsGoal(Goal goal) {
        return getObbiettivi().contains(goal);
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save() {
        ConfigManager goals = FWObbiettivi.getInstance().getGoals();
        goals.getFile().set("goals", null);

        for (Goal goal: getObbiettivi()) {
            String path = "goals." + goal.getName();
            goals.getFile().set(path + ".branch", goal.getBranch().getName());
            goals.getFile().set(path + ".plot", goal.getNumPlot());
            goals.getFile().set(path + ".requiredGoals", goal.getRequiredGoals());
            if (goal.getRequiredZenar() > 0.0)
                goals.getFile().set(path + ".requiredZenar", goal.getRequiredZenar());
            if (!goal.getRequiredObjects().isEmpty()) {
                goals.getFile().set(path + ".requiredObject", goal.getRequiredObjects().stream().map(p -> p.getType().toString()).collect(Collectors.toList()));
                goals.getFile().set(path + ".requiredQuantity", goal.getRequiredObjects().stream().map(ItemStack::getAmount).collect(Collectors.toList()));
            }
            if (!goal.getPayment().isEmpty()) {
                goals.getFile().set(path + ".payment", goal.getPayment().stream().map(p -> p.getType().toString()).collect(Collectors.toList()));
                goals.getFile().set(path + ".paymentQuantity", goal.getPayment().stream().map(ItemStack::getAmount).collect(Collectors.toList()));
            }
            if (!goal.getReward().isEmpty()) {
                goals.getFile().set(path + ".reward", goal.getReward().stream().map(p -> p.getType().toString()).collect(Collectors.toList()));
                goals.getFile().set(path + ".rewardQuantity", goal.getReward().stream().map(ItemStack::getAmount).collect(Collectors.toList()));
            }
            if (goal.getRewardZenar() > 0.0)
                goals.getFile().set(path + ".rewardZenar", goal.getRewardZenar());
            if (!goal.getRewardPermissions().isEmpty())
                goals.getFile().set(path + ".rewardPermissions", goal.getRewardPermissions());
            if (!goal.getRewardMultiplierPlugin().equals(""))
                goals.getFile().set(path + ".rewardMultiplierPlugin", goal.getRewardMultiplierPlugin());
            goals.getFile().set(path + ".description", goal.getDescrizione());
        }

        goals.saveFile();
    }

    public static void load() {
        obbiettivi.clear();
        ConfigurationSection sec = FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal");

        if (sec != null) {
            for (String goal : sec.getKeys(false)) {
                // Required Goals
                ArrayList<String> requiredGoal = new ArrayList<String>();
                requiredGoal.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".requiredGoals"));

                // Required Zenar
                double requiredZenar = 0.0;
                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("requiredZenar"))
                    requiredZenar = FWObbiettivi.getInstance().getConfig().getDouble("goal." + goal + ".requiredZenar");

                // Required Objects
                ArrayList<String> requiredObjectsMaterial = new ArrayList<String>();
                ArrayList<Integer> requiredObjectsQuantity = new ArrayList<Integer>();
                ArrayList<ItemStack> requiredObjects = new ArrayList<ItemStack>();

                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("requiredObject") &&
                        FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("requiredQuantity")) {
                    requiredObjectsMaterial.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".requiredObject"));
                    requiredObjectsQuantity.addAll(FWObbiettivi.getInstance().getConfig().getIntegerList("goal." + goal + ".requiredQuantity"));
                    for (int i = 0; i < requiredObjectsMaterial.size(); i++) {
                        requiredObjects.add(new ItemStack(Material.getMaterial(requiredObjectsMaterial.get(i)),
                                requiredObjectsQuantity.get(i)));
                    }
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
                ArrayList<Integer> paymentsQuantity = new ArrayList<Integer>();
                ArrayList<ItemStack> payment = new ArrayList<ItemStack>();

                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("payment") &&
                        FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("paymentQuantity")){
                    paymentsMaterial.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".payment"));
                    paymentsQuantity.addAll(FWObbiettivi.getInstance().getConfig().getIntegerList("goal." + goal + ".paymentQuantity"));
                    for (int i = 0; i < paymentsMaterial.size(); i++) {
                        payment.add(new ItemStack(Material.getMaterial(paymentsMaterial.get(i)),
                                paymentsQuantity.get(i)));
                    }
                }

                // Reward materials
                ArrayList<String> rewardsMaterial = new ArrayList<String>();
                ArrayList<Integer> rewardsQuantity = new ArrayList<Integer>();
                ArrayList<ItemStack> reward = new ArrayList<ItemStack>();

                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("reward") &&
                        FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("rewardQuantity")){
                    rewardsMaterial.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".reward"));
                    rewardsQuantity.addAll(FWObbiettivi.getInstance().getConfig().getIntegerList("goal." + goal + ".rewardQuantity"));

                    for (int i = 0; i < rewardsMaterial.size(); i++) {
                        reward.add(new ItemStack(Material.getMaterial(rewardsMaterial.get(i)),
                                rewardsQuantity.get(i)));
                    }
                }

                // Reward Zenar
                double rewardZenar = 0.0;
                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("rewardZenar"))
                    rewardZenar = FWObbiettivi.getInstance().getConfig().getDouble("goal." + goal + ".rewardZenar");

                // Reward Permissions
                ArrayList<String> rewardPermissions = new ArrayList<String>();
                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("rewardPermissions"))
                    rewardPermissions.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".rewardPermissions"));

                // Reward Multiplier Plugin Name
                String rewardMultiplierPlugin = "";
                if (FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal." + goal).getKeys(false).contains("rewardMultiplierPlugin"))
                    rewardMultiplierPlugin = FWObbiettivi.getInstance().getConfig().getString("goal." + goal + ".rewardMultiplierPlugin");

                // Description
                ArrayList<String> description = new ArrayList<String>();
                description.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".description"));

                obbiettivi.add(new Goal(goal, ramo, numPlot, requiredGoal, requiredZenar, requiredObjects, payment, reward, rewardZenar, rewardPermissions, rewardMultiplierPlugin, description));
            }
        }
    }
}
