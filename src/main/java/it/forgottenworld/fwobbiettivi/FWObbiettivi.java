package it.forgottenworld.fwobbiettivi;

import it.forgottenworld.fwobbiettivi.command.GoalsCommandExecutor;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.listeners.DisbandTownListener;
import it.forgottenworld.fwobbiettivi.listeners.GUIGoalsListener;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public final class FWObbiettivi extends JavaPlugin {

    static FileConfiguration defaultConfig;
    public static FWObbiettivi instance;
    public ArrayList<Branch> rami = new ArrayList<Branch>();
    public ArrayList<Goal> obbiettivi = new ArrayList<Goal>();
    public ArrayList<TownGoals> obbiettiviInTown = new ArrayList<TownGoals>();
    public GoalsGUI gui = new GoalsGUI();
    public HashMap<Player, GoalsGUI> map = new HashMap<>();

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
        Bukkit.getLogger().log(Level.INFO,"["+"FWObbiettivi"+"] " + msg);
    }

    // Metodi per il salvataggio dei dati su file e per il loro caricamento
    public static void loadData() {
    }

    public static void saveData() {
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
