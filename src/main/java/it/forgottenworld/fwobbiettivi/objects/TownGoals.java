package it.forgottenworld.fwobbiettivi.objects;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.FWLocation;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.*;
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
        HashMap<Pair<Integer, Integer>, TownGoal> app = GoalAreaManager.getChunksFromTownGoal(tg);
        if (app.isEmpty()) {
            // todo message
            FWObbiettivi.info("inpossibile trovare chunk");
            return;
        }

        Iterator<Map.Entry<Pair<Integer, Integer>, TownGoal>> it = app.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Pair<Integer, Integer>, TownGoal> entry = it.next();
            if (tg.equals(entry.getValue())){
                // Rename
                TownyUtil.renamePlot(new Location(tg.getLocation().getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16)), "");
                // Remove chunk
                GoalAreaManager.removeChunk(new Location(tg.getLocation().getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16)));
                it.remove();
            }
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
        ArrayList<Goal> goals = null;
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
        ArrayList<Town> towns = null;
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
        StringBuilder sb = new StringBuilder();

        for(TownGoal tg: obbiettiviInTown){
            sb.append(tg.getTown().getUuid().toString()).append("*");
            sb.append(tg.getGoal().getName()).append("*");
            sb.append(FWLocation.getStringFromLocation(tg.getLocation())).append("*");
            sb.append(tg.isActive());
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

    public static void load(){
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
                    TownGoal tg = new TownGoal();
                    tg.setTown(TownyUtil.getTownFromUUID(UUID.fromString(valueString[0])));

                    tg.setGoal(Goals.getGoalFromString(valueString[1]));

                    tg.setLocation(FWLocation.getLocationFromString(valueString[2]));

                    tg.setActive(Boolean.valueOf(valueString[3]));

                    Block b = tg.getLocation().getBlock();
                    Chest chestState = (Chest) b.getState();
                    chestState.setCustomName("FWChest");
                    b.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

                    obbiettiviInTown.add(tg);
                }
            }

        } catch (FileNotFoundException e) {
            FWObbiettivi.info(Messages.NO_EXISTING_FILE_DATA);
        } catch (IOException e) {
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
    }

}
