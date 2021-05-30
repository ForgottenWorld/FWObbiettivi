package it.forgottenworld.fwobbiettivi.command.admin.goal;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StatusCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.STATUS_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.STATUS_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_STATUS;
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
        Player player = (Player) sender;

        if (TownGoals.getObbiettiviInTown().isEmpty()) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_LOC));
            return;
        }

        sender.sendMessage(ChatFormatter.chatHeader());
        // Check if a goal exist in that town
        for (TownGoal tg : GoalAreaManager.getListTownGoalFromChunk(player.getLocation().getChunk().getChunkKey())){
            if (tg.isActive()) {
                player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + tg.getGoal().getName()) + " - " + ChatFormatter.formatSuccessMessageNoPrefix("ENABLED"));
            } else {
                player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("- " + tg.getGoal().getName()) + " - " + ChatFormatter.formatErrorMessageNoPrefix("DISABLED"));
            }
        }
        sender.sendMessage(ChatFormatter.chatFooter());
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
