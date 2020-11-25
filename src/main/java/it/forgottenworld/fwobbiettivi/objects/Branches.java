package it.forgottenworld.fwobbiettivi.objects;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class Branches {

    static ArrayList<Branch> rami = new ArrayList<Branch>();

    public static Branch getBranchFromString(String name){
        for (Branch b: rami){
            if (name.equals(b.getName()))
                return b;
        }
        return null;
    }

    public static ArrayList<Branch> getRami(){
        return rami;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save() {
    }

    public static void load() {
        Set<String> nameBranch = FWObbiettivi.getInstance().getConfig().getConfigurationSection("branch").getKeys(false);

        for (String branch: nameBranch) {

            ItemStack icon = new ItemStack(Material.getMaterial(FWObbiettivi.getInstance().getConfig().getString("branch." + branch + ".material")));

            ArrayList<String> description = new ArrayList<String>();
            description.addAll(FWObbiettivi.getInstance().getConfig().getStringList("branch." + branch + ".description"));

            rami.add(new Branch(branch, icon, description));
        }
    }
}
