package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisbandTownListener implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onDisbandTownEvent(DeleteTownEvent e){
        // Rimozione Obbiettivi Cittadini riguarganti e.getTownName()
    }

}
