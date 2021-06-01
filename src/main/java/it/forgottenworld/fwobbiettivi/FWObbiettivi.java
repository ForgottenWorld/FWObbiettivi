package it.forgottenworld.fwobbiettivi;

import it.forgottenworld.fwobbiettivi.command.BranchCommandExecutor;
import it.forgottenworld.fwobbiettivi.command.CommandExecutor;
import it.forgottenworld.fwobbiettivi.command.GoalsCommandExecutor;
import it.forgottenworld.fwobbiettivi.command.TreasuryCommandExecutor;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.listeners.*;
import it.forgottenworld.fwobbiettivi.listeners.GoalAreaListener;
import it.forgottenworld.fwobbiettivi.objects.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public final class FWObbiettivi extends JavaPlugin {

    private static FWObbiettivi instance;

    static FileConfiguration defaultConfig;
    private static ConfigManager database;
    private static ConfigManager branches;
    private static ConfigManager goals;
    public GoalsGUI gui = new GoalsGUI();
    public HashMap<UUID, GoalsGUI> map = new HashMap<>();

    public static FWObbiettivi getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        info("Enabled!");
        instance = this;

        info("Loading configuration...");
        loadConfiguration();

        // Caricamento da file
        info("Loading infos...");
        loadConfigManager();
        loadData();
        info("Data loaded");

        // Registrazione CommandExecutor
        info("Registering commands...");
        this.getCommand("fwobbiettivi").setExecutor(new CommandExecutor());

        this.getCommand("branch").setExecutor(new BranchCommandExecutor());
        this.getCommand("ramo").setExecutor(new BranchCommandExecutor());

        this.getCommand("goals").setExecutor(new GoalsCommandExecutor());
        this.getCommand("obbiettivo").setExecutor(new GoalsCommandExecutor());
        this.getCommand("ob").setExecutor(new GoalsCommandExecutor());

        this.getCommand("treasury").setExecutor(new TreasuryCommandExecutor());
        this.getCommand("tesoreria").setExecutor(new TreasuryCommandExecutor());
        this.getCommand("tes").setExecutor(new TreasuryCommandExecutor());

        // Registrazione event-listeners
        info("Registering listeners...");
        this.getServer().getPluginManager().registerEvents(new TownListener(), this);
        this.getServer().getPluginManager().registerEvents(new GUIGoalsListener(), this);
        this.getServer().getPluginManager().registerEvents(new GoalsChestListener(), this);
        this.getServer().getPluginManager().registerEvents(new GoalsChunkListener(), this);
        this.getServer().getPluginManager().registerEvents(new GoalAreaListener(), this);
        this.getServer().getPluginManager().registerEvents(new MantenimentoBonusListener(), this);
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
        Bukkit.getLogger().log(Level.INFO,"[FWObbiettivi] " + ChatColor.GREEN + msg);
    }

    public static void warning(String msg)
    {
        Bukkit.getLogger().log(Level.WARNING,"[FWObbiettivi] " + ChatColor.GOLD + msg);
    }

    public static void error(String msg)
    {
        Bukkit.getLogger().log(Level.SEVERE,"[FWObbiettivi] " + ChatColor.RED + msg);
    }

    public static void debug(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"[FWObbiettivi] [Debug]" + ChatColor.AQUA + msg);
    }

    // Metodi per il salvataggio dei dati su file e per il loro caricamento
    public static void loadConfigManager(){
        database = new ConfigManager();
        database.setup("database.yml");
        branches = new ConfigManager();
        branches.setup("branches.yml");
        goals = new ConfigManager();
        goals.setup("goals.yml");
    }

    public ConfigManager getDatabase(){
        return database;
    }

    public ConfigManager getBranches(){
        return branches;
    }

    public ConfigManager getGoals(){
        return goals;
    }

    public static void loadData() {
        info("Loading Configurations...");
        instance.reloadConfig();

        info("Loading Branches...");
        Branches.load();

        info("Loading Goals...");
        Goals.load();

        info("Loading Treasuries...");
        Treasuries.load();

        info("Loading TownGoals...");
        TownGoals.load();

        info("Loading Data...");
        GoalAreaManager.load();
        info("Finish loading saves");
    }

    public static void saveData() {
        info("Saving Configurations...");
        //instance.saveConfig();

        info("Saving Branches...");
        Branches.save();

        info("Saving Goals...");
        Goals.save();

        info("Saving Treasuries...");
        Treasuries.save();

        info("Saving TownGoals...");
        TownGoals.save();

        info("Saving Data...");
        GoalAreaManager.save();
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
