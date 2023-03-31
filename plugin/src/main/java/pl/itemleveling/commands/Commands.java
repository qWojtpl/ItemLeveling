package pl.itemleveling.commands;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.itemleveling.ItemLeveling;
import pl.itemleveling.item.CustomItem;
import pl.itemleveling.item.ItemManager;

public class Commands implements CommandExecutor {

    private final ItemLeveling plugin = ItemLeveling.getInstance();
    private final String prefix = plugin.getDataHandler().getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = false;
        if(sender instanceof Player) {
            if(!sender.hasPermission(plugin.getPermissionManager().getPermission("itemleveling.manage"))) {
                sender.sendMessage(prefix + "§cYou don't have permission!");
                return true;
            }
            isPlayer = true;
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                plugin.getDataHandler().loadConfig();
                sender.sendMessage(prefix + "§aReloaded!");
            } else if(args[0].equalsIgnoreCase("get")) {
                if(!isPlayer) {
                    sender.sendMessage(prefix + "§cYou must be a player to execute this command!");
                    return true;
                }
                if(args.length > 1) {
                    ItemManager im = plugin.getItemManager();
                    boolean found = false;
                    CustomItem item = null;
                    for (CustomItem ci : im.getItems()) {
                        if (ci.getName().equalsIgnoreCase(args[1])) {
                            found = true;
                            item = ci;
                            break;
                        }
                    }
                    int count = 0;
                    if(args.length > 2) {
                        try {
                            count = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(prefix + "§c" + args[2] + " is not a valid level number!");
                            return true;
                        }
                    }
                    if(!found) {
                        sender.sendMessage(prefix + "§cCan't found item: " + args[1]);
                        return true;
                    }
                    ((Player) sender).getInventory().addItem(im.getItemStack(item, count));
                    sender.sendMessage(prefix + "§aEnjoy your new item!");
                } else {
                    sender.sendMessage(prefix + "§cCorrect usage: /il get <item> <level>");
                }
            } else if(args[0].equalsIgnoreCase("info")) {
                if(!isPlayer) {
                    sender.sendMessage(prefix + "§cYou must be a player to execute this command!");
                    return true;
                }
                ItemStack is = ((Player) sender).getInventory().getItemInMainHand();
                if(is.getType().equals(Material.AIR)) {
                    sender.sendMessage(prefix + "§cYou must have item in your hand!");
                    return true;
                }
                NBTItem nbt = new NBTItem(is);
                sender.sendMessage("§e<---------- §aItemLeveling §e---------->");
                sender.sendMessage("");
                String specialItem = "§cFalse";
                if(nbt.getBoolean("itemleveling-isSuperItem")) specialItem = "§2True";
                sender.sendMessage("§aIs special item?: " + specialItem);
                sender.sendMessage("§aItem ID: §2" + nbt.getString("itemleveling-id"));
                sender.sendMessage("§aItem level: §2" + nbt.getInteger("itemleveling-itemLevel"));
                for(int i = 0; i < 36; i++) {
                    if(nbt.getInteger("itemleveling-event-" + i) == 0) continue;
                    sender.sendMessage("§aEvent " + i + " progress: §2" + nbt.getInteger("itemleveling-event-" + i));
                }
                sender.sendMessage("");
                sender.sendMessage("§e<---------- §aItemLeveling §e---------->");
            } else {
                ShowHelp(sender);
            }
        } else {
            ShowHelp(sender);
        }
        return true;
    }

    public void ShowHelp(CommandSender sender) {
        sender.sendMessage("§e<---------- §aItemLeveling §e---------->");
        sender.sendMessage("");
        sender.sendMessage("§a/il reload §2- §eReload configuration");
        sender.sendMessage("§a/il get <item> <level> §2- §eGet custom item");
        sender.sendMessage("§a/il info §2- §eCheck information about item in your hand");
        sender.sendMessage("");
        sender.sendMessage("§e<---------- §aItemLeveling §e---------->");
    }

}
