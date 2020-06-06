package it.forgottenworld.fwobbiettivi.command;

import it.forgottenworld.fwobbiettivi.gui.ObbiettiviGUI;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import it.forgottenworld.fwobbiettivi.utility.NameUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ObbiettiviCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args.length){
            case 0:
                ObbiettiviGUI gui = new ObbiettiviGUI((Player) sender);
                gui.openGUI(GUIUtil.GOALS_STEP);
                break;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        String argsIndex = "";

        return NameUtil.filterByStart(suggestions, argsIndex);
    }

}
