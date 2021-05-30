package it.forgottenworld.fwobbiettivi.gui;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.objects.Branch;
import it.forgottenworld.fwobbiettivi.objects.managers.Branches;
import it.forgottenworld.fwobbiettivi.objects.Goal;
import it.forgottenworld.fwobbiettivi.objects.managers.Goals;
import it.forgottenworld.fwobbiettivi.utility.GUIUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoalsGUI {

    public enum Action {
        NEW,
        EDIT,
        DELETE
    }

    private Player player;
    private Action action;
    private List<BigDecimal> steps;

    public String goalName;

    public GoalsGUI(){
        this.steps = new ArrayList<BigDecimal>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<BigDecimal> getSteps() {
        return steps;
    }

    public void setSteps(List<BigDecimal> steps) {
        this.steps = steps;
    }

    /**
     *
     * @param step
     */
    public void openGUI(int step){
        steps.add(BigDecimal.valueOf(step));
        switch (step){
            case GUIUtil.GOALS_STEP:
                openGoalsGUI();
                break;
            case GUIUtil.GOALS_EDIT_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.GOALS_DELETE_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.BRANCH_STEP:
                openBranchGUI();
                break;
            case GUIUtil.BRANCH_DELETE_STEP:
                openBranchListGUI();
                break;
            case GUIUtil.BRANCH_LIST_STEP:
                openBranchGUI();
                break;
            case GUIUtil.GOALS_LIST_STEP:
                openBranchGUI();
                break;
        }
    }

    public void openGUI(int step, Branch b){
        steps.add(BigDecimal.valueOf(step));
        openGoalsListGUI(b);
    }

    public void openGUI(int step, Goal g){
        steps.add(BigDecimal.valueOf(step));
        switch (step) {
            case GUIUtil.GOAL_INFO_STEP:
                openGoalInfoGUI(g);
                break;
            case GUIUtil.GOAL_INFO_REQ_STEP:
                openGeneratedRequiredGUI(g);
                break;
            case GUIUtil.GOAL_INFO_PAY_STEP:
                openGeneratedPaymentGUI(g);
                break;
            case GUIUtil.GOAL_INFO_REW_STEP:
                openGeneratedRewardGUI(g);
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

    private void openGoalsListGUI(Branch branch) {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOALS_LIST_INVENTORY_SIZE, GUIUtil.GOALS_LIST_INVENTORY_TITLE);

        for(int i = 0; i < Goals.getGoalsFromBranch(branch).size(); i++){
            // Creo item per ogni ramo presente nell'array
            Goal obbiettivo = Goals.getGoalsFromBranch(branch).get(i);
            GUI.setItem(i,
                    GUIUtil.prepareMenuPoint(
                            obbiettivo.getBranch().getIcon().getType(),
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

    private void openGoalInfoGUI(Goal g) {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOAL_INFO_INVENTORY_SIZE, g.getName());

        GUI.setItem(GUIUtil.GOALS_REQUIRED_OBJ_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_REQUIRED_OBJ_ITEM_MATERIAL,
                        GUIUtil.GOALS_REQUIRED_OBJ_ITEM_NAME,
                        GUIUtil.GOALS_REQUIRED_OBJ_ITEM_LORE
                ));

        GUI.setItem(GUIUtil.GOALS_PAYMENT_OBJ_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_PAYMENT_OBJ_ITEM_MATERIAL,
                        GUIUtil.GOALS_PAYMENT_OBJ_ITEM_NAME,
                        GUIUtil.GOALS_PAYMENT_OBJ_ITEM_LORE
                ));

        GUI.setItem(GUIUtil.GOALS_REWARD_OBJ_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_REWARD_OBJ_ITEM_MATERIAL,
                        GUIUtil.GOALS_REWARD_OBJ_ITEM_NAME,
                        GUIUtil.GOALS_REWARD_OBJ_ITEM_LORE
                ));

        GUI.setItem(GUIUtil.GOALS_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_INFO_BACK_ITEM_MATERIAL,
                        GUIUtil.GOALS_INFO_BACK_ITEM_NAME,
                        GUIUtil.GOALS_INFO_BACK_ITEM_LORE
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

        for(int i = 0; i < Branches.getRami().size(); i++){
            // Creo item per ogni ramo presente nell'array
            Branch ramo = Branches.getRami().get(i);
            GUI.setItem(i,
                    GUIUtil.prepareMenuPoint(
                            ramo.getIcon().getType(),
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

    private void openGeneratedRequiredGUI(Goal goal) {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOAL_INFO_INVENTORY_SIZE, goal.getName());
        int position = 0;

        if (!goal.getRequiredObjects().isEmpty()) {
            for (ItemStack is : goal.getRequiredObjects()) {
                GUI.setItem(position, is);
                position++;
            }
        }

        GUI.setItem(GUIUtil.GOALS_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_INFO_BACK_ITEM_MATERIAL,
                        GUIUtil.GOALS_INFO_BACK_ITEM_NAME,
                        GUIUtil.GOALS_INFO_BACK_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

    private void openGeneratedPaymentGUI(Goal goal) {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOAL_INFO_INVENTORY_SIZE, goal.getName());
        int position = 0;

        if (!goal.getPayment().isEmpty()) {
            for (ItemStack is : goal.getPayment()) {
                GUI.setItem(position, is);
                position++;
            }
        }

        GUI.setItem(GUIUtil.GOALS_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_INFO_BACK_ITEM_MATERIAL,
                        GUIUtil.GOALS_INFO_BACK_ITEM_NAME,
                        GUIUtil.GOALS_INFO_BACK_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

    private void openGeneratedRewardGUI(Goal goal) {
        Inventory GUI = Bukkit.createInventory(null, GUIUtil.GOAL_INFO_INVENTORY_SIZE, goal.getName());
        int position = 0;

        if (!goal.getReward().isEmpty()) {
            for (ItemStack is : goal.getReward()) {
                GUI.setItem(position, is);
                position++;
            }
        }

        GUI.setItem(GUIUtil.GOALS_BACK_ITEM_SLOT,
                GUIUtil.prepareMenuPoint(
                        GUIUtil.GOALS_INFO_BACK_ITEM_MATERIAL,
                        GUIUtil.GOALS_INFO_BACK_ITEM_NAME,
                        GUIUtil.GOALS_INFO_BACK_ITEM_LORE
                ));

        player.openInventory(GUI);
    }

}
