package it.forgottenworld.fwobbiettivi.utility;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;

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

    public static ArrayList<TownGoals> loadGoalsInTownList(){
        ArrayList<TownGoals> townGoals = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("plugins/FWObbiettivi/townGoals.markus");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            FWObbiettivi.info(sb.toString());
            inputStream.close();

            List<String> file = Arrays.asList(sb.toString().split("\\|"));

            for (String s: file){
                String[] valueString = s.split("\\*");
                TownGoals tg = new TownGoals();
                System.out.println(valueString[0]);
                System.out.println(UUID.fromString(valueString[0]).toString());
                TownyDataSource tds = TownyUniverse.getInstance().getDataSource();
                System.out.println(tds);
                tg.setTown(tds.getTown(UUID.fromString(valueString[0])));

                for(Goal g:FWObbiettivi.instance.obbiettivi){
                    if(g.getName().equals(valueString[1])){
                        tg.setGoal(g);
                        break;
                    }
                }

                tg.setLocation(FWLocation.getLocationFromString(valueString[2]));

                Block b = tg.getLocation().getBlock();
                ((Chest) b.getState()).setCustomName("FWChest");
                b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.instance, Boolean.TRUE));

                townGoals.add(tg);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return townGoals;
    }

    public static void saveGoalsInTownList(ArrayList<TownGoals> townGoals) {
        StringBuilder sb = new StringBuilder();
        for(TownGoals tg: townGoals){
            sb.append(tg.getTown().getUuid().toString()).append("*");
            sb.append(tg.getGoal().getName()).append("*");
            sb.append(FWLocation.getStringFromLocation(tg.getLocation())).append("|");
        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        try {
            FWObbiettivi.info(sb.toString());
            FileWriter writer = new FileWriter("plugins/FWObbiettivi/townGoals.markus");
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
