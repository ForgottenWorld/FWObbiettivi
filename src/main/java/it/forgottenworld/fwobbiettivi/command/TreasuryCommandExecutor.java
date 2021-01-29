package it.forgottenworld.fwobbiettivi.command;

import it.forgottenworld.fwobbiettivi.command.admin.treasury.*;
import it.forgottenworld.fwobbiettivi.command.user.treasury.HelpCommand;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TreasuryCommandExecutor implements TabExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public TreasuryCommandExecutor(){
        subcommands.add(new AddCommand());
        subcommands.add(new HelpCommand());
        subcommands.add(new OpenCommand());
        subcommands.add(new RemoveCommand());
        subcommands.add(new ShowCommand());
        subcommands.add(new TeleportCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {

                    SubCommand subCommand = getSubcommands().get(i);

                    if (!subCommand.getCanConsoleRunCmd()) {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }
                    }

                    if (!Permissions.playerHasPermission(sender, subCommand.getPermission()))
                        return true;

                    if (args.length < subCommand.getArgsRequired()) {
                        sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.ERR_SYNTAX));
                        return true;
                    }

                    subCommand.perform(sender, args);

                }
            }
        }else{
            sender.sendMessage(ChatFormatter.chatHeader());

            for (int i = 0; i < getSubcommands().size(); i++) {

                SubCommand subCommand = getSubcommands().get(i);

                if (!sender.hasPermission(subCommand.getPermission()))
                    continue;

                sender.sendMessage(ChatFormatter.formatListMessage(label, subCommand.getName(), subCommand.getArgumentsName(), subCommand.getDescription()));
            }

            sender.sendMessage(ChatFormatter.chatFooter());
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++) {
                SubCommand subCommand = getSubcommands().get(i);

                if (!sender.hasPermission(subCommand.getPermission()))
                    continue;

                subcommandsArguments.add(subCommand.getName());
            }

            return subcommandsArguments;

        }else if (args.length >= 2) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }
}
