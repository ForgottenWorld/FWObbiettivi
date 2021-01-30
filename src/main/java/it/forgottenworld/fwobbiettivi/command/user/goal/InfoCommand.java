package it.forgottenworld.fwobbiettivi.command.user.goal;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.GoalCommandNames;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InfoCommand extends SubCommand {
    @Override
    public String getName() {
        return GoalCommandNames.INFO_CMD;
    }

    @Override
    public String getDescription() {
        return GoalCommandDescriptions.INFO_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<obName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_INFO;
    }

    @Override
    public boolean getCanConsoleRunCmd() {
        return false;
    }

    @Override
    public int getArgsRequired() {
        return 2;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Goal goal = Goals.getGoalFromString(args[1]);

        if (goal == null){
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_GOAL_IN_LIST) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
            return;
        }

        player.sendMessage(ChatFormatter.chatHeader());

        player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("NAME: ") + ChatColor.GRAY + goal.getName());
        player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("BRANCH: ") + ChatColor.GRAY + goal.getBranch().getName());
        player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("DESCRIPTION: ") + ChatColor.GRAY + ChatFormatter.listFormatter(goal.getDescrizione()));
        player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("PLOT: ") + ChatColor.GRAY + goal.getNumPlot());
        player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("REQUIRED GOALS: ") + ChatColor.GRAY + ChatFormatter.listFormatter(goal.getRequiredGoals()));
        if (goal.getRequiredZenar() > 0)
            player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("REQUIRED ZENAR: ") + ChatColor.GRAY + goal.getRequiredZenar());
        if (!goal.getRequiredObjects().isEmpty())
            player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("REQUIRED OBJECT: ") + ChatColor.GRAY + ChatFormatter.listFormatter(goal.getRequiredObjects()));
        player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("PAYMENT: ") + ChatColor.GRAY + ChatFormatter.listFormatter(goal.getPayment()));
        if (goal.getRequiredZenar() > 0)
            player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("REWARD ZENAR: ") + ChatColor.GRAY + goal.getRewardZenar());
        if (!goal.getReward().isEmpty())
            player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("REWARD OBJECT: ") + ChatColor.GRAY + ChatFormatter.listFormatter(goal.getReward()));
        if (!goal.getRewardPermissions().isEmpty())
            player.sendMessage(ChatFormatter.formatWarningMessageNoPrefix("REWARD PERMS: ") + ChatColor.GRAY + ChatFormatter.listFormatter(goal.getRewardPermissions()));

        player.sendMessage(ChatFormatter.chatFooter());
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
