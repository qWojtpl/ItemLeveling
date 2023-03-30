package pl.itemleveling;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.itemleveling.commands.Commands;
import pl.itemleveling.data.DataHandler;
import pl.itemleveling.events.Events;
import pl.itemleveling.item.ItemManager;
import pl.itemleveling.permissions.PermissionManager;

@Getter
public final class ItemLeveling extends JavaPlugin {

    private static ItemLeveling main;
    private DataHandler dataHandler;
    private PermissionManager permissionManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        main = this;
        if(getServer().getPluginManager().getPlugin("NBTAPI") == null) {
            getLogger().severe("Not found plugin NBT-API, which is required to start this plugin!");
            forceDisable();
            return;
        }
        this.itemManager = new ItemManager();
        this.dataHandler = new DataHandler();
        dataHandler.loadConfig();
        this.permissionManager = new PermissionManager();
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("itemleveling").setExecutor(new Commands());
        getLogger().info("Loaded.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye!");
    }

    public void forceDisable() {
        getServer().getPluginManager().disablePlugin(this);
    }

    public static ItemLeveling getInstance() {
        return main;
    }
}
