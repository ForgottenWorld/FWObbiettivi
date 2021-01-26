package it.forgottenworld.fwobbiettivi.config;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private FWObbiettivi plugin = FWObbiettivi.getPlugin(FWObbiettivi.class);

    public FileConfiguration cfg;
    public File file;

    public void setup(String name){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), name);

        if(!file.exists()){
            try{
                file.createNewFile();
                FWObbiettivi.info(Messages.FILE_CREATED + " " + name);
            } catch (IOException e){
                FWObbiettivi.error(Messages.FILE_NOT_CREATED + " " + name);
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getFile(){
        return cfg;
    }

    public void saveFile(){
        try{
            cfg.save(file);
            //FWObbiettivi.info(Messages.FILE_SAVED + " " + getFile().getName());
        } catch (IOException e){
            FWObbiettivi.error(Messages.FILE_NOT_SAVED + " " + getFile().getName());
        }
    }

    public void reloadFile(){
        cfg = YamlConfiguration.loadConfiguration(file);
        FWObbiettivi.info(Messages.FILE_RELOADED + " " + getFile().getName());
    }
}
