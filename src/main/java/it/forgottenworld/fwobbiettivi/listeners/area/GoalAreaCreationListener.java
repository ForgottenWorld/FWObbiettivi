package it.forgottenworld.fwobbiettivi.listeners.area;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.WorldCoord;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import javafx.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class GoalAreaCreationListener implements Listener {

    @EventHandler
    public void onRightClickSelection(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (e.getItem() != null)
            return;

        Player player = e.getPlayer();
        if (!GoalAreaManager.getInstance().isPlayerInCreationMode(player))
            return;

        if (FWObbiettivi.getInstance().chunks.containsKey(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ())))
            return;

        List<Pair<Integer, Integer>> chunksList = new ArrayList<>();
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX() + 1, e.getClickedBlock().getChunk().getZ()));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX() - 1, e.getClickedBlock().getChunk().getZ()));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ() + 1));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ() - 1));

        for (Pair<Integer, Integer> chunk:chunksList){
            if (FWObbiettivi.getInstance().chunks.containsKey(chunk) &&
                    GoalAreaManager.getInstance().getPlayerGoalAreaCreation().get(e.getPlayer().getUniqueId()).equals(FWObbiettivi.getInstance().chunks.get(chunk))){
                int maxPlot = FWObbiettivi.getInstance().chunks.get(chunk).getGoal().getNumPlot();
                long chunkCount = FWObbiettivi.getInstance().chunks.values().stream().filter(v -> v.equals(FWObbiettivi.getInstance().chunks.get(chunk))).count();

                if (chunkCount < maxPlot){

                    try {
                        if (WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock() == WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock()){
                            WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock().setName(FWObbiettivi.getInstance().chunks.get(chunk).getGoal().getName());
                            // Saving new plot name
                            TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock());
                        }

                        FWObbiettivi.getInstance().chunks.put(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ()), FWObbiettivi.getInstance().chunks.get(chunk));
                    } catch (NotRegisteredException event) {
                        // Not in a town
                        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        return;
                    }

                    e.getPlayer().sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix((chunkCount + 1) + "/" + maxPlot));
                    if ((chunkCount + 1) == maxPlot)
                        GoalAreaManager.getInstance().getPlayerGoalAreaCreation().remove(e.getPlayer().getUniqueId());
                }
                return;
            }
        }

        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_PLOT_NOT_NEAR));

    }


}
