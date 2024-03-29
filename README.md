<p align="center">
  <img src="images/logo.png">
</p>

<br>

# ItemLeveling

<p>Add item leveling to your Minecraft server</p>
<p>Tested minecraft versions: </p> 

`1.19.3`

# Installation

<p>Put ItemLeveling.jar to your plugins folder and restart the server.</p>
<p>REQUIRES NBT API FROM https://www.spigotmc.org/resources/nbt-api.7939/</p>

# Configuration

`Use § or & sign for colors`<br>

<details><summary>config.yml</summary>

```yml
config:
  prefix: "§e[§aItemLeveling§e] "
```

`prefix` - Prefix for commands<br>

<hr>

```yml
config:
  prefix: "§e[§aItemLeveling§e] "

items:
  "some_sword":
    0:
      name: "§aNormal sword"
      lore: "§2Some normal sword..."
      item: WOODEN_SWORD
      enchantments:
        - DAMAGE_ALL:10
      unbreakable: false
      eventsToUpgrade:
        - kill 10 zombie
        - kill 1 spider
    1:
      name: "§aSuper sword!"
      lore: "§2Some super sword!"
      item: DIAMOND_SWORD
      unbreakable: true
  "pickaxe":
    0:
      name: "§aStrange pickaxe"
      lore: "§2Nothing to see here..."
      item: WOODEN_PICKAXE
      progressMessage: "{0}§aI feel power..."
      eventsToUpgrade:
        - break 10 *%ore
    1:
      name: "§aSUPER PICKAXE!"
      lore: "§2Maybe it can be better?"
      item: IRON_PICKAXE
      progressMessage: "{0}§aI feel SO MANY power..."
      enchantments:
        - DIG_SPEED:8
      eventsToUpgrade:
        - break 10 diamond_ore
    2:
      name: "§2§lMEGA SUPER DUPER PICKAXE!"
      lore: "§aIt's unbreakable!"
      item: NETHERITE_PICKAXE
      unbreakable: true
      enchantments:
        - DIG_SPEED:10
  "stick":
    0:
      name: "§aStick"
      lore: "§2Just a normal stick"
      item: STICK
      eventsToUpgrade:
        - break 5 dirt
    1:
      name: "§aSUPER STICK"
      lore: "§2Not a normal stick!"
      item: STICK
      enchantments:
        - DIG_SPEED:3
  "axe":
    0:
      name: "An axe"
      lore: "Strange axe!"
      item: DIAMOND_AXE
      eventsToUpgrade:
        - damage 40 *
    1:
      name: "Super axe!"
      lore: "Super strange axe!"
      item: NETHERITE_AXE
  "armor":
    0:
      name: "Some helmet"
      item: LEATHER_HELMET
      eventsToUpgrade:
        - kill 10 zombie
    1:
      name: "Super helmet!"
      item: DIAMOND_HELMET
  "fishing-rod":
    0:
      name: "Some fishing rod"
      item: FISHING_ROD
      eventsToUpgrade:
        - fish 10 pufferfish
    1:
      name: "Super fishing rod!"
      item: FISHING_ROD
      unbreakable: true
```

Example of an item:<br>
`some_sword` - Name of the item<br>
`0` - Level 0 of the item (first level)<br>
`name` - Name of level 0 item<br>
`lore` - Lore of level 0 item (to get multiple lines use %nl%)<br>
`enchantments` - Enchantments of level 0 item (get enchantments from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html)<br>
` - DAMAGE_ALL:10` - name of enchantment:enchantment level (in this case this is sharpness 10)<br>
`unbreakable` - If set to true then item is unbreakable<br>
`progressMessage` - Use {0} to get prefix. This message will be send to player every progress they made (break a block, kill a monster etc.)<br>
`eventsToUpgrade` - Events to upgrade item to the next level. If you don't want item of this level to be levelable, then delete eventsToUpgrade. On upgrade if there's no next level item will return to level 0.<br>

Supported events:
```java
- break     // Break a block using this item
- kill      // Kill an entity using this item
- damage    // Damage entity using this item
- fish      // Fish 
```

` - kill 10 zombie` - Means not else than kill 10 zombie<br>
` - kill 1 spider` - Kill 1 spider<br>
` - break 3 diamond_ore` - Break diamond ore<br>
` - break 10 *` - Break 10 ANY block<br>
` - break 10 *%_ore` - Break 10 blocks which name contains "_ore"<br>
` - fish 10 pufferfish` - Player need to fish 10 pufferfishes<br>


</details>

# Commands & Permissions

Permission to manage: `itemleveling.manage`<br>
`/il [help]` - Show help<br>
`/il reload` - Reload configuration<br>
`/il get <itemName> [level] [player]` - Get levelable item<br>
`/il info` - Shows information about item in your hand (if it is levelable item, level of item etc.)<br>
