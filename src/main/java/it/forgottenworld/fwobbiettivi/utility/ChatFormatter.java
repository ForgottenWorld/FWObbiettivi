package it.forgottenworld.fwobbiettivi.utility;

import org.bukkit.ChatColor;

import java.util.Collections;

public class ChatFormatter {

    /**
     * PREFIX
     */
    public static String pluginPrefix() {
        return  ChatColor.DARK_GRAY + "[" +
                ChatColor.DARK_AQUA + "Obbiettivi" +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.RESET;
    }

    public static String debugPrefix() {
        return  ChatColor.AQUA + "[ " +
                ChatColor.RED + "DEBUG" +
                ChatColor.AQUA + " ]";
    }

    /**
     * HEADER & FOOTER
     */
    public static String chatHeader() {
        return  ChatColor.AQUA + "---------------------[ " +
                ChatColor.DARK_AQUA + "Obbiettivi" +
                ChatColor.AQUA + " ]----------------------";
    }

    public static String chatFooter() {
        return  ChatColor.AQUA + String.join("", Collections.nCopies(53, "-"));
    }

    /**
     * MESSAGE
     */
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

    public static String formatSuccessMessageNoPrefix(String message) {
        message = ChatColor.GREEN + message;
        return message;
    }

    public static String formatWarningMessageNoPrefix(String message) {
        message = ChatColor.GOLD + message;
        return message;
    }

    public static String formatErrorMessageNoPrefix(String message) {
        message = ChatColor.RED + message;
        return message;
    }

    public static String formatDebugMessage(String message) {
        message = pluginPrefix() + debugPrefix() + ChatColor.AQUA + message;
        return message;
    }

    public static String formatListMessage(String command, String subcommand, String args, String description) {
        String message = ChatColor.GOLD + "> " +
                ChatColor.DARK_AQUA + "/" + command + " " +
                ChatColor.AQUA + subcommand + " " + args +
                ChatColor.GRAY + ": " + description;
        return message;
    }
}