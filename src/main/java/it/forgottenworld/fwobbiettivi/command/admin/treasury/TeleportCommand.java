package it.forgottenworld.fwobbiettivi.command.admin.treasury;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandNames;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportCommand extends SubCommand {
    @Override
    public String getName() {
        return TreasuryCommandNames.TP_CMD;
    }

    @Override
    public String getDescription() {
        return TreasuryCommandDescriptions.TP_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<town>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_TREASURY_TP;
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

        Location location = null;

        for (Treasury tes : Treasuries.getTreasuries()) {
            if (args[1].equals(tes.getTown().getName())) {
                location = tes.getLocationChestRight();
                player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TELEPORTED_TO) + " " + ChatFormatter.formatWarningMessageNoPrefix(tes.getTown().getName()));
                player.teleport(location);
                // Teleport success
                return;
            }
        }

        // Teleport failed
        player.sendMessage(ChatFormatter.formatErrorMessage(Messages.TELEPORTED_FAILED));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> result = new ArrayList<String>();

        if (args.length == 2){
            Town[] towns = new Town[TownyUniverse.getInstance().getDataSource().getTowns().size()];
            TownyUniverse.getInstance().getDataSource().getTowns().toArray(towns);
            for (int i = 0; i < towns.length; i++) {
                if (towns[i].getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    if (Treasuries.containsTreasury(towns[i]))
                        result.add(towns[i].getName());
            }
        }

        return result;
    }
}