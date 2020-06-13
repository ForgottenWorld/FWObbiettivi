package it.forgottenworld.fwobbiettivi;

import it.forgottenworld.fwobbiettivi.command.ObbiettiviCommandExecutor;
import it.forgottenworld.fwobbiettivi.objects.Obbiettivo;
import it.forgottenworld.fwobbiettivi.objects.Ramo;
import it.forgottenworld.fwobbiettivi.listeners.DisbandTownListener;
import it.forgottenworld.fwobbiettivi.listeners.GUIObbiettiviListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class FWObbiettivi extends JavaPlugin {

    static FileConfiguration defaultConfig;
    public static FWObbiettivi instance;
    public ArrayList<Ramo> listaRami = new ArrayList<Ramo>();
    public ArrayList<Obbiettivo> listaObbiettivo = new ArrayList<Obbiettivo>();

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
        this.getServer().getPluginManager().registerEvents(new GUIObbiettiviListener(), this);

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
