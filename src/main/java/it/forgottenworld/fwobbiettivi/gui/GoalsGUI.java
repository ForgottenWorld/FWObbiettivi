package it.forgottenworld.fwobbiettivi.gui;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Iterator;

public class GoalsGUI {

    public enum Action {
        NEW,
        EDIT,
        DELETE
    }

    private Player player;
    private Action action;
    private boolean firstStep;

    public GoalsGUI(Player player){
        this.player = player;
    }

    public GoalsGUI(Player player, Action action, boolean firstStep){
        this.player = player;
        this.action = action;
        this.firstStep = firstStep;
    }

    public void openGUI(int step){
        switch (step){
            case GUIUtil.GOALS_STEP:
                openGoalsGUI();
                break;
            case GUIUtil.GOALS_NEW_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.GOALS_EDIT_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.GOALS_DELETE_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.BRANCH_NEW_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.BRANCH_EDIT_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.BRANCH_DELETE_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.BRANCH_STEP:
                openBranchGUI();
                break;
        }
    }

    private void openGoalsGUI() {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOALS_INVENTORY_SIZE, GUIUtil.GOALS_INVENTORY_TITLE);

        GUI.setItem(GUIUtil.GOALS_NEW_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_NEW_ITEM_MATERIAL,
                        GUIUtil.GOALS_NEW_ITEM_NAME,
                        GUIUtil.GOALS_NEW_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.GOALS_EDIT_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_EDIT_ITEM_MATERIAL,
                        GUIUtil.GOALS_EDIT_ITEM_NAME,
                        GUIUtil.GOALS_EDIT_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.GOALS_DELETE_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_DELETE_ITEM_MATERIAL,
                        GUIUtil.GOALS_DELETE_ITEM_NAME,
                        GUIUtil.GOALS_DELETE_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.GOALS_BRANCH_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_BRANCH_ITEM_MATERIAL,
                        GUIUtil.GOALS_BRANCH_ITEM_NAME,
                        GUIUtil.GOALS_BRANCH_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.GOALS_CLOSE_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_CLOSE_ITEM_MATERIAL,
                        GUIUtil.GOALS_CLOSE_ITEM_NAME,
                        GUIUtil.GOALS_CLOSE_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

    private void openGoalsListGUI() {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOALS_LIST_INVENTORY_SIZE, GUIUtil.GOALS_LIST_INVENTORY_TITLE);

        for(int i = 0; i < FWObbiettivi.instance.obbiettivi.size(); i++){
            // Creo item per ogni ramo presente nell'array
            Goal obbiettivo = FWObbiettivi.instance.obbiettivi.get(i);
            GUI.setItem(i,
                    GUIUtil.prepareMenuPoint(
                            obbiettivo.getBranch().getMaterial(),
                            obbiettivo.getName(),
                            obbiettivo.getDescrizione()
                    ));
        }

        GUI.setItem(GUIUtil.GOALS_LIST_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_LIST_BACK_ITEM_MATERIAL,
                        GUIUtil.GOALS_LIST_BACK_ITEM_NAME,
                        GUIUtil.GOALS_LIST_BACK_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

    private void openBranchGUI() {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.BRANCH_INVENTORY_SIZE, GUIUtil.BRANCH_INVENTORY_TITLE);

        GUI.setItem(GUIUtil.BRANCH_NEW_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.BRANCH_NEW_ITEM_MATERIAL,
                        GUIUtil.BRANCH_NEW_ITEM_NAME,
                        GUIUtil.BRANCH_NEW_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.BRANCH_EDIT_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.BRANCH_EDIT_ITEM_MATERIAL,
                        GUIUtil.BRANCH_EDIT_ITEM_NAME,
                        GUIUtil.BRANCH_EDIT_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.BRANCH_DELETE_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.BRANCH_DELETE_ITEM_MATERIAL,
                        GUIUtil.BRANCH_DELETE_ITEM_NAME,
                        GUIUtil.BRANCH_DELETE_ITEM_LORE
                ));
        GUI.setItem(GUIUtil.BRANCH_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.BRANCH_BACK_ITEM_MATERIAL,
                        GUIUtil.BRANCH_BACK_ITEM_NAME,
                        GUIUtil.BRANCH_BACK_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

    private void openBranchListGUI() {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.BRANCH_LIST_INVENTORY_SIZE, GUIUtil.BRANCH_LIST_INVENTORY_TITLE);

        for(int i = 0; i < FWObbiettivi.instance.rami.size(); i++){
            // Creo item per ogni ramo presente nell'array
            Branch ramo = FWObbiettivi.instance.rami.get(i);
            GUI.setItem(i,
                    GUIUtil.prepareMenuPoint(
                            ramo.getMaterial(),
                            ramo.getName(),
                            ramo.getDescrizione()
                    ));
        }

        GUI.setItem(GUIUtil.BRANCH_LIST_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.BRANCH_LIST_BACK_ITEM_MATERIAL,
                        GUIUtil.BRANCH_LIST_BACK_ITEM_NAME,
                        GUIUtil.BRANCH_LIST_BACK_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

}
