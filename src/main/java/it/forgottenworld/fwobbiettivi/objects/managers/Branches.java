package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class Branches {

    static ArrayList<Branch> rami = new ArrayList<Branch>();

    public static void addBranch(Branch branch){
        if (containsBranch(branch))
            return;

        rami.add(branch);

        save();
    }

    public static void removeBranch(Branch branch){
        rami.remove(branch);

        save();
    }

    public static boolean containsBranch(Branch branch) {
        return getRami().contains(branch);
    }

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
        ConfigManager branches = FWObbiettivi.getInstance().getBranches();
        branches.getFile().set("branches", null);

        for (Branch branch: getRami()) {
            String path = "branches." + branch.getName();
            branches.getFile().set(path + ".material", branch.getIcon().getType().toString());
            branches.getFile().set(path + ".description", branch.getDescrizione());
        }

        branches.saveFile();
    }

    public static void load() {
//        rami.clear();
//        Set<String> nameBranch = FWObbiettivi.getInstance().getConfig().getConfigurationSection("branch").getKeys(false);
//
//        for (String branch: nameBranch) {
//
//            ItemStack icon = new ItemStack(Material.getMaterial(FWObbiettivi.getInstance().getConfig().getString("branch." + branch + ".material")));
//
//            ArrayList<String> description = new ArrayList<String>();
//            description.addAll(FWObbiettivi.getInstance().getConfig().getStringList("branch." + branch + ".description"));
//
//            rami.add(new Branch(branch, icon, description));
//        }

        ConfigManager branches = FWObbiettivi.getInstance().getBranches();
        rami.clear();

        if (branches.getFile().getConfigurationSection("branches") != null) {
            for (String branch : branches.getFile().getConfigurationSection("branches").getKeys(false)) {
                String path = "branches." + branch;
                Branch b = new Branch(branch, new ItemStack(Material.getMaterial(branches.getFile().getString(path + ".material"))), branches.getFile().getStringList(path + ".description"));
                rami.add(b);
            }
        }

    }
}
