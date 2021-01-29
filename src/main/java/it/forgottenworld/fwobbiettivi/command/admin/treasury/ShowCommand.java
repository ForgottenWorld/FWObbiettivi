package it.forgottenworld.fwobbiettivi.command.admin.treasury;

import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ShowCommand extends SubCommand {
    @Override
    public String getName() {
        return TreasuryCommandNames.SHOW_CMD;
    }

    @Override
    public String getDescription() {
        return TreasuryCommandDescriptions.SHOW_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_TREASURY_SHOW;
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
        sender.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_LIST));
        sender.sendMessage(ChatFormatter.formatWarningMessageNoPrefix(Treasuries.getPrintableList()));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}