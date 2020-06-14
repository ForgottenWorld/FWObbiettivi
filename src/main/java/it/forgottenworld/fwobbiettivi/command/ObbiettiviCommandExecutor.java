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
                    case CommandTypes.ADD_COMMAND:
                        break;

                    case CommandTypes.DELETE_COMMAND:
                        break;


                    case CommandTypes.EDIT_COMMAND:
                        break;

                    case CommandTypes.GUI_COMMAND:
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

                    case CommandTypes.HELP_COMMAND:
                        sender.sendMessage(ChatFormatter.helpMessage());
                        return true;

                    case CommandTypes.MOVE_COMMAND:
                        break;

                    case CommandTypes.NEW_COMMAND:
                        break;

                    case CommandTypes.PAY_COMMAND:
                        // To do
                        return true;

                    case CommandTypes.RELOAD_COMMAND:
                        // Ha i permessi?
                        if(!sender.hasPermission(Permissions.PERM_RELOAD)){
                            sender.sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PERM));
                            return true;
                        }

                        FWObbiettivi.info("Saving infos...");
                        FWObbiettivi.saveData();
                        FWObbiettivi.info("Data saved");

                        FWObbiettivi.info("Loading infos...");
                        FWObbiettivi.loadData();
                        FWObbiettivi.info("Data loades");

                        FWObbiettivi.instance.reloadConfig();
                        FWObbiettivi.info(Messages.CONFIG_RELOAD);
                        return true;

                    case CommandTypes.REMOVE_COMMAND:
                        break;

                    case CommandTypes.TP_COMMAND:
                        break;
                }
                break;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        String argsIndex = "";

        switch (args.length){
            case 1:
                if (sender.hasPermission(Permissions.PERM_GUI))
                    suggestions.add(CommandTypes.GUI_COMMAND);

                suggestions.add(CommandTypes.HELP_COMMAND);

                if (sender.hasPermission(Permissions.PERM_PAY))
                    suggestions.add(CommandTypes.PAY_COMMAND);

                if (sender.hasPermission(Permissions.PERM_RELOAD))
                    suggestions.add(CommandTypes.RELOAD_COMMAND);

                break;
            case 2:
                break;
        }

        return NameUtil.filterByStart(suggestions, argsIndex);
    }

}
