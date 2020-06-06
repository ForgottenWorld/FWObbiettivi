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

    // Menu Obbiettivi Cittadini
    public static String GOALS_INVENTORY_TITLE = FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + ".goals_inventory_title");
    public static int GOALS_INVENTORY_SIZE = 9;

    public static final int GOALS_NEW_ITEM_SLOT = 0;
    public static final int GOALS_EDIT_ITEM_SLOT = 1;
    public static final int GOALS_DELETE_ITEM_SLOT = 2;
    public static final int GOALS_BRANCH_ITEM_SLOT = 3;
    public static final int GOALS_CLOSE_ITEM_SLOT = 8;

    public static final String GOALS_NEW_ITEM_NAME = FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + ".goals_new_item_name");
    public static final String GOALS_EDIT_ITEM_NAME = FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + ".goals_edit_item_name");
    public static final String GOALS_DELETE_ITEM_NAME = FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + ".goals_delete_item_name");
    public static final String GOALS_BRANCH_ITEM_NAME = FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + ".goals_branch_item_name");
    public static final String GOALS_CLOSE_ITEM_NAME = FWObbiettivi.instance.getConfig().getString("languages." + getConfLang() + ".goals_close_item_name");

    public static final List<String> GOALS_NEW_ITEM_LORE = FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + ".goals_new_item_list");
    public static final List<String> GOALS_EDIT_ITEM_LORE = FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + ".goals_edit_item_list");
    public static final List<String> GOALS_DELETE_ITEM_LORE = FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + ".goals_delete_item_list");
    public static final List<String> GOALS_BRANCH_ITEM_LORE = FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + ".goals_branch_item_list");
    public static final List<String> GOALS_CLOSE_ITEM_LORE = FWObbiettivi.instance.getConfig().getStringList("languages." + getConfLang() + ".goals_close_item_list");

    public static final Material GOALS_NEW_ITEM_MATERIAL = Material.NETHER_STAR;
    public static final Material GOALS_EDIT_ITEM_MATERIAL = Material.WRITABLE_BOOK;
    public static final Material GOALS_DELETE_ITEM_MATERIAL = Material.BARRIER;
    public static final Material GOALS_BRANCH_ITEM_MATERIAL = Material.DEAD_BUSH;
    public static final Material GOALS_CLOSE_ITEM_MATERIAL = Material.RED_STAINED_GLASS_PANE;

    public static String getConfLang(){
        return FWObbiettivi.instance.getConfig().getString("languages.default");
    }

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
