package it.forgottenworld.fwobbiettivi.command.user.goal;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.LIST_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.LIST_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_LIST;
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
        // TODO List Goals Pagination

        sender.sendMessage(ChatFormatter.chatHeader());
        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOALS_LIST));
        for (Goal g : Goals.getObbiettivi()) {
            sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + g.getName()));
        }
        sender.sendMessage(ChatFormatter.chatFooter());
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
