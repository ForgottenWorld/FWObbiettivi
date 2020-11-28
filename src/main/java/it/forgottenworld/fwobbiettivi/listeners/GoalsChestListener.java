package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.utility.ChatFormatter;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GoalsChestListener implements Listener {

    @EventHandler
    public void onFWChestDestroyEvent(BlockBreakEvent e){
        // Check if block is a FWChest
        if (e.getBlock().getType().equals(Material.CHEST) && e.getBlock().hasMetadata("goalchest")){
            e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_BREAK_GOAL));
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
                    e.getPlayer().sendMessage(ChatFormatter.formatErrorMessage(Messages.NO_PLACE_NEAR_GOAL));
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onFWChestsExplodeEvent(EntityExplodeEvent e){
        // Check if block is a FWChest
        Iterator<Block> it = e.blockList().iterator();
        while(it.hasNext()){
            Block block = it.next();
            if (block.getType().equals(Material.CHEST) && block.hasMetadata("goalchest")){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFWChestsExplodeEvent(BlockExplodeEvent e){
        // Check if block is a FWChest
        Iterator<Block> it = e.blockList().iterator();
        while(it.hasNext()){
            Block block = it.next();
            if (block.getType().equals(Material.CHEST) && block.hasMetadata("goalchest")){
                e.setCancelled(true);
            }
        }
    }

}
