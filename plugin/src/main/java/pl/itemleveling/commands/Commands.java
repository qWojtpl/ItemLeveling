package pl.itemleveling.commands;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.itemleveling.ItemLeveling;
import pl.itemleveling.item.CustomItem;
import pl.itemleveling.item.ItemManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements CommandExecutor {

    private final ItemLeveling plugin = ItemLeveling.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = false;
        if(sender instanceof Player) {
            if(!sender.hasPermission(plugin.getPermissionManager().getPermission("itemleveling.manage"))) {
                sender.sendMessage("§cYou don't have permission!");
                return true;
            }
            isPlayer = true;
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                plugin.getDataHandler().loadConfig();
                sender.sendMessage("§aReloaded!");
            } else if(args[0].equalsIgnoreCase("get")) {
                if(!isPlayer) {
                    sender.sendMessage("§cYou must be player to execute this command!");
                    return true;
                }
                if(args.length > 1) {
                    ItemManager im = plugin.getItemManager();
                    boolean found = false;
                    CustomItem item = null;
                    for(CustomItem ci : im.getItems()) {
                        if(ci.getName().equalsIgnoreCase(args[1])) {
                            found = true;
                            item = ci;
                            break;
                        }
                    }
                    if(!found) {
                        sender.sendMessage("§cCan't found item: " + args[1]);
                        return true;
                    }
                    ItemStack is = new ItemStack(item.getItems().get(0));
                    ItemMeta meta = is.getItemMeta();
                    meta.setDisplayName(item.getNames().get(0));
                    String itemLore = item.getLores().get(0);
                    List<String> lore = Arrays.asList(itemLore.split("%nl%"));
                    meta.setLore(lore);
                    for(int i = 0; i < item.getEnchants().get(0).size(); i++) {
                        meta.addEnchant(item.getEnchants().get(0).get(i), item.getEnchantsLevels().get(0).get(i), true);
                    }
                    meta.setUnbreakable(item.getLevelUnbreakables().get(0));
                    is.setItemMeta(meta);
                    NBTItem nbt = new NBTItem(is);
                    nbt.setBoolean("itemleveling-isSuperItem", true);
                    nbt.setInteger("itemleveling-itemLevel", 0);
                    ((Player) sender).getInventory().addItem(is);
                    sender.sendMessage("§aEnjoy your new item!");
                } else {
                    sender.sendMessage("§cCorrect usage: /il get <item>");
                }
            } else {
                sender.sendMessage("§cCorrect usage: /il <reload|get>");
            }
        }
        return true;
    }

}
