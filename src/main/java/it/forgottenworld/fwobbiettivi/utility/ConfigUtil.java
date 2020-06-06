package it.forgottenworld.fwobbiettivi.utility;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;

import java.util.List;

public class ConfigUtil {

    public static List<String> getConfigStringListLang(String path){
        return FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + "." + path);
    }

    public static String getConfigStringLang(String path){
        return FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + "." + path);
    }

    public static String getConfLang(){
        return FWObbiettivi.instance.getConfig().getString("languages.default");
    }

}
