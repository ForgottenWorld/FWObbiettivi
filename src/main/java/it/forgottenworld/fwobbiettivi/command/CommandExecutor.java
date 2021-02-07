package it.forgottenworld.fwobbiettivi.command;

import it.forgottenworld.fwobbiettivi.command.admin.GuiCommand;
import it.forgottenworld.fwobbiettivi.command.admin.ReloadCommand;
import it.forgottenworld.fwobbiettivi.command.user.HelpCommand;
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

public class CommandExecutor implements TabExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandExecutor(){
        subcommands.add(new GuiCommand());
        subcommands.add(new HelpCommand());
        subcommands.add(new ReloadCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();
            ArrayList<String> result = new ArrayList<String>();

            if (subcommandsArguments.isEmpty()) {
                for (SubCommand subCmd : getSubcommands()){
                    if (sender.hasPermission(subCmd.getPermission()))
                        subcommandsArguments.add(subCmd.getName());
                }
            }

            for (String a : subcommandsArguments){
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }

            return result;

        } else if (args.length >= 2) {
            for (SubCommand subCmd : getSubcommands()){
                if (sender.hasPermission(subCmd.getPermission())) {
                    ArrayList<String> suggestion = new ArrayList<>();
                    ArrayList<String> result = new ArrayList<String>();

                    suggestion.addAll(subCmd.getSubcommandArguments((Player) sender, args));

                    for (String a : suggestion) {
                        if (a.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                            result.add(a);
                    }

                    return result;
                }
            }
        }
        return null;
    }
}