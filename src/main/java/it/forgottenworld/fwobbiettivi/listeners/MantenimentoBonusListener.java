package it.forgottenworld.fwobbiettivi.listeners;

import com.palmergames.bukkit.towny.event.TownUpkeepCalculationEvent;
import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.TownGoal;
import it.forgottenworld.fwobbiettivi.objects.managers.TownGoals;
import it.forgottenworld.fwobbiettivi.objects.managers.Treasuries;
import it.forgottenworld.fwobbiettivi.objects.Treasury;
import me.architetto.fwfortress.fortress.FortressService;
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
        for (Treasury tes: Treasuries.getTreasuries()){
            if (e.getTown().getUuid().equals(tes.getTown().getUuid()) && !exemprion.contains(e.getTown().getName()))
                e.setUpkeep(e.getUpkeep() - ((e.getUpkeep() / 100) * plotMantenimento.get(tes.getName())));
        }

        for (TownGoal tg: TownGoals.getObbiettiviInTown()){
            if (tg.getGoal().getRewardMultiplierPlugin().equals("")) {
                if (e.getTown().getUuid().equals(tg.getTown().getUuid()) && !exemprion.contains(e.getTown().getName()) && plotMantenimento.get(tg.getGoal().getName()) != null) {
                    e.setUpkeep(e.getUpkeep() - ((e.getUpkeep() / 100) * plotMantenimento.get(tg.getGoal().getName())));
                }
            } else {
                String multiplierPluginName = tg.getGoal().getRewardMultiplierPlugin();
                switch (multiplierPluginName) {
                    case "FWFortress":
                        if (e.getTown().getUuid().equals(tg.getTown().getUuid()) && !exemprion.contains(e.getTown().getName()) && plotMantenimento.get(tg.getGoal().getName()) != null) {
                            e.setUpkeep(e.getUpkeep() - (((e.getUpkeep() / 100) * plotMantenimento.get(tg.getGoal().getName())) * FortressService.getInstance().getAmountOfFortressOwnedByTown(tg.getTown().getName())));
                        }
                        break;
                }
            }
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
    }

}
