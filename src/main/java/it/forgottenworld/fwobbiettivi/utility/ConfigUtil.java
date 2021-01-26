package it.forgottenworld.fwobbiettivi.utility;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;

import java.util.*;

public class ConfigUtil {

    public static final boolean DEBUG = FWObbiettivi.getInstance().getConfig().getBoolean("debug");
    public static final String LANG = FWObbiettivi.getInstance().getConfig().getString("languages.default");
    public static final boolean CHUNK = FWObbiettivi.getInstance().getConfig().getBoolean("chunk.protection");
    public static final boolean MULTI_GOAL = FWObbiettivi.getInstance().getConfig().getBoolean("chunk.multiGoal");
    public static final int MAX_GOAL_IN_CHUNK = FWObbiettivi.getInstance().getConfig().getInt("chunk.max");
    public static final String TREASURY = FWObbiettivi.getInstance().getConfig().getString("treasury.name");

    /**
     * LANG
     */
    public static List<String> getConfigStringListLang(String path){
        return FWObbiettivi.getInstance().getConfig().getStringList("languages." + LANG + "." + path);
    }

    public static String getConfigStringLang(String path){
        return FWObbiettivi.getInstance().getConfig().getString("languages." + LANG + "." + path);
    }

    /**
     * List of blocks allowed in a chunk of an active Goal.
     */
    public static List<String> getConfigStringListAllowedBlocks(){
        return FWObbiettivi.getInstance().getConfig().getStringList("allowedBlocks");
    }

    /**
     * TREASURY
     */
    public static String getTreasuryName(){
        return FWObbiettivi.getInstance().getConfig().getString("treasury.name");
    }

    public static int getTreasuryNumPlot(){
        return FWObbiettivi.getInstance().getConfig().getInt("treasury.plot");
    }

    /**
     * WORLD
     */
    public static String getWorldName() {
        return FWObbiettivi.getInstance().getConfig().getString("world");
    }
}
