package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.TownPreUnclaimEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.Treasuries;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import it.forgottenworld.fwobbiettivi.utility.Messages;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

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
        TownGoal tg = GoalAreaManager.getChunks().get(new Pair<>(e.getTownBlock().getX(), e.getTownBlock().getZ()));
        Treasury tes = GoalAreaManager.getChunksTes().get(new Pair<>(e.getTownBlock().getX(), e.getTownBlock().getZ()));

        if (tg != null) {
            // search if coord is in a goal
            if (!GoalAreaManager.isOnTownGoal(new Location(tg.getLocation().getWorld(), (e.getTownBlock().getX() * 16), 64, (e.getTownBlock().getZ() * 16))))
                return;

            e.setCancelled(true);
            FWObbiettivi.info(Messages.GOAL_UNCLAIM_REMOVED_ABORT + " " + e.getTown().getName());
        }

        if (tes != null) {
            // search if coord is in a goal
            if (!GoalAreaManager.isOnTreasury(new Location(tg.getLocation().getWorld(), (e.getTownBlock().getX() * 16), 64, (e.getTownBlock().getZ() * 16))))
                return;

            e.setCancelled(true);
            FWObbiettivi.info(Messages.TREASURY_UNCLAIM_REMOVED_ABORT + " " + e.getTown().getName());
        }
    }

}
