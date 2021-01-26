package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import javafx.util.Pair;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class GoalsChunkListener implements Listener {

    @EventHandler
    public void onPlaceBlockOnChunkEvent(BlockPlaceEvent e){
        if (ConfigUtil.CHUNK) {
            if (!ConfigUtil.getConfigStringListAllowedBlocks().contains(e.getBlock().getType().toString())) {
                Pair<Integer, Integer> chunk = new Pair<>(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ());
                if (GoalAreaManager.getChunks().get(chunk) != null && GoalAreaManager.getChunks().get(chunk).stream().anyMatch(k->k.isActive()==true)) {
                    e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PLACE_IN_GOAL));
                    e.setCancelled(true);
                }

                if (GoalAreaManager.getChunksTes().get(chunk) != null) {
                    e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PLACE_IN_GOAL));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreakBlockOnChunkEvent(BlockBreakEvent e){
        if (ConfigUtil.CHUNK) {
            if (!ConfigUtil.getConfigStringListAllowedBlocks().contains(e.getBlock().getType().toString())) {
                Pair<Integer, Integer> chunk = new Pair<>(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ());
                if (GoalAreaManager.getChunks().get(chunk) != null && GoalAreaManager.getChunks().get(chunk).stream().anyMatch(k->k.isActive()==true)) {
                    e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_BREAK_IN_GOAL));
                    e.setCancelled(true);
                }

                if (GoalAreaManager.getChunksTes().get(chunk) != null) {
                    e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_BREAK_IN_GOAL));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplodeOnChunkEvent(EntityExplodeEvent e){
        if (ConfigUtil.CHUNK) {
            Pair<Integer, Integer> chunk = new Pair<>(e.getLocation().getChunk().getX(), e.getLocation().getChunk().getZ());
            if (GoalAreaManager.getChunks().get(chunk) != null && GoalAreaManager.getChunks().get(chunk).stream().anyMatch(k->k.isActive()==true)) {
                e.setCancelled(true);
            }

            if (GoalAreaManager.getChunksTes().get(chunk) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockExplodeOnChunkEvent(BlockExplodeEvent e){
        if (ConfigUtil.CHUNK) {
            Pair<Integer, Integer> chunk = new Pair<>(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ());
            if (GoalAreaManager.getChunks().get(chunk) != null && GoalAreaManager.getChunks().get(chunk).stream().anyMatch(k->k.isActive()==true)) {
                e.setCancelled(true);
            }

            if (GoalAreaManager.getChunksTes().get(chunk) != null) {
                e.setCancelled(true);
            }
        }
    }

}
