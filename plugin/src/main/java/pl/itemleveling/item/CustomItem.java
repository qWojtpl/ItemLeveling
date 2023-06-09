package pl.itemleveling.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;

@Getter
public class CustomItem {

    private final String name;
    private final HashMap<Integer, String> names;
    private final HashMap<Integer, String> lores;
    private final HashMap<Integer, Material> items;
    private final HashMap<Integer, List<Enchantment>> enchants;
    private final HashMap<Integer, List<Integer>> enchantsLevels;
    private final HashMap<Integer, Boolean> unbreakables;
    private final HashMap<Integer, List<String>> events;
    private final HashMap<Integer, String> messages;

    public CustomItem(String name, HashMap<Integer, String> names, HashMap<Integer, String> lores,
                      HashMap<Integer, Material> items, HashMap<Integer, List<Enchantment>> enchants,
                      HashMap<Integer, List<Integer>> enchantsLevels, HashMap<Integer, Boolean> unbreakables,
                      HashMap<Integer, List<String>> events, HashMap<Integer, String> messages) {
        this.name = name;
        this.names = names;
        this.lores = lores;
        this.items = items;
        this.enchants = enchants;
        this.enchantsLevels = enchantsLevels;
        this.unbreakables = unbreakables;
        this.events = events;
        this.messages = messages;
    }

}
