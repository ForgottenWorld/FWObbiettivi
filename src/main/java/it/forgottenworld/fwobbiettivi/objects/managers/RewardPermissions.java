package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class RewardPermissions {

    private HashMap <UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    public void addPermission(Player player, String perm){
        if (!perms.containsKey(player.getUniqueId())) {
            PermissionAttachment attachment = player.addAttachment(FWObbiettivi.getInstance());
            perms.put(player.getUniqueId(), attachment);
        }

        PermissionAttachment pperms = perms.get(player.getUniqueId());
        pperms.setPermission(perm, true);
    }

    public void removePermission(Player player, String perm){
        perms.get(player.getUniqueId()).unsetPermission(perm);
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public void save(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        database.getFile().set("permissions", null);



        database.saveFile();
    }

    public void load(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        perms.clear();

        if (database.getFile().getConfigurationSection("permissions") != null) {

        }
    }

}
