package it.forgottenworld.fwobbiettivi.command.admin;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.command.SubCommand;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import it.forgottenworld.fwobbiettivi.utility.cmd.CommandDescriptions;
import it.forgottenworld.fwobbiettivi.utility.cmd.CommandNames;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiCommand extends SubCommand {
    @Override
    public String getName() {
        return CommandNames.GUI_CMD;
    }

    @Override
    public String getDescription() {
        return CommandDescriptions.GUI_CMD_DESCRIPTION;
    }

    @Override
    public String getArgumentsName() {
        return "";
    }

    @Override
    public String getPermission() {
        return Permissions.PERM_GUI;
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

        FWObbiettivi.getInstance().map.put(player.getUniqueId(), new GoalsGUI());
        FWObbiettivi.getInstance().map.get(player.getUniqueId()).setPlayer(player);
        FWObbiettivi.getInstance().map.get(player.getUniqueId()).openGUI(GUIUtil.GOALS_STEP);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
