package it.forgottenworld.fwobbiettivi.objects.managers;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.utility.FWLocation;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class TownGoals {

    static ArrayList<TownGoal> obbiettiviInTown = new ArrayList<TownGoal>();

    /**
     *
     * @param tg
     */
    public static void addTownGoal(TownGoal tg) {
        if (!containsTownGoal(tg.getLocation())) {
            obbiettiviInTown.add(tg);
            Chest chestState = (Chest) tg.getLocation().getBlock().getState();
            chestState.setCustomName("FWChest");
            tg.getLocation().getBlock().setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));
        }

        save();
    }

    /**
     *
     * @param tg
     */
    public static void removeTownGoal(TownGoal tg){
        List<Chunk> chunks = GoalAreaManager.getChunksFromTownGoal(tg);
        if (chunks.isEmpty()) {
            FWObbiettivi.info(Messages.CHUNK_NOT_FOUND);
            return;
        }

        for (Chunk c : chunks){
            // Rename
            TownyUtil.renamePlot(new Location(tg.getLocation().getWorld(), (c.getX() * 16), 64, (c.getZ() * 16)), tg.getGoal().getName(), true);
            // Remove chunk
            GoalAreaManager.removeChunk(c, tg);
        }

        obbiettiviInTown.remove(tg);

        // Remove FWChest
        Chest chestState = (Chest) tg.getLocation().getBlock().getState();
        chestState.setCustomName("Chest");
        tg.getLocation().getBlock().removeMetadata("goalchest", FWObbiettivi.getInstance());

        save();
    }

    /**
     *
     * @param town
     * @return
     */
    public static boolean townHasGoal(Town town){
        for (TownGoal tg : obbiettiviInTown) {
            if (town.getUuid().equals(tg.getTown().getUuid()))
                return true;
        }
        return false;
    }

    /**
     *
     * @param town
     * @return
     */
    public static ArrayList<Goal> getGoalFromTown(Town town){
        ArrayList<Goal> goals = new ArrayList<>();
        for (TownGoal tg : obbiettiviInTown) {
            if (town.getUuid().equals(tg.getTown().getUuid()))
                goals.add(tg.getGoal());
        }
        return goals;
    }

    /**
     *
     * @param goal
     * @return
     */
    public static boolean goalHasTown(Goal goal){
        for (TownGoal tg : obbiettiviInTown) {
            if (goal.getName().equals(tg.getGoal().getName()))
                return true;
        }
        return false;
    }

    /**
     *
     * @param goal
     * @return
     */
    public static ArrayList<Town> getTownFromGoal(Goal goal){
        ArrayList<Town> towns = new ArrayList<>();
        for (TownGoal tg : obbiettiviInTown) {
            if (goal.getName().equals(tg.getGoal().getName()))
                towns.add(tg.getTown());
        }
        return towns;
    }

    /**
     *
     * @param goal
     * @param town
     * @return
     */
    public static TownGoal getTownGoalFromGoalAndTown(Goal goal, Town town){
        for (TownGoal tg : obbiettiviInTown) {
            if ((goal.equals(tg.getGoal())) && town.equals(tg.getTown()))
                return tg;
        }
        return null;
    }

    /**
     *
     * @param location
     * @return
     */
    public static boolean containsTownGoal(Location location) {
        for (TownGoal tg : obbiettiviInTown)
            if (tg.getLocation().equals(location))
                return true;
        return false;
    }

    /**
     *
     * @param goal
     * @param town
     * @return
     */
    public static boolean containsTownGoal(Goal goal, Town town) {
        for (TownGoal tg : obbiettiviInTown)
            if (tg.getGoal().equals(goal) && tg.getTown().equals(town))
                return true;
        return false;
    }

    /**
     *
     * @return
     */
    public static ArrayList<TownGoal> getObbiettiviInTown(){
        return obbiettiviInTown;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        database.getFile().set("towngoals", null);

        for(TownGoal tg: obbiettiviInTown) {
            String path = "towngoals." + tg.getTown().getName() + "." + tg.getGoal().getName();
            database.getFile().set(path + ".fw-chest", FWLocation.getStringFromLocation(tg.getLocation()));
            database.getFile().set(path + ".is-active", tg.isActive());
        }
        database.saveFile();

//        StringBuilder sb = new StringBuilder();
//
//        for(TownGoal tg: obbiettiviInTown){
//            sb.append(tg.getTown().getUuid().toString()).append("*");
//            sb.append(tg.getGoal().getName()).append("*");
//            sb.append(FWLocation.getStringFromLocation(tg.getLocation())).append("*");
//            sb.append(tg.isActive());
//            sb.append("|");
//        }
//
//        if (sb.length() > 0)
//            sb.setLength(sb.length() - 1);
//
//        try {
//            FileWriter writer = new FileWriter("plugins/FWObbiettivi/townGoals.markus");
//            if(sb.length() == 0){
//                writer.write("");
//            }else {
//                writer.write(sb.toString());
//            }
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void load(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        obbiettiviInTown.clear();

        if (database.getFile().getConfigurationSection("towngoals") != null) {
            for (String town : database.getFile().getConfigurationSection("towngoals").getKeys(false)) {
                for (String goal : database.getFile().getConfigurationSection("towngoals." + town).getKeys(false)) {
                    String path = "towngoals." + town + "." + goal;
                    TownGoal tg = new TownGoal();

                    tg.setTown(TownyUtil.getTownFromString(town));
                    tg.setGoal(Goals.getGoalFromString(goal));
                    tg.setLocation(FWLocation.getLocationFromString(database.getFile().getString(path + ".fw-chest")));
                    tg.setActive(database.getFile().getBoolean(path + ".is-active"));

                    Block b = tg.getLocation().getBlock();
                    Chest chestState = (Chest) b.getState();
                    chestState.setCustomName("FWChest");
                    b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

                    obbiettiviInTown.add(tg);
                }
            }
        }

//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream("plugins/FWObbiettivi/townGoals.markus");
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            StringBuilder sb = new StringBuilder();
//            while((line = bufferedReader.readLine()) != null){
//                sb.append(line);
//            }
//
//            inputStream.close();
//
//            if(sb.length() != 0){
//                List<String> file = Arrays.asList(sb.toString().split("\\|"));
//
//                for (String s: file){
//                    String[] valueString = s.split("\\*");
//                    TownGoal tg = new TownGoal();
//                    tg.setTown(TownyUtil.getTownFromUUID(UUID.fromString(valueString[0])));
//
//                    tg.setGoal(Goals.getGoalFromString(valueString[1]));
//
//                    tg.setLocation(FWLocation.getLocationFromString(valueString[2]));
//
//                    tg.setActive(Boolean.valueOf(valueString[3]));
//
//                    Block b = tg.getLocation().getBlock();
//                    Chest chestState = (Chest) b.getState();
//                    chestState.setCustomName("FWChest");
//                    b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));
//
//                    obbiettiviInTown.add(tg);
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            FWObbiettivi.info(Messages.NO_EXISTING_FILE_DATA);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null){
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}