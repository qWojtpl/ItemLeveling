package pl.itemleveling.permissions;

import org.bukkit.permissions.Permission;
import pl.itemleveling.ItemLeveling;

import java.util.HashMap;

public class PermissionManager {

    private final HashMap<String, Permission> permissions = new HashMap<>();

    public PermissionManager() {
        registerPermission("itemleveling.manage", "Manage item leveling plugin");
    }

    public void registerPermission(String permission, String description) {
        Permission perm = new Permission(permission, description);
        ItemLeveling.getInstance().getServer().getPluginManager().addPermission(perm);
        permissions.put(permission, perm);
    }

    public Permission getPermission(String permission) {
        return permissions.getOrDefault(permission, null);
    }

}
