package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.TownUpkeepCalculationEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.TownGoals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MantenimentoBonusListener implements Listener {

    private static HashMap<String, Double> plotMantenimento = new HashMap<String, Double>();

    private static List<String> exemprion = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onTownUpkeepCalculated(TownUpkeepCalculationEvent e) {
        updateDatasFromConfig();
        for (TownGoal tg: TownGoals.getObbiettiviInTown()){
            if (e.getTown().equals(tg.getTown()) && !exemprion.contains(e.getTown().getName()))
                e.setUpkeep(e.getUpkeep() - ((e.getUpkeep() / 100) * plotMantenimento.get(tg.getGoal().getName())));
        }
    }

    private static void updateDatasFromConfig(){
        plotMantenimento.clear();
        for (String s : FWObbiettivi.getInstance().getConfig().getConfigurationSection("mantenimentoBonus.plotNames").getKeys(false))
            plotMantenimento.put(s, FWObbiettivi.getInstance().getConfig().getDouble("mantenimentoBonus.plotNames." + s));

        exemprion.clear();
        List<String> list = FWObbiettivi.getInstance().getConfig().getStringList("exemprionBonus");
        if (!list.isEmpty())
            exemprion.addAll(list);

        System.out.println(exemprion.get(0));
    }

}
