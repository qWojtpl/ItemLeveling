package pl.itemleveling.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import pl.itemleveling.ItemLeveling;
import pl.itemleveling.item.CustomItem;
import pl.itemleveling.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper implements TabCompleter {

    private final ItemLeveling plugin = ItemLeveling.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if(!(sender instanceof Player) || !sender.hasPermission(
                plugin.getPermissionManager().getPermission("itemleveling.manage"))) return null;
        if(args.length == 1) {
            completions.add("help");
            completions.add("get");
            completions.add("info");
            completions.add("reload");
        } else if(args[0].equalsIgnoreCase("get")) {
            if(args.length == 2) {
                for(CustomItem ci : plugin.getItemManager().getItems()) {
                    completions.add(ci.getName());
                }
            } else if(args.length == 3) {
                completions.add("#LEVEL");
            } else if(args.length == 4) {
                for(Player p : plugin.getServer().getOnlinePlayers()) {
                    if(!PlayerUtil.isVanished(p)) completions.add(p.getName());
                }
            }
        }
        return StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<>());
    }
}
