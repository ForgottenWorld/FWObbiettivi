package it.forgottenworld.fwobbiettivi.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class GoalsChestEvent implements Listener {

    @EventHandler
    public void onGoalsChestDestroyEvent(BlockBreakEvent e){
        // Check if block is a CHEST
        if (e.getBlock().getType().equals(Material.CHEST) && e.getBlock().hasMetadata("goalchest")){
            // TODO Messaggio non puoi spaccare
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestsPlaceNearbyEvent(BlockPlaceEvent e){
        // Check if block is a CHEST
        if (e.getBlock().getType().equals(Material.CHEST)){
            List<Block> blocksList = new ArrayList<>();
            World w = e.getBlock().getLocation().getWorld();
            blocksList.add(w.getBlockAt(e.getBlock().getX() + 1 , e.getBlock().getY(), e.getBlock().getZ()));
            blocksList.add(w.getBlockAt(e.getBlock().getX() - 1 , e.getBlock().getY(), e.getBlock().getZ()));
            blocksList.add(w.getBlockAt(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ() + 1));
            blocksList.add(w.getBlockAt(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ() - 1));

            for (Block b:blocksList){
                if (b.getType().equals(Material.CHEST) && b.hasMetadata("goalchest")){
                    // TODO Messaggio non puoi piazzare
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

}
