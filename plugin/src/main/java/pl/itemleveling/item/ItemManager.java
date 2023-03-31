package pl.itemleveling.item;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ItemManager {

    private final List<CustomItem> items = new ArrayList<>();

    public ItemStack getItemStack(CustomItem item, int level) {
        if(!item.getItems().containsKey(level)) {
            level = 0;
        }
        ItemStack is = new ItemStack(item.getItems().get(level));
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(item.getNames().get(level));
        String itemLore = item.getLores().get(level);
        List<String> lore = Arrays.asList(itemLore.split("%nl%"));
        meta.setLore(lore);
        for(int i = 0; i < item.getEnchants().get(level).size(); i++) {
            meta.addEnchant(item.getEnchants().get(level).get(i), item.getEnchantsLevels().get(level).get(i), true);
        }
        meta.setUnbreakable(item.getUnbreakables().get(level));
        is.setItemMeta(meta);
        NBTItem nbt = new NBTItem(is);
        nbt.setString("itemleveling-id", item.getName());
        nbt.setBoolean("itemleveling-isSuperItem", true);
        nbt.setInteger("itemleveling-itemLevel", level);
        nbt.applyNBT(is);
        return is;
    }

    public void Upgrade(ItemStack itemStack, CustomItem item, int level, Player player) {
        int slot = -1;
        for(int i = 0; i < 9; i++) {
            if(player.getInventory().getItem(i) != null) {
                if(player.getInventory().getItem(i).equals(itemStack)) {
                    slot = i;
                    break;
                }
            }
        }
        if(slot == -1) return;
        player.getInventory().setItem(slot, getItemStack(item, level));
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
        player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 10);
    }

}
