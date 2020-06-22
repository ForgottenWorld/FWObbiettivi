package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIGoalsListener implements Listener {

    @EventHandler
    public void onPlayerClickGUIObbiettiviEvent(InventoryClickEvent event){
        if(event.getCurrentItem() == null){
            return;
        }

        String title = event.getView().getTitle();

        if(event.getWhoClicked().hasPermission(Permissions.PERM_GUI)) {
            if (title == GUIUtil.GOALS_INVENTORY_TITLE) {
                handleGoalsSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
                event.setCancelled(true);
            } else if (title == GUIUtil.BRANCH_INVENTORY_TITLE) {
                handleBranchSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
                event.setCancelled(true);
            } else if (title == GUIUtil.BRANCH_LIST_INVENTORY_TITLE) {
                handleBranchListSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
                event.setCancelled(true);
            } else if (title == GUIUtil.GOALS_LIST_INVENTORY_TITLE) {
                handleGoalsListSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
                event.setCancelled(true);
            }
        }

        return;
    }

    // Goals Main Menu
    private void handleGoalsSelectionGUI(Player whoClicked, Material type) {
        FWObbiettivi.instance.map.get(whoClicked).setPlayer(whoClicked);
        switch (type){
            case NETHER_STAR:   // Aggiungi
                // Apro GUI Lista Rami
                FWObbiettivi.instance.map.get(whoClicked).setAction(GoalsGUI.Action.NEW);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.GOALS_NEW_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case WRITABLE_BOOK: // Modifica
                // Apro GUI Lista Rami
                FWObbiettivi.instance.map.get(whoClicked).setAction(GoalsGUI.Action.EDIT);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.GOALS_EDIT_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case RED_STAINED_GLASS_PANE:       // Elimina
                // Apro GUI Lista Rami
                FWObbiettivi.instance.map.get(whoClicked).setAction(GoalsGUI.Action.DELETE);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.GOALS_DELETE_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case DEAD_BUSH:     // Ramo
                // Apro GUI Rami Obbiettivi
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.BRANCH_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case BARRIER:    // Chiudi
                // Chiudo la GUI Obbiettivi Cittadini
                whoClicked.closeInventory();
                FWObbiettivi.instance.map.remove(whoClicked);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Branch Main Menu
    private void handleBranchSelectionGUI(Player whoClicked, Material type) {
        FWObbiettivi.instance.map.get(whoClicked).setPlayer(whoClicked);
        switch (type){
            case NETHER_STAR:   // Aggiungi
                // Apro Modulo creazione Rami
                FWObbiettivi.instance.map.get(whoClicked).setAction(GoalsGUI.Action.NEW);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.BRANCH_NEW_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case WRITABLE_BOOK: // Modifica
                // Apro GUI Lista Rami
                FWObbiettivi.instance.map.get(whoClicked).setAction(GoalsGUI.Action.EDIT);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.BRANCH_EDIT_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case RED_STAINED_GLASS_PANE:       // Elimina
                // Apro GUI Lista Rami
                FWObbiettivi.instance.map.get(whoClicked).setAction(GoalsGUI.Action.DELETE);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(GUIUtil.BRANCH_DELETE_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case BARRIER:    // Torna indietro
                // Torno alla GUI precedente (Obbiettivi Cittadini)
                FWObbiettivi.instance.map.get(whoClicked).getSteps().remove(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                int prev = FWObbiettivi.instance.map.get(whoClicked).getSteps().get(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.instance.map.get(whoClicked).getSteps().remove(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(prev);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Branch List Menu
    private void handleBranchListSelectionGUI(Player whoClicked, Material type) {
        FWObbiettivi.instance.map.get(whoClicked).setPlayer(whoClicked);
        switch (type){
            case BARRIER:    // Torna indietro
                // Torno alla GUI precedente (Rami Obbiettivi)
                FWObbiettivi.instance.map.get(whoClicked).getSteps().remove(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                int prev = FWObbiettivi.instance.map.get(whoClicked).getSteps().get(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.instance.map.get(whoClicked).getSteps().remove(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(prev);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Goals of the branch Menu
    private void handleGoalsListSelectionGUI(Player whoClicked, Material type) {
        FWObbiettivi.instance.map.get(whoClicked).setPlayer(whoClicked);
        switch (type){
            case BARRIER:    // Torna indietro
                // Torno alla GUI precedente (Obbiettivi Cittadini)
                FWObbiettivi.instance.map.get(whoClicked).getSteps().remove(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                int prev = FWObbiettivi.instance.map.get(whoClicked).getSteps().get(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.instance.map.get(whoClicked).getSteps().remove(FWObbiettivi.instance.map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.instance.map.get(whoClicked).openGUI(prev);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }
}
