package it.forgottenworld.fwobbiettivi.command.admin.branch;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.managers.Branches;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.BranchCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.BranchCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends SubCommand {
    @Override
    public String getName() {
        return BranchCommandNames.DELETE_CMD;
    }

    @Override
    public String getDescription() {
        return BranchCommandDescriptions.DELETE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<branchName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_BRANCH_DELETE;
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
        Branch branch = Branches.getBranchFromString(args[1]);

        if (!Branches.containsBranch(branch)) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.BRANCH_NOT_EXIST));
            return;
        }

        // Check if exist goal with this branch
        if (!Goals.getGoalsFromBranch(Branches.getBranchFromString(args[1])).isEmpty()){
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.BRANCH_NOT_EMPTY));
            return;
        }

        // Delete Branch
        Branches.removeBranch(branch);
        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.BRANCH_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(args[1]));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<String>();

        if (args.length == 2) {
            for (Branch branch : Branches.getRami()) {
                if (branch.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(branch.getName());
            }
        }

        return result;
    }
}
