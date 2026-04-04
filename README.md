<p align="center"><img src="https://imgur.com/TcdliLJ.png" alt="plugin banner"></p>

# Just Makes Sense

A Minecraft plugin that adds small, logical quality-of-life features that feel natural and vanilla-friendly.

---

## Overview

Just Makes Sense enhances Minecraft with intuitive mechanics that many players expect to already exist. All features are configurable and can be enabled or disabled individually via the config file.

---

## Features

## Smarter Cauldrons

### Honey Collection

* Place a cauldron underneath a Bee Nest or Bee Hive
* After 1–5 minutes, a Honey Block appears inside the cauldron
* Honey can be removed manually or extracted using hoppers

<p align="center"><img src="https://imgur.com/GBZps4f.png" alt="honey in cauldron"></p>

---

### Ice Formation

* If a water-filled cauldron is placed in a biome where water can freeze:
  * After 1–5 minutes, the water turns into an Ice Block
* Ice can be collected manually or via hoppers

<p align="center"><img src="https://imgur.com/yyjTMLp.png" alt="ice in cauldron"></p>

---

### Mud & Concrete Production

* Throw **Dirt** into a water-filled cauldron to turn it into **Mud**
* Throw **Concrete Powder** into a water-filled cauldron to turn it into **Concrete**

---

### Removing Dye with Water

* Right-click a water-filled cauldron with any dyed item
* The dye is removed from the item
* The cauldron water level decreases by one

---

### Dispenser Interaction

* Dispensers can interact with cauldrons
* Supports water, lava, and powder snow
* Optional interaction from below

<p align="center"><img src="https://imgur.com/tGTZzQS.png" alt="dispenser interaction"></p>

---

## Zombie Variants

* Two new Zombie variants: Jungle and Snow
* The Jungle Zombie will summon poisonous Area Effect Clouds 
* The Snow Zombie will summon slowness Area Effect Clouds

<p align="center"><img src="https://imgur.com/TEob75u.png" alt="zombie variants"></p>

---

## Banners on Boats

* Shift-right-click a boat with a banner to place the banner inside the boat
* The banner occupies one passenger slot
* Only one player can ride the boat while a banner is present
* Shift-right-click again to remove the banner

<p align="center"><img src="https://imgur.com/StpnqOT.png" alt="banners on boats"></p>

---

## Repairable anvils

* Right-click a broken anvil with an iron block to repair it by one stage:
* Damaged --> Chipped; Chipped --> Normal

---

## Instant Sponge Drying

* Setting a **Wet Sponge** on fire (e.g. with Flint and Steel) dries it out instantly.
* No more waiting for the furnace!

---

## Water Bottles vs. Lava

* Splashing/Throwing a **Water Bottle** into lava converts the lava source into **Obsidian**.

---

## Copper Hoppers (1.21.9+ only)

Copper Hoppers are upgraded hoppers with different transfer speeds depending on their oxidation state.

<p align="center"><img src="https://imgur.com/peLIGKg.png" alt="copper hoppers"></p>

### Variants and Speeds

| Variant         | Ticks per Item | Notes               |
| --------------- |----------------|---------------------|
| NORMAL          | 4              | Faster than vanilla |
| EXPOSED         | 6              |                     |
| WEATHERED       | 8              | Vanilla speed       |
| OXIDIZED        | 12             |                     |
| WAXED           | 4              | Faster than vanilla |
| WAXED_EXPOSED   | 6              |                     |
| WAXED_WEATHERED | 8              | Vanilla speed       |
| WAXED_OXIDIZED  | 12             |                     |

Vanilla hopper speed: 8 ticks per item

---

### Copper Hopper Mechanics

* Oxidizes exactly like normal copper blocks
* Can be waxed using Honeycomb
* Wax can be removed using an axe
* After moving a configurable number of items, the wax is removed automatically for balancing
* Setting the value to 0 disables wax removal entirely

---

### Crafting Recipes

Normal Copper Hopper:
```yaml
C   C
C K C
  C 
```

Waxed Copper Hopper:
```yaml
C H C
C K C
  C 
```

C = Copper Ingot
K = Copper Chest (Any variant, if you use a Honeycomb it needs to be waxed)
H = Honeycomb

---

## Dyed Torches

* Craft any dye together with a torch
* Produces a dyed torch variant
* Decorative feature only

<p align="center"><img src="https://imgur.com/Z4rKP0B.png" alt="dyed torches"></p>

---

## Edible Glistering Melon

* Glistering Melons are now edible
* Grants Regeneration II for 4 seconds
* Restores food level and saturation (up to vanilla limits)

---

## Edible Melon blocks

* Right click on Melon blocks to eat a slice
* You get the same Saturation and Food Level as you would get by eating a melon slice

<p align="center"><img src="https://imgur.com/eXkw2AR.png" alt="edible melon blocks"></p>

---

## Mob Loot Tweaks

* Goats drop mutton
* Husks have a chance to drop sand

<p align="center"><img src="https://imgur.com/E35zJqO.png" alt="Mob loot tweaks"></p>

---

## Pet protection

* If you are not sneaking you can't damage your own pet
* --> if you are sneaking you can damage your pet

---

## Horse Statistics Preview

* Sneak-right-click on any horse to see its Stats
* --> Removes the need to test them one by one

<p align="center"><img src="https://imgur.com/WP2ETgE.png" alt="Horse Stats Preview"></p>

---

## Persistent enchanting lapis

* Lapis lazuli stays in the enchanting table when closed.

---

## Crop protection

* Farmland can no longer be trampled.

---

## Double Door support

* Open both doors at once with a single interaction

---

## Configuration

All features can be configured individually.

```yaml
# ==================================================
# "Just Makes Sense" Configuration
# ==================================================

# Enable the update checker (recommended for bug fixes and new features)
update-checker: true

# --------------------------------------------------
# Core Cauldron Features
# --------------------------------------------------

# Enable ice-related cauldron mechanics
enable-ice: true

# Enable honey interactions with cauldrons
enable-honey: true

# Allow removing dye from items using cauldrons
enable-remove-dye: true

# Throw dirt into a Cauldron to turn it into Mud
cauldron-mud: true

# Throw concrete powder into a Cauldron to turn it into Concrete
cauldron-concrete: true

# --------------------------------------------------
# Cauldron Dispenser Support
# --------------------------------------------------
# Dispensers now can dispense liquid and powder snow into cauldrons

# Enable dispenser rework
cauldron-dispenser-support: true

# --------------------------------------------------
# Copper Hopper Mechanics
# --------------------------------------------------

# Enable copper hopper functionality
copper-hopper: true

# Number of items ejected before the hopper loses its wax layer
# (0 = never loses wax, 1000 = vanilla-like default)
copper-hopper-item-count: 1000


# --------------------------------------------------
# New Mobs
# --------------------------------------------------

# Be careful! The following features require the plugin "Citizens" which can be downloaded for free here:
# https://ci.citizensnpcs.co/job/Citizens2/

# Snow Zombies give you the poison effect
snow-zombie: true

# Jungle Zombies give you the poison effect
jungle-zombie: true


# --------------------------------------------------
# Item & Entity Gameplay Tweaks
# --------------------------------------------------

# Right-click a chipped/damaged anvil with an iron block to repair it
repair-anvil: true

# Glistering melons become edible and grant a short regeneration effect
enable-edible-glistering-melon: true

# Right click on melon blocks to eat a slice
melon-block-update: true

# Allow banners to be placed on boats
banner-on-boats: true

# Husks have a chance to drop sand
husks-drop-sand: true

# Goats drop mutton when killed
goat-drop-mutton: true

# Enable dyed torch variants
dyed-torches: true

# Protect your own pets by not allowing to attack them when you are not sneaking
protect-pets: true

# Setting a sponge on fire dries it out instantly
sponge-ignite: true

# Splashing a Water Bottle into lava converts it into obsidian
water-bottles-convert-lava: true

# Allows players to open two doors facing each other at once
double-door: true

# Prevent farmland from being trampled
farmland-protection: true

# Lapis lazuli stays in the enchanting table when closed
persistent-lapis-lazuli: true

# Sneak-right-click a horse to view its health, speed and jump strength
horse-stats: true

# ==================================================
# World Overrides (Optional)
# ==================================================
# Here you can override specific global settings for individual worlds.
# If a world or feature is not listed here, it uses the global settings from above.
# Example:
# world-overrides:
#   Lobby:
#     enable-ice: false
#     snow-zombie: false
#   world_nether:
#     enable-water: false
#     water-bottles-convert-lava: false
world-overrides:

```

---

## This plugin is using bStats

<p align="center"><img src="https://bstats.org/signatures/bukkit/jufyer-justMakesSense-plugin.svg" alt="bStats chart"></p>

---

## Requirements

* Minecraft 1.21 or newer
* Paper

---

## Design Philosophy

The goal of Just Makes Sense is to add features that feel so natural that players might wonder why they were not part of vanilla Minecraft already.
