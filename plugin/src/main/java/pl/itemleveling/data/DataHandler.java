package pl.itemleveling.data;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import pl.itemleveling.ItemLeveling;
import pl.itemleveling.item.CustomItem;
import pl.itemleveling.item.ItemManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class DataHandler {

    private final ItemLeveling plugin = ItemLeveling.getInstance();
    private String prefix;

    public void loadConfig() {
        ItemManager im = plugin.getItemManager();
        im.getItems().clear();
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
        prefix = yml.getString("config.prefix");
        ConfigurationSection section = yml.getConfigurationSection("items");
        if(section == null) return;
        for(String name : section.getKeys(false)) {
            ConfigurationSection section1 = yml.getConfigurationSection("items." + name);
            if(section1 == null) continue;
            List<Integer> levels = new ArrayList<>();
            for(String lvl : section1.getKeys(false)) {
                try {
                    levels.add(Integer.parseInt(lvl));
                } catch(NumberFormatException e) {
                    plugin.getLogger().severe("Cannot compare " + lvl + " with a correct level number! Replacing with 1...");
                    levels.add(1);
                }
            }
            if(!levels.contains(0)) {
                plugin.getLogger().severe("Cannot find level 0 for " + name + "!");
                continue;
            }
            HashMap<Integer, String> names = new HashMap<>();
            HashMap<Integer, String> lores = new HashMap<>();
            HashMap<Integer, Material> items = new HashMap<>();
            HashMap<Integer, List<Enchantment>> enchantments = new HashMap<>();
            HashMap<Integer, List<Integer>> enchantmentsLevels = new HashMap<>();
            HashMap<Integer, Boolean> unbreakables = new HashMap<>();
            HashMap<Integer, List<String>> events = new HashMap<>();
            for(Integer lvl : levels) {
                names.put(lvl, yml.getString("items." + name + "." + lvl + ".name"));
                lores.put(lvl, yml.getString("items." + name + "." + lvl + ".lore"));
                String stringMaterial = yml.getString("items." + name + "." + lvl + ".item");
                Material material;
                if(stringMaterial != null) {
                    Material m = Material.getMaterial(stringMaterial);
                    if(m == null) {
                        plugin.getLogger().warning("Cannot find material: " + stringMaterial + ", replacing with stick...");
                        material = Material.STICK;
                    } else {
                        material = m;
                    }
                } else {
                    plugin.getLogger().warning("Cannot find null material, replacing with stick...");
                    material = Material.STICK;
                }
                items.put(lvl, material);
                List<Enchantment> actualEnchs = new ArrayList<>();
                List<Integer> actualLevels = new ArrayList<>();
                List<String> allEnchs = yml.getStringList("items." + name + "." + lvl + ".enchantments");
                for(String enchant : allEnchs) {
                    String[] enchantAndLevel = enchant.split(":");
                    if(enchantAndLevel.length < 2) {
                        plugin.getLogger().warning("Cannot find enchantment (or level) for " + name + ". Replacing with ALL_DAMAGE:1...");
                        enchantAndLevel = new String[]{"ALL_DAMAGE", "1"};
                    }
                    Enchantment e = Enchantment.getByName(enchantAndLevel[0]);
                    if(e == null) {
                        plugin.getLogger().warning("Cannot find enchantment: " + enchant + ", replacing with flame...");
                        e = Enchantment.ARROW_FIRE;
                    }
                    actualEnchs.add(e);
                    try {
                        actualLevels.add(Integer.parseInt(enchantAndLevel[1]));
                    } catch(NumberFormatException exception) {
                        plugin.getLogger().severe("Cannot compare " + enchant.split(":")[1] +
                                " with a correct level number! Replacing with 1...");
                        actualLevels.add(1);
                    }
                }
                enchantments.put(lvl, actualEnchs);
                enchantmentsLevels.put(lvl, actualLevels);
                unbreakables.put(lvl, yml.getBoolean("items." + name + "." + lvl + ".unbreakable"));
                events.put(lvl, yml.getStringList("items." + name + "." + lvl + ".eventsToUpgrade"));
            }
            CustomItem ci = new CustomItem(name, names, lores, items, enchantments, enchantmentsLevels, unbreakables, events);
            im.getItems().add(ci);
        }
    }
}
