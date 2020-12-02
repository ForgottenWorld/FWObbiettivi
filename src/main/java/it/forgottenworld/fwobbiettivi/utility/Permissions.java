package it.forgottenworld.fwobbiettivi.utility;

import org.bukkit.command.CommandSender;

public class Permissions {

    public static final String PERM_ADD = "forgotten.obbiettivi.add";
    public static final String PERM_CREATE = "forgotten.obbiettivi.create";
    public static final String PERM_DELETE = "forgotten.obbiettivi.delete";
    public static final String PERM_DISABLE = "forgotten.obbiettivi.disable";
    public static final String PERM_EDIT = "forgotten.obbiettivi.edit";
    public static final String PERM_ENABLE = "forgotten.obbiettivi.enable";
    public static final String PERM_GUI = "forgotten.obbiettivi.gui";
    public static final String PERM_LIST = "forgotten.obbiettivi.list";
    public static final String PERM_PAY = "forgotten.obbiettivi.pay";
    public static final String PERM_RELOAD = "forgotten.obbiettivi.reload";
    public static final String PERM_REMOVE = "forgotten.obbiettivi.remove";
    public static final String PERM_SHOW = "forgotten.obbiettivi.show";
    public static final String PERM_TP = "forgotten.obbiettivi.teleport";
    public static final String PERM_TREASURY = "forgotten.obbiettivi.treasury";
    public static final String PERM_TREASURY_ADD = "forgotten.obbiettivi.treasury.add";
    public static final String PERM_TREASURY_OPEN = "forgotten.obbiettivi.treasury.open";
    public static final String PERM_TREASURY_REMOVE = "forgotten.obbiettivi.treasury.remove";
    public static final String PERM_TREASURY_SHOW = "forgotten.obbiettivi.treasury.show";
    public static final String PERM_TREASURY_TP = "forgotten.obbiettivi.treasury.tp";

    public static boolean playerHasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission))
            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));

        return sender.hasPermission(permission);
    }
}
