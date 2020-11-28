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

public class Treasuries {

    static ArrayList<Treasury> treasuries = new ArrayList<>();

    /**
     *
     * @param tes
     */
    public static void addTreasury(Treasury tes) {
        if (!containsTreasury(tes.getTown())) {
            treasuries.add(tes);

            Chest chestStateRight = (Chest) tes.getLocationChestRight().getBlock().getState();
            chestStateRight.setCustomName("FWChest");
            tes.getLocationChestRight().getBlock().setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

            Chest chestStateLeft = (Chest) tes.getLocationChestLeft().getBlock().getState();
            chestStateLeft.setCustomName("FWChest");
            tes.getLocationChestLeft().getBlock().setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

            save();
        }
    }

    /**
     *
     * @param locationRight
     * @param locationLeft
     * @return
     */
    public static boolean isSameChunk(Location locationRight, Location locationLeft){
        return (locationRight.getChunk().getX() == locationLeft.getChunk().getX()) && (locationRight.getChunk().getZ() == locationLeft.getChunk().getZ());
    }

    /**
     *
     * @param tes
     */
    public static void removeTreasury(Treasury tes) {
        HashMap<Pair<Integer, Integer>, Treasury> app = GoalAreaManager.getChunksFromTownTes(tes);
        if (app.isEmpty()) {
            // todo message
            FWObbiettivi.info("inpossibile trovare chunk");
            return;
        }

        Iterator<Map.Entry<Pair<Integer, Integer>, Treasury>> it = app.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Pair<Integer, Integer>, Treasury> entry = it.next();
            if (tes.equals(entry.getValue())){
                // Rename
                TownyUtil.renamePlot(new Location(tes.getLocationChestRight().getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16)), "");
                // Remove chunk
                GoalAreaManager.removeChunkTes(new Location(tes.getLocationChestRight().getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16)));
                it.remove();
            }
        }

        treasuries.remove(tes);

        // Remove FWChest
        Chest chestStateRight = (Chest) tes.getLocationChestRight().getBlock().getState();
        chestStateRight.setCustomName("Chest");
        tes.getLocationChestRight().getBlock().removeMetadata("goalchest", FWObbiettivi.getInstance());

        Chest chestStateLeft = (Chest) tes.getLocationChestLeft().getBlock().getState();
        chestStateLeft.setCustomName("Chest");
        tes.getLocationChestLeft().getBlock().removeMetadata("goalchest", FWObbiettivi.getInstance());

        save();
    }

    /**
     *
     * @param town
     * @return
     */
    public static boolean containsTreasury(Town town) {
        for (Treasury t : treasuries)
            if (t.getTown().equals(town))
                return true;
        return false;
    }

    /**
     *
     * @param town
     * @return
     */
    public static Treasury getFromTown(Town town) {
        for (Treasury t : treasuries)
            if (t.getTown().equals(town))
                return t;
        return null;
    }

    /**
     * Returns a printable list of all objectives present
     */
    public static String getPrintableList() {
        StringBuilder sb = new StringBuilder();
        for (Treasury t : treasuries)
            sb.append("- ").append(t.getTown().getName()).append("\n");
        return sb.toString();
    }

    public static ArrayList<Treasury> getTreasuries(){
        return treasuries;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    /**
     *
     */
    public static void save(){
        StringBuilder sb = new StringBuilder();

        for(Treasury tes: treasuries){
            sb.append(tes.getName()).append("*");
            sb.append(tes.getTown().getUuid().toString()).append("*");
            sb.append(FWLocation.getStringFromLocation(tes.getLocationChestRight())).append("*");
            sb.append(FWLocation.getStringFromLocation(tes.getLocationChestLeft())).append("*");
            sb.append(tes.getNumPlot());
            sb.append("|");
        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        try {
            FileWriter writer = new FileWriter("plugins/FWObbiettivi/treasuries.markus");
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

    //TODO

    /**
     *
     */
    public static void load(){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("plugins/FWObbiettivi/treasuries.markus");
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
                    Treasury tes = new Treasury(valueString[0], TownyUtil.getTownFromUUID(UUID.fromString(valueString[1])), FWLocation.getLocationFromString(valueString[2]), FWLocation.getLocationFromString(valueString[3]), Integer.parseInt(valueString[4]));

                    Block right = tes.getLocationChestRight().getBlock();
                    Chest chestStateRight = (Chest) right.getState();
                    chestStateRight.setCustomName("FWChest");
                    right.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

                    Block left = tes.getLocationChestLeft().getBlock();
                    Chest chestStateLeft = (Chest) left.getState();
                    chestStateLeft.setCustomName("FWChest");
                    left.setMetadata("goalchest", new FixedMetadataValue(FWObbiettivi.getInstance(), Boolean.TRUE));

                    treasuries.add(tes);
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
