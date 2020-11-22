package it.forgottenworld.fwobbiettivi;

import it.forgottenworld.fwobbiettivi.command.GoalsCommandExecutor;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.listeners.*;
import it.forgottenworld.fwobbiettivi.listeners.area.GoalAreaCreationListener;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public final class FWObbiettivi extends JavaPlugin {

    private static FWObbiettivi instance;

    static FileConfiguration defaultConfig;
    public static ArrayList<Branch> rami = new ArrayList<Branch>();
    public static ArrayList<Goal> obbiettivi = new ArrayList<Goal>();
    public static ArrayList<TownGoals> obbiettiviInTown = new ArrayList<TownGoals>();
    public static HashMap<Pair<Integer, Integer>, TownGoals> chunks = new HashMap<Pair<Integer, Integer>, TownGoals>();
    public GoalsGUI gui = new GoalsGUI();
    public HashMap<Player, GoalsGUI> map = new HashMap<>();

    public static FWObbiettivi getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        info("Enabled!");
        instance = this;

        info("Loading configuration...");
        loadConfiguration();

        // Registrazione CommandExecutor
        info("Registering commands...");
        this.getCommand("obbiettivo").setExecutor(new GoalsCommandExecutor());
        this.getCommand("ob").setExecutor(new GoalsCommandExecutor());
        this.getCommand("goals").setExecutor(new GoalsCommandExecutor());

        // Registrazione event-listeners
        info("Registering listeners...");
        this.getServer().getPluginManager().registerEvents(new DisbandTownListener(), this);
        this.getServer().getPluginManager().registerEvents(new GUIGoalsListener(), this);
        this.getServer().getPluginManager().registerEvents(new GoalsChestListener(), this);
        this.getServer().getPluginManager().registerEvents(new GoalsChunkListener(), this);
        this.getServer().getPluginManager().registerEvents(new GoalAreaCreationListener(), this);
        this.getServer().getPluginManager().registerEvents(new MantenimentoBonusListener(), this);

        // Caricamento da file
        info("Loading infos...");
        loadData();
        info("Data loaded");
    }

    @Override
    public void onDisable() {
        // Salvataggio su file
        info("Saving infos...");
        saveData();
        info("Data Saved");

        info("Disabled");
    }

    // Metodi statici per semplificare il logging
    public static void info(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"[FWObbiettivi] " + msg);
    }

    public static void debug(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"[FWObbiettivi] [Debug]" + msg);
    }

    // Metodi per il salvataggio dei dati su file e per il loro caricamento
    public static void loadData() {
        info("Loading Branch");
        rami = ConfigUtil.loadBranchesList();

        info("Loading Goals");
        obbiettivi = ConfigUtil.loadGoalsList();

        info("Loading Data");
        obbiettiviInTown = ConfigUtil.loadGoalsInTownList();
        chunks = ConfigUtil.loadChunkList();
        info("Finish loading saves");
    }

    public static void saveData() {
        info("Saving Data");
        ConfigUtil.saveGoalsInTownList(obbiettiviInTown);
        ConfigUtil.saveChunkList(chunks);
        info("Finish saving saves");
    }

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        defaultConfig = getConfig();
    }

    public static FileConfiguration getDefaultConfig() {
        return defaultConfig;
    }
}
