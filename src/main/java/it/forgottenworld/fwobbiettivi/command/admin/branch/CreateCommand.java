package it.forgottenworld.fwobbiettivi.command.admin.branch;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.managers.Branches;
import it.forgottenworld.fwobbiettivi.prompt.BranchCreationPrompt;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.BranchCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.BranchCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends SubCommand {
    @Override
    public String getName() {
        return BranchCommandNames.CREATE_CMD;
    }

    @Override
    public String getDescription() {
        return BranchCommandDescriptions.CREATE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<branchName>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_BRANCH_CREATE;
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
        // Check if already exist
        if (Branches.containsBranch(Branches.getBranchFromString(args[1]))) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.BRANCH_ALREADY_EXIST));
            return;
        }

        // Aggiungo ramo
        BranchCreationPrompt creationPrompt = new BranchCreationPrompt(FWObbiettivi.getPlugin(FWObbiettivi.class));
        creationPrompt.startConversationForPlayer(player);

        // Saving
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
