package it.forgottenworld.fwobbiettivi.objects.managers;

import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.FWLocation;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.metadata.FixedMetadataValue;

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
        List<Chunk> chunks = GoalAreaManager.getChunksFromTownTes(tes);
        if (chunks.isEmpty()) {
            FWObbiettivi.info(Messages.CHUNK_NOT_FOUND);
            return;
        }

        for (Chunk c : chunks){
            // Rename
            TownyUtil.renamePlot(new Location(tes.getLocationChestRight().getWorld(), (c.getX() * 16), 64, (c.getZ() * 16)), tes.getName(), true);
            // Remove chunk
            GoalAreaManager.removeChunkTes(c, tes);
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
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        database.getFile().set("treasuries", null);

        for(Treasury tes: treasuries) {
            String path = "treasuries." + tes.getTown().getName();
            database.getFile().set(path + ".r-chest", FWLocation.getStringFromLocation(tes.getLocationChestRight()));
            database.getFile().set(path + ".l-chest", FWLocation.getStringFromLocation(tes.getLocationChestLeft()));
        }
        database.saveFile();
    }

    /**
     *
     */
    public static void load(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        treasuries.clear();

        if (database.getFile().getConfigurationSection("treasuries") != null) {
            for (String treasury : database.getFile().getConfigurationSection("treasuries").getKeys(false)) {
                String path = "treasuries." + treasury;
                Treasury tes = new Treasury(ConfigUtil.getTreasuryName(), TownyUtil.getTownFromString(treasury), FWLocation.getLocationFromString(database.getFile().getString(path + ".r-chest")), FWLocation.getLocationFromString(database.getFile().getString(path + ".l-chest")), ConfigUtil.getTreasuryNumPlot());

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
    }

}
