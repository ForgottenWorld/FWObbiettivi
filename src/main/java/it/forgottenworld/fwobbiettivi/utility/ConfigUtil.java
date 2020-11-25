package it.forgottenworld.fwobbiettivi.utility;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;

import java.util.*;

public class ConfigUtil {

    public static final boolean DEBUG = FWObbiettivi.getInstance().getConfig().getBoolean("debug");

    public static List<String> getConfigStringListLang(String path){
        return FWObbiettivi.getInstance().getConfig().getStringList("languages." + getConfLang() + "." + path);
    }

    public static String getConfigStringLang(String path){
        return FWObbiettivi.getInstance().getConfig().getString("languages." + getConfLang() + "." + path);
    }

    public static String getConfLang(){
        return FWObbiettivi.getInstance().getConfig().getString("languages.default");
    }

}
