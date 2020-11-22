package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import it.forgottenworld.townycustomupkeep.api.events.CustomTownUpkeepCalculationEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class MantenimentoBonusListener implements Listener {

    private static HashMap<String, Double> plotMantenimento = new HashMap<String, Double>();

    @EventHandler
    public void onCustomTownyUpkeeps(CustomTownUpkeepCalculationEvent e){
        updateDatasFromConfig();
        for (TownGoals tg: FWObbiettivi.getInstance().obbiettiviInTown){
            e.setUpkeep((plotMantenimento.get(tg.getGoal().getName()) * 100) / e.getUpkeep());
        }
    }

    private static void updateDatasFromConfig(){
        plotMantenimento.clear();
        for (String s : FWObbiettivi.getInstance().getConfig().getConfigurationSection("mantenimentoBonus.plotNames").getKeys(false))
            plotMantenimento.put(s.toLowerCase(), FWObbiettivi.getInstance().getConfig().getDouble("mantenimentoBonus.plotNames." + s));
    }

}
