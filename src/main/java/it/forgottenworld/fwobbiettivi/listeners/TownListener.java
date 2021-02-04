package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.event.town.TownPreUnclaimEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.managers.RewardPermissions;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class TownListener implements Listener {

    @EventHandler
    public void onDisbandTownEvent(PreDeleteTownEvent e){
        ArrayList<TownGoal> remove = new ArrayList<>();
        ArrayList<Treasury> removeTes = new ArrayList<>();

        // Removing Goals from Town when it disband
        for(TownGoal tg: TownGoals.getObbiettiviInTown()){
            if(tg.getTown().getName().equals(e.getTownName())){
                remove.add(tg);
            }
        }
        for (TownGoal tg : remove) {
            // Removing Goal to that Town
            TownGoals.removeTownGoal(tg);
        }

        // Removing Treasury from Town when it disband
        for(Treasury tes: Treasuries.getTreasuries()){
            if(tes.getTown().getName().equals(e.getTownName())){
                removeTes.add(tes);
            }
        }
        for (Treasury tes : removeTes) {
            // Removing Treasury to that Town
            Treasuries.removeTreasury(tes);
        }
    }

    @EventHandler
    public void onUnclaimTownEvent(TownPreUnclaimEvent e){
        List<TownGoal> tg = GoalAreaManager.getChunks().get(new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), e.getTownBlock().getX() * 16, 64, e.getTownBlock().getZ() * 16).getChunk());
        Treasury tes = GoalAreaManager.getChunksTes().get(new Location(Bukkit.getServer().getWorld(ConfigUtil.getWorldName()), e.getTownBlock().getX() * 16, 64, e.getTownBlock().getZ() * 16).getChunk());

        if (tg != null) {
            e.setCancelled(true);
            FWObbiettivi.info(Messages.GOAL_UNCLAIM_REMOVED_ABORT + " " + e.getTown().getName());
        }

        if (tes != null) {
            e.setCancelled(true);
            FWObbiettivi.info(Messages.TREASURY_UNCLAIM_REMOVED_ABORT + " " + e.getTown().getName());
        }
    }

    @EventHandler
    public void onResidentJoinTown(TownAddResidentEvent e){
        Player player = e.getResident().getPlayer();

        for (Goal g : TownGoals.getGoalFromTown(e.getTown())) {
            if (!g.getRewardPermissions().isEmpty()) {
                for (String perm : g.getRewardPermissions()) {
                    RewardPermissions.addPermission(player, perm);
                }
            }
        }
    }

    @EventHandler
    public void onResidentLeaveTown(TownRemoveResidentEvent e){
        Player player = e.getResident().getPlayer();

        for (Goal g : TownGoals.getGoalFromTown(e.getTown())) {
            if (!g.getRewardPermissions().isEmpty()) {
                for (String perm : g.getRewardPermissions()) {
                    RewardPermissions.removePermission(player, perm);
                }
            }
        }
    }
}
