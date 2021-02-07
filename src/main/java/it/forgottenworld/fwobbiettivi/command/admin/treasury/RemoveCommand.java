package it.forgottenworld.fwobbiettivi.command.admin.treasury;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.TownyUtil;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.TreasuryCommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand extends SubCommand {
    @Override
    public String getName() {
        return TreasuryCommandNames.REMOVE_CMD;
    }

    @Override
    public String getDescription() {
        return TreasuryCommandDescriptions.REMOVE_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "<town>";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_TREASURY_REMOVE;
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

        if (TownyUtil.getTownFromString(args[1]) == null || Treasuries.getFromTown(TownyUtil.getTownFromString(args[1])) == null) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_FOUND));
            return;
        }

        if (!TownGoals.getGoalFromTown(TownyUtil.getTownFromString(args[1])).isEmpty()) {
            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.TREASURY_REMOVED_ABORT));
            return;
        }

        Treasury tes = Treasuries.getFromTown(TownyUtil.getTownFromString(args[1]));
        player.sendMessage(ChatFormatter.formatSuccessMessage(Messages.TREASURY_REMOVED) + " " + ChatFormatter.formatWarningMessageNoPrefix(tes.getTown().getName()));
        Treasuries.removeTreasury(tes);
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