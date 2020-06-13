package it.forgottenworld.fwobbiettivi.utility;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIUtil {

    // Steps
    public static final int GOALS_STEP = 1;
    public static final int GOALS_NEW_STEP = 2;
    public static final int GOALS_EDIT_STEP = 3;
    public static final int GOALS_DELETE_STEP = 4;
    public static final int BRANCH_STEP = 5;

    public static final int BRANCH_NEW_STEP = 6;
    public static final int BRANCH_EDIT_STEP = 7;
    public static final int BRANCH_DELETE_STEP = 8;

    public static final int BRANCH_LIST_STEP = 9;
    public static final int GOALS_LIST_STEP = 10;

    // Menu Obbiettivi Cittadini
    public static String GOALS_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("goals_inventory_title");
    public static int GOALS_INVENTORY_SIZE = 9;

    public static final int GOALS_NEW_ITEM_SLOT = 0;
    public static final int GOALS_EDIT_ITEM_SLOT = 1;
    public static final int GOALS_DELETE_ITEM_SLOT = 2;
    public static final int GOALS_BRANCH_ITEM_SLOT = 3;
    public static final int GOALS_CLOSE_ITEM_SLOT = 8;

    public static final String GOALS_NEW_ITEM_NAME = ConfigUtil.getConfigStringLang("goals_new_item_name");
    public static final String GOALS_EDIT_ITEM_NAME = ConfigUtil.getConfigStringLang("goals_edit_item_name");
    public static final String GOALS_DELETE_ITEM_NAME = ConfigUtil.getConfigStringLang("goals_delete_item_name");
    public static final String GOALS_BRANCH_ITEM_NAME = ConfigUtil.getConfigStringLang("goals_branch_item_name");
    public static final String GOALS_CLOSE_ITEM_NAME = ConfigUtil.getConfigStringLang("goals_close_item_name");

    public static final List<String> GOALS_NEW_ITEM_LORE = ConfigUtil.getConfigStringListLang("goals_new_item_list");
    public static final List<String> GOALS_EDIT_ITEM_LORE = ConfigUtil.getConfigStringListLang("goals_edit_item_list");
    public static final List<String> GOALS_DELETE_ITEM_LORE = ConfigUtil.getConfigStringListLang("goals_delete_item_list");
    public static final List<String> GOALS_BRANCH_ITEM_LORE = ConfigUtil.getConfigStringListLang("goals_branch_item_list");
    public static final List<String> GOALS_CLOSE_ITEM_LORE = ConfigUtil.getConfigStringListLang("goals_close_item_list");

    public static final Material GOALS_NEW_ITEM_MATERIAL = Material.NETHER_STAR;
    public static final Material GOALS_EDIT_ITEM_MATERIAL = Material.WRITABLE_BOOK;
    public static final Material GOALS_DELETE_ITEM_MATERIAL = Material.RED_STAINED_GLASS_PANE;
    public static final Material GOALS_BRANCH_ITEM_MATERIAL = Material.DEAD_BUSH;
    public static final Material GOALS_CLOSE_ITEM_MATERIAL = Material.BARRIER;

    // Menu New Obbiettivo

    // Menu Edit Obbiettivo

    // Menu Delete Obbiettivo

    // Menu Rami Obbiettivi
    public static String BRANCH_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("branch_inventory_title");
    public static int BRANCH_INVENTORY_SIZE = 9;

    public static final int BRANCH_NEW_ITEM_SLOT = 0;
    public static final int BRANCH_EDIT_ITEM_SLOT = 1;
    public static final int BRANCH_DELETE_ITEM_SLOT = 2;
    public static final int BRANCH_BACK_ITEM_SLOT = 8;

    public static final String BRANCH_NEW_ITEM_NAME = ConfigUtil.getConfigStringLang("branch_new_item_name");
    public static final String BRANCH_EDIT_ITEM_NAME = ConfigUtil.getConfigStringLang("branch_edit_item_name");
    public static final String BRANCH_DELETE_ITEM_NAME = ConfigUtil.getConfigStringLang("branch_delete_item_name");
    public static final String BRANCH_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("branch_back_item_name");

    public static final List<String> BRANCH_NEW_ITEM_LORE = ConfigUtil.getConfigStringListLang("branch_new_item_list");
    public static final List<String> BRANCH_EDIT_ITEM_LORE = ConfigUtil.getConfigStringListLang("branch_edit_item_list");
    public static final List<String> BRANCH_DELETE_ITEM_LORE = ConfigUtil.getConfigStringListLang("branch_delete_item_list");
    public static final List<String> BRANCH_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("branch_back_item_list");

    public static final Material BRANCH_NEW_ITEM_MATERIAL = Material.NETHER_STAR;
    public static final Material BRANCH_EDIT_ITEM_MATERIAL = Material.WRITABLE_BOOK;
    public static final Material BRANCH_DELETE_ITEM_MATERIAL = Material.RED_STAINED_GLASS_PANE;
    public static final Material BRANCH_BACK_ITEM_MATERIAL = Material.BARRIER;

    // Menu New Ramo

    // Menu Edit Ramo

    // Menu Delete Ramo

    // Menu Lista Rami
    public static final String BRANCH_LIST_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("branch_list_inventory_title");
    public static int BRANCH_LIST_INVENTORY_SIZE = 18;

    public static final int BRANCH_LIST_BACK_ITEM_SLOT = 17;

    public static final String BRANCH_LIST_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("branch_back_item_name");

    public static final List<String> BRANCH_LIST_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("branch_back_item_list");

    public static final Material BRANCH_LIST_BACK_ITEM_MATERIAL = Material.BARRIER;

    //Menu Lista Obbiettivi in un Ramo
    public static final String GOALS_LIST_INVENTORY_TITLE = ConfigUtil.getConfigStringLang("goals_list_inventory_title");
    public static int GOALS_LIST_INVENTORY_SIZE = 54;

    public static final int GOALS_LIST_BACK_ITEM_SLOT = 53;

    public static final String GOALS_LIST_BACK_ITEM_NAME = ConfigUtil.getConfigStringLang("branch_back_item_name");

    public static final List<String> GOALS_LIST_BACK_ITEM_LORE = ConfigUtil.getConfigStringListLang("branch_back_item_list");

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
