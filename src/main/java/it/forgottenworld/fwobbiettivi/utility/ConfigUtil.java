package it.forgottenworld.fwobbiettivi.utility;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import javafx.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.*;
import java.util.*;

public class ConfigUtil {

    public static final boolean DEBUG = FWObbiettivi.getInstance().getConfig().getBoolean("debug");

    public static List<String> getConfigStringListLang(String path){
        return FWObbiettivi.getInstance().getConfig().getStringList("languages." + getConfLang() + "." + path);
    }

    public static String getConfigStringLang(String path){
        return FWObbiettivi.getInstance().getConfig().getString("languages." + getConfLang() + "." + path);
    }

    public static String getConfLang(){
        return FWObbiettivi.getInstance().getConfig().getString("languages.default");
    }

    public static ArrayList<Goal> loadGoalsList(){
        ArrayList<Goal> obbiettivi = new ArrayList<Goal>();
        ConfigurationSection sec = FWObbiettivi.getInstance().getConfig().getConfigurationSection("goal");

        for (String goal: sec.getKeys(false)) {
            ArrayList<String> requiredGoal = new ArrayList<String>();
            requiredGoal.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".requiredGoals"));

            // Check if the Branch exist
            Branch ramo = null;
            for (Iterator<Branch> it = FWObbiettivi.getInstance().rami.iterator(); it.hasNext(); ) {
                Branch branch = it.next();
                if (branch.getName().equals(FWObbiettivi.getInstance().getConfig().getString("goal." + goal + ".branch"))){
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

            for (int i = 0; i < rewardsMaterial.size(); i++) {
                reward.add(new ItemStack(Material.getMaterial(rewardsMaterial.get(i)),
                        rewardsQuantity.get(i)));
            }

            // Description
            ArrayList<String> description = new ArrayList<String>();
            description.addAll(FWObbiettivi.getInstance().getConfig().getStringList("goal." + goal + ".description"));

            obbiettivi.add(new Goal(goal, ramo, numPlot, requiredGoal, payment, reward, description));
        }
        
        return obbiettivi;
    }

    public static ArrayList<Branch> loadBranchesList(){
        ArrayList<Branch> rami = new ArrayList<Branch>();
        Set<String> nameBranch = FWObbiettivi.getInstance().getConfig().getConfigurationSection("branch").getKeys(false);

        for (String branch: nameBranch) {

            ItemStack icon = new ItemStack(Material.getMaterial(FWObbiettivi.getInstance().getConfig().getString("branch." + branch + ".material")));

            ArrayList<String> description = new ArrayList<String>();
            description.addAll(FWObbiettivi.getInstance().getConfig().getStringList("branch." + branch + ".description"));

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

            inputStream.close();

            if(sb.length() != 0){
                List<String> file = Arrays.asList(sb.toString().split("\\|"));

                for (String s: file){
                    String[] valueString = s.split("\\*");
                    TownGoals tg = new TownGoals();
                    TownyDataSource tds = TownyUniverse.getInstance().getDataSource();
                    tg.setTown(tds.getTown(UUID.fromString(valueString[0])));

                    for(Goal g:FWObbiettivi.getInstance().obbiettivi){
                        if(g.getName().equals(valueString[1])){
                            tg.setGoal(g);
                            break;
                        }
                    }

                    tg.setLocation(FWLocation.getLocationFromString(valueString[2]));

                    tg.setActive(Boolean.valueOf(valueString[3]));

                    Block b = tg.getLocation().getBlock();
                    Chest chestState = (Chest) b.getState();
                    chestState.setCustomName("FWChest");
                    b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

                    townGoals.add(tg);
                }
            }

        } catch (FileNotFoundException e) {
            FWObbiettivi.info(Messages.NO_EXISTING_FILE_DATA);
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

    public static HashMap<Pair<Integer, Integer>, TownGoals> loadChunkList(){
        HashMap<Pair<Integer, Integer>, TownGoals> chunks = new HashMap<>();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("plugins/FWObbiettivi/chunks.markus");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            inputStream.close();

            if(sb.length() != 0){
                List<String> file = Arrays.asList(sb.toString().split("\\|"));

                for (String s: file){
                    String[] valueString = s.split("\\*");
                    TownGoals tg = new TownGoals();
                    TownyDataSource tds = TownyUniverse.getInstance().getDataSource();
                    tg.setTown(tds.getTown(UUID.fromString(valueString[2])));

                    for(Goal g:FWObbiettivi.getInstance().obbiettivi){
                        if(g.getName().equals(valueString[3])){
                            tg.setGoal(g);
                            break;
                        }
                    }

                    tg.setLocation(FWLocation.getLocationFromString(valueString[4]));

                    tg.setActive(Boolean.valueOf(valueString[5]));

                    chunks.put(new Pair<>(Integer.valueOf(valueString[0]), Integer.valueOf(valueString[1])), tg);
                }
            }

        } catch (FileNotFoundException e) {
            FWObbiettivi.info(Messages.NO_EXISTING_FILE_DATA);
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

        return chunks;
    }

    public static void saveGoalsInTownList(ArrayList<TownGoals> townGoals) {
        StringBuilder sb = new StringBuilder();

        for(TownGoals tg: townGoals){
            generateTownGoalsString(sb, tg);
            sb.append("|");
        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        try {
            FileWriter writer = new FileWriter("plugins/FWObbiettivi/townGoals.markus");
            if(sb.length() == 0){
                writer.write("");
            }else {
                writer.write(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChunkList(HashMap<Pair<Integer, Integer>, TownGoals> chunks) {
        StringBuilder sb = new StringBuilder();

        for (Pair<Integer, Integer> pair: chunks.keySet()){
            TownGoals tg = chunks.get(pair);
            sb.append(pair.getKey()).append("*");
            sb.append(pair.getValue()).append("*");
            generateTownGoalsString(sb, tg);
            sb.append("|");
        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        try {
            FileWriter writer = new FileWriter("plugins/FWObbiettivi/chunks.markus");
            if(sb.length() == 0){
                writer.write("");
            }else {
                writer.write(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateTownGoalsString(StringBuilder sb, TownGoals tg){
        sb.append(tg.getTown().getUuid().toString()).append("*");
        sb.append(tg.getGoal().getName()).append("*");
        sb.append(FWLocation.getStringFromLocation(tg.getLocation())).append("*");
        sb.append(tg.isActive());
    }

}
