package it.forgottenworld.fwobbiettivi.utility;

import org.bukkit.ChatColor;

public class ChatFormatter {

    public static String pluginPrefix() {
        return  ChatColor.DARK_GRAY + "[" +
                ChatColor.DARK_AQUA + "Obbiettivi" +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.RESET;
    }

    public static String chatHeader() {
        return  ChatColor.AQUA + "-------------------[ " +
                ChatColor.DARK_AQUA + "Obbiettivi" +
                ChatColor.AQUA + " ]-------------------";
    }

    public static String formatSuccessMessage(String message) {
        message = pluginPrefix() + ChatColor.GREEN + message;
        return message;
    }

    public static String formatWarningMessage(String message) {
        message = pluginPrefix() + ChatColor.GOLD + message;
        return message;
    }

    public static String formatErrorMessage(String message) {
        message = pluginPrefix() + ChatColor.RED + message;
        return message;
    }

    public static String helpMessage() {
        String message = chatHeader();
        message = message.concat(
                "\n" + ChatColor.GRAY + "Lista comandi:" +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.GRAY + ": ." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "add " + "<Obbiettivo> " + ChatColor.GRAY + ": Aggiunge una Town al Cicero." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "gui " + ChatColor.GRAY + ": Apre la GUI." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "help " + ChatColor.GRAY + ": Mostra questa lista." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "pay " + ChatColor.GRAY + ": Paga tutti gli obbiettivi presenti." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "reload " + ChatColor.GRAY + ": Ricarica il config.yml."
        );
        return message;
    }

}
