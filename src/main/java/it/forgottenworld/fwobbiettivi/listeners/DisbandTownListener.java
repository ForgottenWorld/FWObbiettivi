package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.fwobbiettivi.utility.ConfigUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisbandTownListener implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onDisbandTownEvent(DeleteTownEvent e){
        // Removing Goals from Town when it disband
        for(TownGoals tg: FWObbiettivi.instance.obbiettiviInTown){
            if(tg.getTown().getName() == e.getTownName()){
                Block bRemove = tg.getLocation().getBlock();
                ((Chest) bRemove.getState()).setCustomName("Chest");
                bRemove.removeMetadata("goalchest", FWObbiettivi.instance);

                if(ConfigUtil.DEBUG)
                    FWObbiettivi.debug("Rimosso: " + tg.getGoal().getName() + " da " + tg.getTown().getName());

                // Removing Goal to that Town
                FWObbiettivi.instance.obbiettiviInTown.remove(tg);
            }
        }

        // Saving
        FWObbiettivi.saveData();
    }

}
