package it.forgottenworld.fwobbiettivi.command;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.ObbiettiviGUI;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import it.forgottenworld.fwobbiettivi.utility.Messages;
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
            case 1:
                switch (args[0].toLowerCase()){
                    case "help":
                        sender.sendMessage(ChatFormatter.helpMessage());
                        return true;
                    case "reload":
                        FWObbiettivi.info("Saving infos...");

                        FWObbiettivi.info("Data saved");

                        FWObbiettivi.info("Loading infos...");

                        FWObbiettivi.info("Data loades");

                        FWObbiettivi.instance.reloadConfig();
                        FWObbiettivi.info(Messages.CONFIG_RELOAD);
                        return true;
                }
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
