package it.forgottenworld.fwobbiettivi.listeners;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.gui.GoalsGUI;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Branches;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.prompt.BranchCreationPrompt;
import it.forgottenworld.fwobbiettivi.prompt.GoalCreationPrompt;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import it.forgottenworld.fwobbiettivi.utility.Permissions;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GUIGoalsListener implements Listener {

    @EventHandler
    public void onPlayerClickGUIObbiettiviEvent(InventoryClickEvent event){
        if(event.getCurrentItem() == null){
            return;
        }

        String title = event.getView().getTitle();

        if(event.getWhoClicked().hasPermission(Permissions.PERM_GUI)) {
            if (title.equals(GUIUtil.GOALS_INVENTORY_TITLE)) {
                handleGoalsSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
                event.setCancelled(true);
            } else if (title.equals(GUIUtil.BRANCH_INVENTORY_TITLE)) {
                handleBranchSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem().getType());
                event.setCancelled(true);
            } else if (title.equals(GUIUtil.BRANCH_LIST_INVENTORY_TITLE)) {
                handleBranchListSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem());
                event.setCancelled(true);
            } else if (title.equals(GUIUtil.GOALS_LIST_INVENTORY_TITLE)) {
                handleGoalsListSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem());
                event.setCancelled(true);
            } else if (Goals.getObbiettivi().stream().anyMatch(p -> p.getName().equals(title))) {
                handleGoalInfoSelectionGUI((Player) event.getWhoClicked(), event.getCurrentItem(), title);
                event.setCancelled(true);
            }
        }
    }

    // Goals Main Menu
    private void handleGoalsSelectionGUI(Player player, Material type) {
        UUID whoClicked = player.getUniqueId();
        FWObbiettivi.getInstance().map.get(whoClicked).setPlayer(player);
        switch (type){
            case NETHER_STAR: // Aggiungi
                // Apro GUI Lista Rami
                FWObbiettivi.getInstance().map.remove(whoClicked);
                player.playSound( player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                player.closeInventory();

                GoalCreationPrompt creationPrompt = new GoalCreationPrompt(FWObbiettivi.getPlugin(FWObbiettivi.class));
                creationPrompt.startConversationForPlayer(player);
                break;
            case WRITABLE_BOOK: // Modifica
                // Apro GUI Lista Rami
                FWObbiettivi.getInstance().map.get(whoClicked).setAction(GoalsGUI.Action.EDIT);
                FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.GOALS_EDIT_STEP);
                player.playSound( player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case RED_STAINED_GLASS_PANE: // Elimina
                // Apro GUI Lista Rami
                FWObbiettivi.getInstance().map.get(whoClicked).setAction(GoalsGUI.Action.DELETE);
                FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.GOALS_DELETE_STEP);
                player.playSound( player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case DEAD_BUSH: // Ramo
                // Apro GUI Rami Obbiettivi
                FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.BRANCH_STEP);
                player.playSound( player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1 );
                break;
            case BARRIER: // Chiudi
                // Chiudo la GUI Obbiettivi Cittadini
                player.closeInventory();
                FWObbiettivi.getInstance().map.remove(whoClicked);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Branch Main Menu
    private void handleBranchSelectionGUI(Player player, Material type) {
        UUID whoClicked = player.getUniqueId();
        FWObbiettivi.getInstance().map.get(whoClicked).setPlayer(player);
        switch (type){
            case NETHER_STAR: // Aggiungi
                // Apro Modulo creazione Rami
                FWObbiettivi.getInstance().map.remove(whoClicked);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                player.closeInventory();

                BranchCreationPrompt creationPrompt = new BranchCreationPrompt(FWObbiettivi.getPlugin(FWObbiettivi.class));
                creationPrompt.startConversationForPlayer(player);
                break;
            case RED_STAINED_GLASS_PANE: // Elimina
                // Apro GUI Lista Rami
                FWObbiettivi.getInstance().map.get(whoClicked).setAction(GoalsGUI.Action.DELETE);
                FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.BRANCH_DELETE_STEP);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case BARRIER: // Torna indietro
                // Torno alla GUI precedente (Obbiettivi Cittadini)
                FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
                int prev = FWObbiettivi.getInstance().map.get(whoClicked).getSteps().get(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1).intValue();
                FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.getInstance().map.get(whoClicked).openGUI(prev);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }

    // Branch List Menu
    private void handleBranchListSelectionGUI(Player player, ItemStack item) {
        UUID whoClicked = player.getUniqueId();
        FWObbiettivi.getInstance().map.get(whoClicked).setPlayer(player);

        if (item.getType().equals(Material.BARRIER)) {
            // Torna indietro
            // Torno alla GUI precedente (Rami Obbiettivi) o (Obbiettivi Cittadini)
            FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
            int prev = FWObbiettivi.getInstance().map.get(whoClicked).getSteps().get(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1).intValue();
            FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
            FWObbiettivi.getInstance().map.get(whoClicked).openGUI(prev);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
        } else {
            int prev = FWObbiettivi.getInstance().map.get(whoClicked).getSteps().get(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 2).intValue();

            if (prev == GUIUtil.GOALS_STEP) {
                for (Branch b : Branches.getRami()) {
                    if (b.getIcon().getType().equals(item.getType())) {
                        FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.GOALS_LIST_STEP, b);
                        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                    }
                }
            }
        }
    }

    // Goals of the branch Menu
    private void handleGoalsListSelectionGUI(Player player, ItemStack item) {
        UUID whoClicked = player.getUniqueId();
        FWObbiettivi.getInstance().map.get(whoClicked).setPlayer(player);

        if (item.getType().equals(Material.BARRIER)) {
            // Torna indietro
            // Torno alla GUI precedente (Lista Rami)
            FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
            int prev = FWObbiettivi.getInstance().map.get(whoClicked).getSteps().get(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1).intValue();
            FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
            FWObbiettivi.getInstance().map.get(whoClicked).openGUI(prev);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
        } else {
            for (Goal g : Goals.getGoalsFromBranch(Goals.getGoalFromString(item.getItemMeta().getDisplayName()).getBranch())) {
                if (g.getName().equals(item.getItemMeta().getDisplayName())) {
                    FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.GOAL_INFO_STEP, g);
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                }
            }
        }
    }

    // Goal Info
    private void handleGoalInfoSelectionGUI(Player player, ItemStack item, String name) {
        UUID whoClicked = player.getUniqueId();
        FWObbiettivi.getInstance().map.get(whoClicked).setPlayer(player);

        switch (item.getType()){
            case COMPASS:

                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case CHEST:

                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case GOLD_BLOCK:

                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                break;
            case BARRIER:
                // Torna indietro
                // Torno alla GUI precedente (Lista Obbiettivi in Ramo)
                FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.getInstance().map.get(whoClicked).getSteps().remove(FWObbiettivi.getInstance().map.get(whoClicked).getSteps().size() - 1);
                FWObbiettivi.getInstance().map.get(whoClicked).openGUI(GUIUtil.GOALS_LIST_STEP, Goals.getGoalFromString(name).getBranch());
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 10, 1);
                break;
        }
    }
}
