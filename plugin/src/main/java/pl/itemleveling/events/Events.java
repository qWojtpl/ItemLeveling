package pl.itemleveling.events;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import pl.itemleveling.ItemLeveling;
import pl.itemleveling.item.CustomItem;
import pl.itemleveling.item.ItemManager;

import java.text.MessageFormat;

public class Events implements Listener {

    private final ItemLeveling plugin = ItemLeveling.getInstance();

    public void check(ItemStack item, Player player, String event) {
        NBTItem nbt = new NBTItem(item);
        if(!nbt.getBoolean("itemleveling-isSuperItem")) return;
        String id = nbt.getString("itemleveling-id");
        int level = nbt.getInteger("itemleveling-itemLevel");
        CustomItem customItem = null;
        boolean found = false;
        ItemManager im = plugin.getItemManager();
        for(CustomItem ci : im.getItems()) {
            if(ci.getName().equals(id)) {
                found = true;
                customItem = ci;
            }
        }
        if(!found) return;
        int max = 0;
        int sum = 0;
        int i = -1;
        String[] splitSourceEvent = event.split(" ");
        for(String ev : customItem.getEvents().get(level)) {
            i++;
            String[] splitEv = ev.split(" ");
            int required = Integer.parseInt(splitEv[1]);
            int current = nbt.getInteger("itemleveling-event-" + i);
            max += required;
            sum += current;
            if(!splitEv[0].equalsIgnoreCase(splitSourceEvent[0])) continue;
            if(splitEv[2].equalsIgnoreCase("*")) {
                splitEv[2] = splitSourceEvent[1];
            }
            if(splitEv[2].contains("*%")) {
                String type = splitEv[2].replace("*%", "");
                if(splitSourceEvent[1].toLowerCase().contains(type.toLowerCase())) {
                    splitEv[2] = splitSourceEvent[1];
                }
            }
            if(!splitEv[2].equalsIgnoreCase(splitSourceEvent[1])) continue;
            if(current >= required) continue;
            if(customItem.getMessages().get(level) != null) {
                player.sendMessage(
                        MessageFormat.format(customItem.getMessages().get(level), plugin.getDataHandler().getPrefix()));
            }
            current += 1;
            sum += 1;
            nbt.setInteger("itemleveling-event-" + i, current);
            nbt.applyNBT(item);
        }
        if(sum == 0 && max == 0) return;
        if(sum >= max) {
            im.Upgrade(item, customItem, ++level, player);
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if(event.getEntity().getKiller() == null) return;
        Player killer = event.getEntity().getKiller();
        String victim = event.getEntity().getType().name();
        ItemStack murder_weapon = killer.getInventory().getItemInMainHand();
        if(murder_weapon.getType().equals(Material.AIR)) return;
        check(murder_weapon, killer, "kill " + victim);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDestroy(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType().equals(Material.AIR)) return;
        check(item, event.getPlayer(), "break " + event.getBlock().getType());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;
        Entity damager = event.getDamager();
        if(!(damager instanceof Player)) return;
        ItemStack item = ((Player) damager).getInventory().getItemInMainHand();
        if(item.getType().equals(Material.AIR)) return;
        NBTItem nbt = new NBTItem(item);
        if(nbt.getBoolean("itemleveling-isSuperItem")) {
            double damage = event.getDamage();
            for(int i = 0; i < damage; i++) {
                check(item, (Player) damager, "damage " + event.getEntity().getType());
            }
        }
    }

}
