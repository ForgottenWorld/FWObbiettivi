package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.TownUpkeepCalculationEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class MantenimentoBonusListener implements Listener {

    private static HashMap<String, Double> plotMantenimento = new HashMap<String, Double>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onTownUpkeepCalculated(TownUpkeepCalculationEvent e) {
        updateDatasFromConfig();
        for (TownGoal tg: TownGoals.getObbiettiviInTown()){
            if (e.getTown().equals(tg.getTown()))
                e.setUpkeep(e.getUpkeep() - ((e.getUpkeep() / 100) * plotMantenimento.get(tg.getGoal().getName())));
        }
    }

    private static void updateDatasFromConfig(){
        plotMantenimento.clear();
        for (String s : FWObbiettivi.getInstance().getConfig().getConfigurationSection("mantenimentoBonus.plotNames").getKeys(false))
            plotMantenimento.put(s, FWObbiettivi.getInstance().getConfig().getDouble("mantenimentoBonus.plotNames." + s));
    }

}
