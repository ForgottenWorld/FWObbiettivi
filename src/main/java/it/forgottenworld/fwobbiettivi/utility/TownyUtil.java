package it.forgottenworld.fwobbiettivi.utility;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Location;

import java.util.*;

public class TownyUtil {

    /**
     * Check if a Location is in a Town
     * @param location A Location
     * @return The validity of the control
     */
    public static boolean isInTown(Location location){
        return TownyUniverse.getInstance().hasTownBlock(WorldCoord.parseWorldCoord(location));
    }

    /**
     * Returns a Town from a Location
     * @param location A Location
     * @return The Town or null
     */
    public static Town getTownFromLocation(Location location){
        try {
            return TownyUniverse.getInstance().getTownBlock(WorldCoord.parseWorldCoord(location)).getTown();
        } catch (NotRegisteredException e) {}
        return null;
    }

    /**
     *
     * @param name
     * @return
     */
    public static Town getTownFromString(String name) {
        try {
            return TownyAPI.getInstance().getDataSource().getTown(name);
        } catch (NotRegisteredException e) {}
        return null;
    }

    /**
     *
     * @param uuid
     * @return
     */
    public static Town getTownFromUUID(UUID uuid) {
        try {
            return TownyAPI.getInstance().getDataSource().getTown(uuid);
        } catch (NotRegisteredException e) {}
        return null;
    }

    /**
     * Method for renaming the plot of a goal
     * @param location A Location
     * @param name The name for the plot
     * @param state A boolean indicating whether the chunk is to be added or removed
     */
    public static void renamePlot(Location location, String name, boolean state){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(WorldCoord.parseWorldCoord(location).getTownBlock().getName());
            if (state) {
                // Removing name from chunk
                if (sb.length() != 0) {
                    List<String> goalNames = Arrays.asList(sb.toString().split("\\~"));
                    sb.setLength(0);

                    for (String s : goalNames){
                        if (!s.equals(name))
                            sb.append(s).append("~");
                    }

                    if (sb.length() > 0)
                        sb.setLength(sb.length() - 1);
                }
            } else {
                // Adding name to the chunk
                if (!sb.toString().equals(""))
                    sb.append("~");
                sb.append(name);
            }
            // Rename plot
            WorldCoord.parseWorldCoord(location).getTownBlock().setName(sb.toString());
            // Saving new plot name
            TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(location).getTownBlock());
        } catch (NotRegisteredException ignored) {}
    }

}
