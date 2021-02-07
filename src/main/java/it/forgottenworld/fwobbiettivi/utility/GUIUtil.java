package it.forgottenworld.fwobbiettivi.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIUtil {

    // Steps
    public static final int GOALS_STEP = 1; // Goal Menu

    public static final int GOALS_EDIT_STEP = 2;
    public static final int GOALS_DELETE_STEP = 3;

    public static final int BRANCH_STEP = 4; // Branch Menu

    public static final int BRANCH_EDIT_STEP = 5;
    public static final int BRANCH_DELETE_STEP = 6;

    public static final int BRANCH_LIST_STEP = 7;
    public static final int GOALS_LIST_STEP = 8;

    public static final int GOAL_INFO_STEP = 9;

    // Menu Obbiettivi Cittadini
    public static String GOALS_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("gui.goals_inventory_title");
    public static int GOALS_INVENTORY_SIZE = 9;

    public static final int GOALS_NEW_ITEM_SLOT = 0;
    public static final int GOALS_EDIT_ITEM_SLOT = 1;
    public static final int GOALS_DELETE_ITEM_SLOT = 2;
    public static final int GOALS_BRANCH_ITEM_SLOT = 3;
    public static final int GOALS_CLOSE_ITEM_SLOT = 8;

    public static final String GOALS_NEW_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_new_item_name");
    public static final String GOALS_EDIT_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_edit_item_name");
    public static final String GOALS_DELETE_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_delete_item_name");
    public static final String GOALS_BRANCH_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_branch_item_name");
    public static final String GOALS_CLOSE_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_close_item_name");

    public static final List<String> GOALS_NEW_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_new_item_list");
    public static final List<String> GOALS_EDIT_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_edit_item_list");
    public static final List<String> GOALS_DELETE_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_delete_item_list");
    public static final List<String> GOALS_BRANCH_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_branch_item_list");
    public static final List<String> GOALS_CLOSE_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_close_item_list");

    public static final Material GOALS_NEW_ITEM_MATERIAL = Material.NETHER_STAR;
    public static final Material GOALS_EDIT_ITEM_MATERIAL = Material.WRITABLE_BOOK;
    public static final Material GOALS_DELETE_ITEM_MATERIAL = Material.RED_STAINED_GLASS_PANE;
    public static final Material GOALS_BRANCH_ITEM_MATERIAL = Material.DEAD_BUSH;
    public static final Material GOALS_CLOSE_ITEM_MATERIAL = Material.BARRIER;

    // Menu Edit Obbiettivo
    public static final int GOAL_INFO_INVENTORY_SIZE = 27;

    public static final int GOALS_REQUIRED_OBJ_SLOT = 0;
    public static final int GOALS_PAYMENT_OBJ_SLOT = 1;
    public static final int GOALS_REWARD_OBJ_SLOT = 2;
    public static final int GOALS_BACK_ITEM_SLOT = 26;

    public static final String GOALS_REQUIRED_OBJ_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_required_obj_item_name");
    public static final String GOALS_PAYMENT_OBJ_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_payment_obj_item_name");
    public static final String GOALS_REWARD_OBJ_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_reward_obj_item_name");
    public static final String GOALS_INFO_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_info_back_item_name");

    public static final List<String> GOALS_REQUIRED_OBJ_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_required_obj_item_list");
    public static final List<String> GOALS_PAYMENT_OBJ_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_payment_obj_item_list");
    public static final List<String> GOALS_REWARD_OBJ_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_reward_obj_item_list");
    public static final List<String> GOALS_INFO_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_info_back_item_list");

    public static final Material GOALS_REQUIRED_OBJ_ITEM_MATERIAL = Material.COMPASS;
    public static final Material GOALS_PAYMENT_OBJ_ITEM_MATERIAL = Material.CHEST;
    public static final Material GOALS_REWARD_OBJ_ITEM_MATERIAL = Material.GOLD_BLOCK;
    public static final Material GOALS_INFO_BACK_ITEM_MATERIAL = Material.BARRIER;

    // Menu Rami Obbiettivi
    public static String BRANCH_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("gui.branch_inventory_title");
    public static int BRANCH_INVENTORY_SIZE = 9;

    public static final int BRANCH_NEW_ITEM_SLOT = 0;
    public static final int BRANCH_DELETE_ITEM_SLOT = 1;
    public static final int BRANCH_BACK_ITEM_SLOT = 8;

    public static final String BRANCH_NEW_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.branch_new_item_name");
    public static final String BRANCH_DELETE_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.branch_delete_item_name");
    public static final String BRANCH_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.branch_back_item_name");

    public static final List<String> BRANCH_NEW_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.branch_new_item_list");
    public static final List<String> BRANCH_DELETE_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.branch_delete_item_list");
    public static final List<String> BRANCH_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.branch_back_item_list");

    public static final Material BRANCH_NEW_ITEM_MATERIAL = Material.NETHER_STAR;
    public static final Material BRANCH_EDIT_ITEM_MATERIAL = Material.WRITABLE_BOOK;
    public static final Material BRANCH_DELETE_ITEM_MATERIAL = Material.RED_STAINED_GLASS_PANE;
    public static final Material BRANCH_BACK_ITEM_MATERIAL = Material.BARRIER;

    // Menu Lista Rami
    public static final String BRANCH_LIST_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("gui.branch_list_inventory_title");
    public static int BRANCH_LIST_INVENTORY_SIZE = 27;

    public static final int BRANCH_LIST_BACK_ITEM_SLOT = 26;

    public static final String BRANCH_LIST_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.branch_list_back_item_name");

    public static final List<String> BRANCH_LIST_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.branch_list_back_item_list");

    public static final Material BRANCH_LIST_BACK_ITEM_MATERIAL = Material.BARRIER;

    //Menu Lista Obbiettivi in un Ramo
    public static final String GOALS_LIST_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("gui.goals_list_inventory_title");
    public static int GOALS_LIST_INVENTORY_SIZE = 54;

    public static final int GOALS_LIST_BACK_ITEM_SLOT = 53;

    public static final String GOALS_LIST_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("gui.goals_list_back_item_name");

    public static final List<String> GOALS_LIST_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("gui.goals_list_back_item_list");

    public static final Material GOALS_LIST_BACK_ITEM_MATERIAL = Material.BARRIER;

    public static ItemStack prepareMenuPoint(Material material, String displayName, List<String> lore) {
        ItemStack menuPoint = new ItemStack(material);

        ItemMeta menuPointMeta = menuPoint.getItemMeta();
        menuPointMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        menuPointMeta.setDisplayName(displayName);
        menuPointMeta.setLore(lore);

        menuPoint.setItemMeta(menuPointMeta);

        return menuPoint;
    }

}
