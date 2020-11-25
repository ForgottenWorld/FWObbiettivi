package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.TownPreUnclaimEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.GoalAreaManager;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class TownListener implements Listener {

    @EventHandler
    public void onDisbandTownEvent(PreDeleteTownEvent e){
        ArrayList<TownGoal> remove = new ArrayList<>();
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
    }

    @EventHandler
    public void onUnclaimTownEvent(TownPreUnclaimEvent e){
        TownGoal tg = GoalAreaManager.getChunks().get(new Pair<>(e.getTownBlock().getX(), e.getTownBlock().getZ()));

        if (tg != null) {
            // search if coord is in a goal
            if (!GoalAreaManager.isOnTownGoal(new Location(tg.getLocation().getWorld(), (e.getTownBlock().getX() * 16), 64, (e.getTownBlock().getZ() * 16))))
                return;

            e.setCancelled(true);
            //todo messaggio
            FWObbiettivi.info("Ha cercato di unclaimare un obbiettivo: " + e.getTown().getName());
        }
    }

}
