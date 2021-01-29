package it.forgottenworld.fwobbiettivi.command.user.goal;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.HELP_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.HELP_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_HELP;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // FIXME help command
        sender.sendMessage(ChatFormatter.chatHeader());

//        for (int i = 0; i < GoalsCommandExecutor.getSubcommands().size(); i++) {
//
//            SubCommand subCommand = GoalsCommandExecutor.getSubcommands().get(i);
//
//            if (!sender.hasPermission(subCommand.getPermission()))
//                continue;
//
//            sender.sendMessage(ChatFormatter.formatListMessage(command.toString(), subCommand.getName(), subCommand.getArgumentsName(), subCommand.getDescription()));
//        }

        sender.sendMessage(ChatFormatter.chatFooter());
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
