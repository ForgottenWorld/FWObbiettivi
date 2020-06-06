package it.forgottenworld.fwobbiettivi;

import it.forgottenworld.fwobbiettivi.command.ObbiettiviCommandExecutor;
import it.forgottenworld.fwobbiettivi.listeners.DisbandTownListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class FWObbiettivi extends JavaPlugin {

    static FileConfiguration defaultConfig;
    public static FWObbiettivi instance;

    @Override
    public void onEnable() {
        info("Enabled!");
        FWObbiettivi.instance = this;

        info("Loading configuration...");
        loadConfiguration();

        // Registrazione CommandExecutor
        info("Registering commands...");
        this.getCommand("obbiettivo").setExecutor(new ObbiettiviCommandExecutor());
        this.getCommand("ob").setExecutor(new ObbiettiviCommandExecutor());
        this.getCommand("goals").setExecutor(new ObbiettiviCommandExecutor());

        // Registrazione event-listeners
        info("Registering listeners...");
        this.getServer().getPluginManager().registerEvents(new DisbandTownListener(), this);

        // Caricamento delle aree da file
        info("Loading infos...");
        // Qualcosa
        info("Data loaded");
    }

    @Override
    public void onDisable() {
        info("Saving infos...");
        // Qualcosa
        info("Data Saved");
        info("Disabled");
    }

    //Metodi statici per semplificare il logging
    public static void info(String msg)
    {
        Bukkit.getLogger().log(Level.INFO,"["+"FWObbiettivi"+"] " + msg);
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
