package it.forgottenworld.fwobbiettivi.objects.managers;

import it.forgottenworld.fwobbiettivi.FWObbiettivi;
import it.forgottenworld.fwobbiettivi.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class RewardPermissions {

    private static HashMap <UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

    // FIXME
    public static void addPermission(Player player, String perm){
        if (player == null)
            return;

        if (!perms.containsKey(player.getUniqueId())) {
            PermissionAttachment attachment = player.addAttachment(FWObbiettivi.getPlugin(FWObbiettivi.class));
            perms.put(player.getUniqueId(), attachment);
        }

        PermissionAttachment pperms = perms.get(player.getUniqueId());
        pperms.setPermission(perm, true);

        save();
    }

    public static void removePermission(Player player, String perm){
        if (player == null)
            return;

        perms.get(player.getUniqueId()).unsetPermission(perm);

        save();
    }

    public static HashMap<UUID, PermissionAttachment> getPerms() {
        return perms;
    }

    /*
     * ==================================================================================
     * 										Save & Load
     * ==================================================================================
     */

    public static void save(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        database.getFile().set("permissions", null);

        for (Map.Entry<UUID, PermissionAttachment> p : getPerms().entrySet()) {
            String path = "permissions." + p.getKey();
            List<String> perms = new ArrayList<>();

            for (Map.Entry<String, Boolean> m : p.getValue().getPermissions().entrySet()) {

                if (m.getValue()) {
                    perms.add(m.getKey());
                } else {
                    perms.add("- " + m.getKey());
                }
//                List<String> coord = new ArrayList<>();
//                List<String> app = database.getFile().getStringList(path);
//
//                if (!app.isEmpty()) {
//                    coord.addAll(app);
//                }
//
//                if (!coord.contains(c.getKey().getX() + ";" + c.getKey().getZ()))
//                    coord.add(c.getKey().getX() + ";" + c.getKey().getZ());
//
//                database.getFile().set(path, coord);
            }
            database.getFile().set(path, perms);
        }

        database.saveFile();
    }

    public static void load(){
        ConfigManager database = FWObbiettivi.getInstance().getDatabase();
        perms.clear();

        if (database.getFile().getConfigurationSection("permissions") != null) {

        }
    }

}
