package it.forgottenworld.fwobbiettivi.command.admin.branch;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.prompt.BranchCreationPrompt;
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
        return "";
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
        return 1;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // Aggiungo ramo
        BranchCreationPrompt creationPrompt = new BranchCreationPrompt(FWObbiettivi.getPlugin(FWObbiettivi.class));
        creationPrompt.startConversationForPlayer(player);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
