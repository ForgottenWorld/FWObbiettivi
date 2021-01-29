package it.forgottenworld.fwobbiettivi.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getArgumentsName();

    public abstract String getPermission();

    public abstract boolean getCanConsoleRunCmd();

    public abstract int getArgsRequired();

    public abstract void perform(CommandSender sender, String[] args);

    public abstract List<String> getSubcommandArguments(Player player, String args[]);

}
