package it.forgottenworld.fwobbiettivi.utility;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConfigUtil {

    public static final boolean DEBUG = FWObbiettivi.instance.getConfig().getBoolean("debug");

    public static List<String> getConfigStringListLang(String path){
        return FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + "." + path);
    }

    public static String getConfigStringLang(String path){
        return FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + "." + path);
    }

    public static String getConfLang(){
        return FWObbiettivi.instance.getConfig().getString("languages.default");
    }

    public static ArrayList<Goal> loadGoalsList(){
        ArrayList<Goal> obbiettivi = new ArrayList<Goal>();
        ConfigurationSection sec = FWObbiettivi.instance.getConfig().getConfigurationSection("goal");

        for (String goal: sec.getKeys(false)) {
            ArrayList<String> requiredGoal = new ArrayList<String>();
            requiredGoal.addAll(FWObbiettivi.instance.getConfig().getStringList("goal." + goal + ".requiredGoals"));

            // Check if the Branch exist
            Branch ramo = null;
            for (Iterator<Branch> it = FWObbiettivi.instance.rami.iterator(); it.hasNext(); ) {
                Branch branch = it.next();
                if (branch.getName().equals(FWObbiettivi.instance.getConfig().getString("goal." + goal + ".branch"))){
                    // Adding Goal to that Town
                    ramo = branch;
                } else {
                    FWObbiettivi.info(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + ChatFormatter.formatWarningMessage(FWObbiettivi.instance.getConfig().getString("goal." + goal + ".branch")));
                }
            }

            // Payment materials
            ArrayList<String> paymentsMaterial = new ArrayList<String>();
            paymentsMaterial.addAll(FWObbiettivi.instance.getConfig().getStringList("goal." + goal + ".payment"));
            if(DEBUG)
                FWObbiettivi.info("paymentsMaterial=" + paymentsMaterial.toString());
            ArrayList<Integer> paymentsQuantity = new ArrayList<Integer>();
            paymentsQuantity.addAll(FWObbiettivi.instance.getConfig().getIntegerList("goal." + goal + ".paymentQuantity"));
            if(DEBUG)
                FWObbiettivi.info("paymentsQuantity=" + paymentsQuantity.toString());
            ArrayList<ItemStack> payment = new ArrayList<ItemStack>();

            for (int i = 0; i < paymentsMaterial.size(); i++) {
                if(DEBUG)
                    FWObbiettivi.info("paymentsMaterial=" + paymentsMaterial.get(i).toString());
                payment.add(new ItemStack(Material.getMaterial(paymentsMaterial.get(i)),
                        paymentsQuantity.get(i)));
            }

            // Reward materials
            ArrayList<String> rewardsMaterial = new ArrayList<String>();
            rewardsMaterial.addAll(FWObbiettivi.instance.getConfig().getStringList("goal." + goal + ".reward"));
            ArrayList<Integer> rewardsQuantity = new ArrayList<Integer>();
            rewardsQuantity.addAll(FWObbiettivi.instance.getConfig().getIntegerList("goal." + goal + ".rewardQuantity"));
            ArrayList<ItemStack> reward = new ArrayList<ItemStack>();

            for (int i = 0; i < rewardsMaterial.size(); i++) {
                reward.add(new ItemStack(Material.getMaterial(rewardsMaterial.get(i)),
                        rewardsQuantity.get(i)));
            }

            // Description
            ArrayList<String> description = new ArrayList<String>();
            description.addAll(FWObbiettivi.instance.getConfig().getStringList("goal." + goal + ".description"));

            obbiettivi.add(new Goal(goal, ramo, requiredGoal, payment, reward, description));
        }
        
        return obbiettivi;
    }

    public static ArrayList<Branch> loadBranchesList(){
        ArrayList<Branch> rami = new ArrayList<Branch>();
        Set<String> nameBranch = FWObbiettivi.instance.getConfig().getConfigurationSection("branch").getKeys(false);

        for (String branch: nameBranch) {

            ItemStack icon = new ItemStack(Material.getMaterial(FWObbiettivi.instance.getConfig().getString("branch." + branch + ".material")));

            ArrayList<String> description = new ArrayList<String>();
            description.addAll(FWObbiettivi.instance.getConfig().getStringList("branch." + branch + ".description"));

            rami.add(new Branch(branch, icon, description));
        }

        return rami;
    }

}
