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

[IMAGE: cauldron_honey]

---

### Ice Formation

* If a water-filled cauldron is placed in a biome where water can freeze:

  * After 1–5 minutes, the water turns into an Ice Block
* Ice can be collected manually or via hoppers

[IMAGE: cauldron_ice]

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

[IMAGE: cauldron_dispenser]

---

## Banners on Boats

* Shift-right-click a boat with a banner to place the banner inside the boat
* The banner occupies one passenger slot
* Only one player can ride the boat while a banner is present
* Shift-right-click again to remove the banner

[IMAGE: banner_boat]

---

## Copper Hoppers

Copper Hoppers are upgraded hoppers with different transfer speeds depending on their oxidation state.

[IMAGE: copper_hopper_variants]

### Variants and Speeds

| Variant         | Ticks per Item | Notes               |
| --------------- |----------------| ------------------- |
| NORMAL          | 4              | Faster than vanilla |
| EXPOSED         | 6              |                     |
| WEATHERED       | 8              |                     |
| OXIDIZED        | 12             |                     |
| WAXED           | 4              |                     |
| WAXED_EXPOSED   | 6              |                     |
| WAXED_WEATHERED | 8              |                     |
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

[IMAGE: dyed_torches]

---

## Edible Glistering Melon

* Glistering Melons are now edible
* Grants Regeneration II for 4 seconds
* Restores food level and saturation (up to vanilla limits)

---

## Mob Loot Tweaks

* Goats drop mutton
* Husks have a chance to drop sand

[IMAGE: mob_drops]

---

## Configuration

All features can be configured individually.

```yaml
# ==================================================
# "Just Makes Sense" Configuration
# ==================================================

# --------------------------------------------------
# Core Cauldron Features
# --------------------------------------------------

enable-ice: true
enable-honey: true
enable-remove-dye: true

# --------------------------------------------------
# Cauldron Liquid Support
# --------------------------------------------------

allow-interaction-from-below: true
enable-water: true
enable-lava: true
enable-powder-snow: true

# --------------------------------------------------
# Copper Hopper Mechanics
# --------------------------------------------------

copper-hopper: true

# Number of items moved before wax is removed
# 0 = wax never wears off
copper-hopper-item-count: 10

# --------------------------------------------------
# Item and Entity Gameplay Tweaks
# --------------------------------------------------

enable-edible-glistering-melon: true
banner-on-boats: true
husks-drop-sand: true
goat-drop-mutton: true
dyed-torches: true
```

---

## Requirements

* Minecraft 1.21 or newer
* Paper or Spigot server

---

## Design Philosophy

The goal of Just Makes Sense is to add features that feel so natural that players might wonder why they were not part of vanilla Minecraft already.
