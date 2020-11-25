package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.WorldCoord;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        if (GoalAreaManager.getChunks().containsKey(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ())))
            return;

        List<Pair<Integer, Integer>> chunksList = new ArrayList<>();
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX() + 1, e.getClickedBlock().getChunk().getZ()));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX() - 1, e.getClickedBlock().getChunk().getZ()));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ() + 1));
        chunksList.add(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ() - 1));

        for (Pair<Integer, Integer> chunk:chunksList){
            if (GoalAreaManager.getChunks().containsKey(chunk) &&
                    GoalAreaManager.getInstance().getPlayerGoalAreaCreation().get(e.getPlayer().getUniqueId()).equals(GoalAreaManager.getChunks().get(chunk))){
                int maxPlot = GoalAreaManager.getChunks().get(chunk).getGoal().getNumPlot();
                long chunkCount = GoalAreaManager.getChunks().values().stream().filter(v -> v.equals(GoalAreaManager.getChunks().get(chunk))).count();

                if (chunkCount < maxPlot){

                    try {
                        if (WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock() == WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock()){
                            WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock().setName(GoalAreaManager.getChunks().get(chunk).getGoal().getName());
                            // Saving new plot name
                            TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(e.getClickedBlock().getLocation()).getTownBlock());
                        }

                        GoalAreaManager.getChunks().put(new Pair<>(e.getClickedBlock().getChunk().getX(), e.getClickedBlock().getChunk().getZ()), GoalAreaManager.getChunks().get(chunk));
                    } catch (NotRegisteredException event) {
                        // Not in a town
                        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_TOWN_LOC));
                        return;
                    }

                    e.getPlayer().sendMessage(ChatFormatter.formatSuccessMessage(Messages.GOAL_PLOT_NEEDED) + " " + ChatFormatter.formatWarningMessageNoPrefix((chunkCount + 1) + "/" + maxPlot));

                    if (e.getPlayer().getTargetBlockExact(5) == null)
                        chunkCount--;
                    else
                        e.getPlayer().getTargetBlockExact(5).getWorld().playSound(e.getPlayer().getTargetBlockExact(5).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);

                    GoalAreaManager.spawnEffectAtBlock(e.getPlayer().getTargetBlockExact(5).getLocation());

                    if ((chunkCount + 1) == maxPlot)
                        GoalAreaManager.getInstance().getPlayerGoalAreaCreation().remove(e.getPlayer().getUniqueId());
                }
                return;
            }
        }

        e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.GOAL_PLOT_NOT_NEAR));

    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if (!GoalAreaManager.getInstance().isPlayerInCreationMode(player))
            return;

        TownGoal tg = GoalAreaManager.getInstance().getPlayerGoalAreaCreation().get(e.getPlayer().getUniqueId());

        Iterator<Map.Entry<Pair<Integer, Integer>, TownGoal>> it = GoalAreaManager.getChunks().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Pair<Integer, Integer>, TownGoal> entry = it.next();
            if (tg.equals(entry.getValue())){
                try {
                    WorldCoord.parseWorldCoord(new Location(e.getPlayer().getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16))).getTownBlock().setName("");
                    // Saving new plot name
                    TownyUniverse.getInstance().getDataSource().saveTownBlock(WorldCoord.parseWorldCoord(new Location(e.getPlayer().getWorld(), (entry.getKey().getKey() * 16), 64, (entry.getKey().getValue() * 16))).getTownBlock());
                } catch (NotRegisteredException event) {}
                it.remove();
            }
        }
        // Removing Goal to that Town
        TownGoals.removeTownGoal(tg);
        FWObbiettivi.info(Messages.ABORT_ADD_GOAL);

        GoalAreaManager.getInstance().getPlayerGoalAreaCreation().remove(e.getPlayer().getUniqueId());
    }

}
