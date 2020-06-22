package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
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

        if(title == GUIUtil.GOALS_INVENTORY_TITLE){
            handleGoalsSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
            event.setCancelled(true);
        }else if(title == GUIUtil.BRANCH_INVENTORY_TITLE){
            handleBranchSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
            event.setCancelled(true);
        }else if(title == GUIUtil.BRANCH_LIST_INVENTORY_TITLE){
            handleBranchListSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
            event.setCancelled(true);
        }else if(title == GUIUtil.GOALS_LIST_INVENTORY_TITLE){
            handleGoalsListSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
            event.setCancelled(true);
        }

        return;
    }

    // Goals Main Menu
    private void handleGoalsSelectionGUI(Player whoClicked, Material type) {
        switch (type){
            case NETHER_STAR:   // Aggiungi
                // Apro GUI Lista Rami
                GoalsGUI newObGUI = new GoalsGUI(whoClicked, GoalsGUI.Action.NEW, false);
                newObGUI.openGUI(GUIUtil.GOALS_NEW_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case WRITABLE_BOOK: // Modifica
                // Apro GUI Lista Rami
                GoalsGUI editObGUI = new GoalsGUI(whoClicked, GoalsGUI.Action.EDIT, false);
                editObGUI.openGUI(GUIUtil.GOALS_EDIT_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case RED_STAINED_GLASS_PANE:       // Elimina
                // Apro GUI Lista Rami
                GoalsGUI deleteObGUI = new GoalsGUI(whoClicked, GoalsGUI.Action.DELETE, false);
                deleteObGUI.openGUI(GUIUtil.GOALS_DELETE_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case DEAD_BUSH:     // Ramo
                // Apro GUI Rami Obbiettivi
                GoalsGUI branch = new GoalsGUI(whoClicked);
                branch.openGUI(GUIUtil.BRANCH_STEP);
                whoClicked.playSound( whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case BARRIER:    // Chiudi
                // Chiudo la GUI Obbiettivi Cittadini
                whoClicked.closeInventory();
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Branch Main Menu
    private void handleBranchSelectionGUI(Player whoClicked, Material type) {
        switch (type){
            case NETHER_STAR:   // Aggiungi
                // Apro Modulo creazione Rami
                GoalsGUI newObGUI = new GoalsGUI(whoClicked, GoalsGUI.Action.NEW, false);
                newObGUI.openGUI(GUIUtil.BRANCH_NEW_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case WRITABLE_BOOK: // Modifica
                // Apro GUI Lista Rami
                GoalsGUI editObGUI = new GoalsGUI(whoClicked, GoalsGUI.Action.EDIT, false);
                editObGUI.openGUI(GUIUtil.BRANCH_EDIT_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case RED_STAINED_GLASS_PANE:       // Elimina
                // Apro GUI Lista Rami
                GoalsGUI deleteObGUI = new GoalsGUI(whoClicked, GoalsGUI.Action.DELETE, false);
                deleteObGUI.openGUI(GUIUtil.BRANCH_DELETE_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case BARRIER:    // Torna indietro
                // Torno alla GUI precedente (Obbiettivi Cittadini)
                GoalsGUI backObGUI = new GoalsGUI(whoClicked);
                backObGUI.openGUI(GUIUtil.GOALS_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Branch List Menu
    private void handleBranchListSelectionGUI(Player whoClicked, Material type) {
        switch (type){
            case BARRIER:    // Torna indietro
                // Torno alla GUI precedente (Rami Obbiettivi)
                GoalsGUI backObGUI = new GoalsGUI(whoClicked);
                backObGUI.openGUI(GUIUtil.BRANCH_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Goals of the branch Menu
    private void handleGoalsListSelectionGUI(Player whoClicked, Material type) {
        switch (type){
            case BARRIER:    // Torna indietro
                // Torno alla GUI precedente (Obbiettivi Cittadini)
                GoalsGUI backObGUI = new GoalsGUI(whoClicked);
                backObGUI.openGUI(GUIUtil.BRANCH_LIST_STEP);
                whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }
}
