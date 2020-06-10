package it.forgottenworld.fwobbiettivi.command;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.ObbiettiviGUI;
import it.forgottenworld.fwobbiettivi.utility.*;
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

                break;
            case 1:
                switch (args[0].toLowerCase()){
                    case "gui":
                        // Deve essere un Player
                        if (!(sender instanceof Player)){
                            sender.sendMessage(Messages.NO_CONSOLE);
                            return true;
                        }

                        Player player = (Player) sender;
                        // Ha i permessi?
                        if(!player.hasPermission(Permissions.PERM_GUI)){
                            player.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

                        ObbiettiviGUI gui = new ObbiettiviGUI(player);
                        gui.openGUI(GUIUtil.GOALS_STEP);
                        return true;
                    case "help":
                        sender.sendMessage(ChatFormatter.helpMessage());
                        return true;
                    case "reload":
                        // Ha i permessi?
                        if(!sender.hasPermission(Permissions.PERM_RELOAD)){
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

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
