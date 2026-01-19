package org.jufyer.plugin.justMakesSense.copperHoppper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.recpies.api.RecipeType;
import org.jufyer.plugin.justMakesSense.recpies.api.RecipesAPI;
import org.jufyer.plugin.justMakesSense.recpies.impl.domains.ItemRecipe;
import org.jufyer.plugin.justMakesSense.recpies.impl.domains.recipes.RecipeBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HopperRegistry {

  private static final Map<CopperVariant, ItemStack> COPPER_ITEMS = new HashMap<>();

  public static void createCopperHopperItems() {
    for (CopperVariant variant : CopperVariant.values()) {
      String name = variant.getRegistryName();
      Float customModelDataFloat = variant.getCustomModelData();

      ItemStack item = new ItemStack(Material.SMALL_AMETHYST_BUD);
      ItemMeta meta = item.getItemMeta();

      if (meta != null) {
        var customData = meta.getCustomModelDataComponent();
        customData.setFloats(List.of(customModelDataFloat));
        meta.setCustomModelDataComponent(customData);
        meta.customName(Component.text(name, Style.empty()));
        meta.getPersistentDataContainer().set(variant.getItemKey(), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
      }

      COPPER_ITEMS.put(variant, item);
    }
  }

  public static ItemStack getCopperHopperItem(CopperVariant variant) {
    ItemStack item = COPPER_ITEMS.get(variant);
    return (item != null) ? item.clone() : null;
  }

  public static ItemStack CopperHopperItem1to1(CopperVariant variant) {
    return COPPER_ITEMS.get(variant);
  }

//  public static ItemDisplay spawnCopperHopperDisplay(CopperVariant variant, Location location, BlockFace blockFace) {
//    ItemStack item = COPPER_ITEMS.get(variant);
//    if (item == null || location.getWorld() == null) return null;
//
//    ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
//    display.setItemStack(item.clone());
//    display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
//
//    Block block = location.getBlock();
//    block.setType(Material.HOPPER);
//    Hopper hopper = (Hopper) block.getState();
//
//    hopper.setTransferCooldown(variant.getSpeed());
//
//    hopper.getPersistentDataContainer().set(variant.getBlockKey(), PersistentDataType.INTEGER, variant.getSpeed());
//    hopper.update();
//
//    Main.copperHoppers.put(hopper, display);
//    Main.loadedCopperHoppers.add(hopper.getLocation());
//
//    if (blockFace == BlockFace.NORTH) {
//      ItemStack displayItemStack = display.getItemStack();
//      for (CopperVariant copperVariant : CopperVariant.values()) {
//        if (displayItemStack.getPersistentDataContainer().has(copperVariant.getItemKey())) {
//          ItemMeta displayItemMeta = displayItemStack.getItemMeta();
//          var customData = displayItemMeta.getCustomModelDataComponent();
//          Float customModelDataFloat = copperVariant.getCustomModelDataSide();
//          customData.setFloats(List.of(customModelDataFloat));
//          displayItemMeta.setCustomModelDataComponent(customData);
//          displayItemStack.setItemMeta(displayItemMeta);
//          display.setItemStack(displayItemStack);
//
//          if (block.getBlockData() instanceof Directional directional) {
//            directional.setFacing(BlockFace.SOUTH);
//            block.setBlockData(directional);
//          }
//          break;
//        }
//      }
//    } else if (blockFace == BlockFace.EAST) {
//      ItemStack displayItemStack = display.getItemStack();
//      for (CopperVariant copperVariant : CopperVariant.values()) {
//        if (displayItemStack.getPersistentDataContainer().has(copperVariant.getItemKey())) {
//          ItemMeta displayItemMeta = displayItemStack.getItemMeta();
//          var customData = displayItemMeta.getCustomModelDataComponent();
//          Float customModelDataFloat = copperVariant.getCustomModelDataSide();
//          customData.setFloats(List.of(customModelDataFloat));
//          displayItemMeta.setCustomModelDataComponent(customData);
//          displayItemStack.setItemMeta(displayItemMeta);
//          display.setItemStack(displayItemStack);
//
//          Transformation transformation = display.getTransformation();
//
//          // Rot 90 around Y
//          transformation.getLeftRotation().setAngleAxis((float) Math.toRadians(-90), 0, 1, 0);
//          transformation.getTranslation().set(1f, 0f, 0f);
//
//          display.setTransformation(transformation);
//
//          if (block.getBlockData() instanceof Directional directional) {
//            directional.setFacing(BlockFace.WEST);
//            block.setBlockData(directional);
//          }
//          break;
//        }
//      }
//    } else if (blockFace == BlockFace.SOUTH) {
//      ItemStack displayItemStack = display.getItemStack();
//      for (CopperVariant copperVariant : CopperVariant.values()) {
//        if (displayItemStack.getPersistentDataContainer().has(copperVariant.getItemKey())) {
//          ItemMeta displayItemMeta = displayItemStack.getItemMeta();
//          var customData = displayItemMeta.getCustomModelDataComponent();
//          Float customModelDataFloat = copperVariant.getCustomModelDataSide();
//          customData.setFloats(List.of(customModelDataFloat));
//          displayItemMeta.setCustomModelDataComponent(customData);
//          displayItemStack.setItemMeta(displayItemMeta);
//          display.setItemStack(displayItemStack);
//
//          Transformation transformation = display.getTransformation();
//          transformation.getLeftRotation().setAngleAxis((float) Math.toRadians(180), 0, 1, 0);
//          transformation.getTranslation().set(1f, 0f, 1f);
//
//          display.setTransformation(transformation);
//
//          if (block.getBlockData() instanceof Directional directional) {
//            directional.setFacing(BlockFace.NORTH);
//            block.setBlockData(directional);
//          }
//          break;
//        }
//      }
//    } else if (blockFace == BlockFace.WEST) {
//      ItemStack displayItemStack = display.getItemStack();
//      for (CopperVariant copperVariant : CopperVariant.values()) {
//        if (displayItemStack.getPersistentDataContainer().has(copperVariant.getItemKey())) {
//          ItemMeta displayItemMeta = displayItemStack.getItemMeta();
//          var customData = displayItemMeta.getCustomModelDataComponent();
//          Float customModelDataFloat = copperVariant.getCustomModelDataSide();
//          customData.setFloats(List.of(customModelDataFloat));
//          displayItemMeta.setCustomModelDataComponent(customData);
//          displayItemStack.setItemMeta(displayItemMeta);
//          display.setItemStack(displayItemStack);
//
//          Transformation transformation = display.getTransformation();
//          transformation.getLeftRotation().setAngleAxis((float) Math.toRadians(90), 0, 1, 0);
//          transformation.getTranslation().set(0f, 0f, 1f);
//
//          display.setTransformation(transformation);
//
//          if (block.getBlockData() instanceof Directional directional) {
//            directional.setFacing(BlockFace.EAST);
//            block.setBlockData(directional);
//          }
//          break;
//        }
//      }
//    }
//
//    return display;
//  }

  public static ItemDisplay spawnCopperHopperDisplay(CopperVariant variant, Location location, BlockFace blockFace) {
    if (location.getBlock().getType() != Material.AIR) return null;
    if (Main.copperHoppers.containsKey(location)) return null;

    ItemStack item = COPPER_ITEMS.get(variant);
    if (item == null || location.getWorld() == null) return null;

    Block block = location.getBlock();
    block.setType(Material.HOPPER);

    if (block.getBlockData() instanceof Directional directional) {
      directional.setFacing(blockFace.getOppositeFace());
      block.setBlockData(directional);
    }

    Hopper hopper = (Hopper) block.getState();
    hopper.setTransferCooldown(variant.getSpeed());
    hopper.getPersistentDataContainer().set(variant.getBlockKey(), PersistentDataType.INTEGER, variant.getSpeed());
    hopper.update();

    ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
    display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);

    updateDisplayVisuals(display, variant, blockFace.getOppositeFace());

    Main.copperHoppers.put(hopper.getLocation(), display);
    Main.loadedCopperHoppers.add(hopper.getLocation());

    return display;
  }

  private static void updateDisplayVisuals(ItemDisplay display, CopperVariant variant, BlockFace facing) {
    ItemStack displayStack = COPPER_ITEMS.get(variant).clone();
    ItemMeta meta = displayStack.getItemMeta();
    var customData = meta.getCustomModelDataComponent();

    Transformation transformation = display.getTransformation();
    float angle = 0;
    boolean isSide = (facing != BlockFace.DOWN);

    switch (facing) {
      case NORTH -> angle = -180;
      case SOUTH -> angle = 0;
      case WEST -> angle = -90;
      case EAST -> angle = 90;
      default -> angle = 0;
    }

    float modelData = isSide ? variant.getCustomModelDataSide() : variant.getCustomModelData();
    customData.setFloats(List.of(modelData));
    meta.setCustomModelDataComponent(customData);
    displayStack.setItemMeta(meta);

    transformation.getLeftRotation().setAngleAxis((float) Math.toRadians(angle), 0, 1, 0);

    switch (facing) {
      case NORTH -> transformation.getTranslation().add(1,0,1);
      case WEST -> transformation.getTranslation().add(1,0,0);
      case EAST -> transformation.getTranslation().add(0,0,1);
    }

    display.setItemStack(displayStack);
    display.setTransformation(transformation);
  }


  public static void addCopperHopperRecipes() {
    RecipesAPI recipesAPI = new RecipesAPI(Main.getInstance(), true);

    for (CopperVariant variant : CopperVariant.values()) {
      if (variant.equals(CopperVariant.WAXED)) continue;
      if (variant.equals(CopperVariant.WAXED_EXPOSED)) continue;
      if (variant.equals(CopperVariant.WAXED_WEATHERED)) continue;
      if (variant.equals(CopperVariant.WAXED_OXIDIZED)) continue;

      String chestName = variant.getChestName().toUpperCase();
      Material chestMaterial = Material.getMaterial(chestName);

      if (chestMaterial == null) {
        Main.getInstance().getLogger().warning("Could not find material: " + chestName);
        continue;
      }

      ItemStack resultItem = getCopperHopperItem(variant);
      String recipeName = variant.getRegistryName();

      ItemRecipe copperHopper = new RecipeBuilder()
        .setType(RecipeType.CRAFTING_SHAPED)
        .setName(recipeName)
        .setResult(resultItem)
        .setAmount(1)
        .setPattern("C C", "CKC", " C ")
        .addIngredient(Material.COPPER_INGOT, 'C')
        .addIngredient(chestMaterial, 'K')
        .build();

      recipesAPI.addRecipe(copperHopper);
    }

    for (CopperVariant variant : CopperVariant.values()) {
      if (variant.equals(CopperVariant.NORMAL)) continue;
      if (variant.equals(CopperVariant.EXPOSED)) continue;
      if (variant.equals(CopperVariant.WEATHERED)) continue;
      if (variant.equals(CopperVariant.OXIDIZED)) continue;

      String chestName = variant.getChestName().toUpperCase();
      Material chestMaterial = Material.getMaterial(chestName);

      if (chestMaterial == null) {
        Main.getInstance().getLogger().warning("Could not find material: " + chestName);
        continue;
      }

      ItemStack resultItem = getCopperHopperItem(variant);
      String recipeName = variant.getRegistryName();

      ItemRecipe copperHopper = new RecipeBuilder()
        .setType(RecipeType.CRAFTING_SHAPED)
        .setName(recipeName)
        .setResult(resultItem)
        .setAmount(1)
        .setPattern("CHC", "CKC", " C ")
        .addIngredient(Material.COPPER_INGOT, 'C')
        .addIngredient(Material.HONEYCOMB, 'H')
        .addIngredient(chestMaterial, 'K')
        .build();

      recipesAPI.addRecipe(copperHopper);
    }
  }

  // --- Save and Load ---

  public static void saveHopperWithItemDisplay() {
    File file = new File(Main.getInstance().getDataFolder(), "copperHopper.yml");
    YamlConfiguration config = new YamlConfiguration();

    int i = 0;
    for (Map.Entry<Location, ItemDisplay> entry : Main.copperHoppers.entrySet()) {
      Location hopper = entry.getKey();
      ItemDisplay display = entry.getValue();

      config.set("hoppers." + i + ".location", hopper.getBlock().getLocation());
      config.set("hoppers." + i + ".uuid", display.getUniqueId().toString());
      i++;
    }

    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void loadHopperWithItemDisplay() {
    File file = new File(Main.getInstance().getDataFolder(), "copperHopper.yml");
    if (!file.exists()) return;

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    if (config.getConfigurationSection("hoppers") == null) return;

    for (String key : config.getConfigurationSection("hoppers").getKeys(false)) {
      Location loc = config.getLocation("hoppers." + key + ".location");
      String uuidStr = config.getString("hoppers." + key + ".uuid");

      if (loc == null || uuidStr == null) continue;

      if (!loc.isChunkLoaded()) {
        loc.getChunk().load();
      }

      UUID uuid = UUID.fromString(uuidStr);
      Entity entity = Bukkit.getEntity(uuid);

      if (entity instanceof ItemDisplay itemDisplay) {
        Main.copperHoppers.put(loc, itemDisplay);
        Main.loadedCopperHoppers.add(loc.toBlockLocation());
      }
    }
  }
}
