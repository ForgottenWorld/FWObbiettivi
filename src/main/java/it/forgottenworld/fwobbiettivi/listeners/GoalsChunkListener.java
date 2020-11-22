package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
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
        Pair<Integer, Integer> chunk = new Pair<>(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ());
        if (FWObbiettivi.getInstance().chunks.get(chunk) != null && FWObbiettivi.getInstance().chunks.get(chunk).isActive()){
            e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_BREAK_IN_GOAL));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakBlockOnChunkEvent(BlockBreakEvent e){
        Pair<Integer, Integer> chunk = new Pair<>(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ());
        if (FWObbiettivi.getInstance().chunks.get(chunk) != null && FWObbiettivi.getInstance().chunks.get(chunk).isActive()){
            e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PLACE_IN_GOAL));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplodeOnChunkEvent(EntityExplodeEvent e){
        Pair<Integer, Integer> chunk = new Pair<>(e.getLocation().getChunk().getX(), e.getLocation().getChunk().getZ());
        if (FWObbiettivi.getInstance().chunks.get(chunk) != null && FWObbiettivi.getInstance().chunks.get(chunk).isActive()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplodeOnChunkEvent(BlockExplodeEvent e){
        Pair<Integer, Integer> chunk = new Pair<>(e.getBlock().getLocation().getChunk().getX(), e.getBlock().getLocation().getChunk().getZ());
        if (FWObbiettivi.getInstance().chunks.get(chunk) != null && FWObbiettivi.getInstance().chunks.get(chunk).isActive()){
            e.setCancelled(true);
        }
    }

}
