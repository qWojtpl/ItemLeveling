package pl.itemleveling.events;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import pl.itemleveling.ItemLeveling;

public class Events implements Listener {

    private final ItemLeveling plugin = ItemLeveling.getInstance();

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if(event.getEntity().getKiller() == null) return;
        Player killer = event.getEntity().getKiller();
        ItemStack murder_weapon = killer.getInventory().getItemInMainHand();
        NBTItem nbt = new NBTItem(murder_weapon);
        if(nbt.getBoolean("itemleveling-isSuperItem")) {
            int level = nbt.getInteger("itemleveling-itemLevel");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDestroy(BlockBreakEvent event) {

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

    }

}
