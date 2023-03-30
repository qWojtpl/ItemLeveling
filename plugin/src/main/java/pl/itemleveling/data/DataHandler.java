package pl.itemleveling.data;

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

public class DataHandler {

    private final ItemLeveling plugin = ItemLeveling.getInstance();

    public void loadConfig() {
        ItemManager im = plugin.getItemManager();
        im.getItems().clear();
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection section = yml.getConfigurationSection("items");
        if(section == null) return;
        for(String name : section.getKeys(false)) {
            ConfigurationSection section1 = yml.getConfigurationSection("items." + name);
            if(section1 == null) continue;
            List<Integer> levels = new ArrayList<>();
            for(String lvl : section1.getKeys(false)) {
                try {
                    levels.add(Integer.valueOf(lvl));
                } catch(NumberFormatException e) {
                    plugin.getLogger().severe("Cannot compare " + lvl + " with a correct level number! " +
                            "Don't loading other items...");
                    return;
                }
            }
            HashMap<Integer, String> names = new HashMap<>();
            HashMap<Integer, String> lores = new HashMap<>();
            HashMap<Integer, Material> items = new HashMap<>();
            HashMap<Integer, List<Enchantment>> enchantments = new HashMap<>();
            HashMap<Integer, List<Integer>> enchantmentsLevels = new HashMap<>();
            HashMap<Integer, Boolean> unbreakables = new HashMap<>();
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
                List<String> enchs = yml.getStringList("items." + name + "." + lvl + ".enchantments");
                for(String ench : enchs) {
                    String[] enchantAndLevel = ench.split(":");
                    if(enchantAndLevel.length < 2) {
                        plugin.getLogger().warning("Cannot find enchantment (or level) for " + name +
                                ". Don't loading other items...");
                        return;
                    }
                    Enchantment e = Enchantment.getByName(enchantAndLevel[0]);
                    if(e == null) {
                        plugin.getLogger().warning("Cannot find enchantment: " + ench + ", replacing with flame...");
                        e = Enchantment.ARROW_FIRE;
                    }
                    actualEnchs.add(e);
                    try {
                        actualLevels.add(Integer.valueOf(enchantAndLevel[1]));
                    } catch(NumberFormatException exception) {
                        plugin.getLogger().severe("Cannot compare " + ench.split(":")[1] +
                                " with a correct level number! Don't loading other items...");
                        return;
                    }
                }
                enchantments.put(lvl, actualEnchs);
                enchantmentsLevels.put(lvl, actualLevels);
                unbreakables.put(lvl, yml.getBoolean("items." + name + "." + lvl + ".unbreakable"));
            }
            CustomItem ci = new CustomItem(name, names, lores, items, enchantments, enchantmentsLevels, unbreakables);
            im.getItems().add(ci);
        }
    }
}
