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
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "add " + "<Obbiettivo> <Town> " + ChatColor.GRAY + ": Aggiunge un obbiettivo a una Town." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "delete " + "<Obbiettivo> " + ChatColor.GRAY + ": Cancella un obbiettivo." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "edit " + "<Obbiettivo> " + ChatColor.GRAY + ": Modifica un obbiettivo." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "gui " + ChatColor.GRAY + ": Apre la GUI." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "help " + ChatColor.GRAY + ": Mostra questa lista." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "move " + "<Obbiettivo> <Town>" + ChatColor.GRAY + ": Sposta un obbiettivo di una Town sulla tua posizione." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "new " + "<Obbiettivo> " + ChatColor.GRAY + ": Crea un nuovo obbiettivo." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "pay " + ChatColor.GRAY + ": Paga tutti gli obbiettivi presenti." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "reload " + ChatColor.GRAY + ": Ricarica il config.yml." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "remove " + "<Obbiettivo> <Town>" + ChatColor.GRAY + ": Rimuove un obbiettivi a una Town." +
                "\n" + ChatColor.GOLD + "> " + ChatColor.DARK_AQUA + "/ob " + ChatColor.AQUA + "tp " + "<Obbiettivo> <Town>" + ChatColor.GRAY + ": Teletrasporta il player ad un determinato obbiettivo in una Town."
        );
        return message;
    }

}
