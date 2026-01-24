package org.jufyer.plugin.justMakesSense.dyedTorches;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

public class DyedTorchesRegistry {

  private static final Map<DyedTorchesVariant, ItemStack> TORCH_ITEMS = new HashMap<>();

  public static void createDyedTorchItems() {
    for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
      String name = variant.getRegistryName();
      Float customModelDataFloat = variant.getCustomModelData();

      ItemStack item = new ItemStack(Material.MEDIUM_AMETHYST_BUD);
      ItemMeta meta = item.getItemMeta();

      if (meta != null) {
        var customData = meta.getCustomModelDataComponent();
        customData.setFloats(List.of(customModelDataFloat));
        meta.setCustomModelDataComponent(customData);
        meta.customName(Component.text(name, Style.empty()));
        meta.getPersistentDataContainer().set(variant.getItemKey(), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
      }

      TORCH_ITEMS.put(variant, item);
    }
  }

  public static ItemStack getTorchItem(DyedTorchesVariant variant) {
    ItemStack item = TORCH_ITEMS.get(variant);
    return (item != null) ? item.clone() : null;
  }

  public static ItemStack DyedTorchItem1to1(DyedTorchesVariant variant) {
    return TORCH_ITEMS.get(variant);
  }

  public static ItemDisplay spawnDyedTorchDisplay(DyedTorchesVariant variant, Location location, BlockFace blockFace) {
    if (location.getBlock().getType() != Material.AIR) return null;
    if (Main.dyedTorches.containsKey(location)) return null;

    ItemStack item = TORCH_ITEMS.get(variant);
    if (item == null || location.getWorld() == null) return null;

    Block block = location.getBlock();
    if (blockFace.equals(BlockFace.UP)) {
      block.setType(Material.TORCH);
    }else {
      block.setType(Material.WALL_TORCH);
    }

    if (block.getBlockData() instanceof Directional directional) {
      directional.setFacing(blockFace);
      block.setBlockData(directional);
    }

    ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
    display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);

//    if (blockFace.equals(BlockFace.UP)) {
      display.getPersistentDataContainer().set(variant.getBlockKey(), PersistentDataType.BYTE, (byte) 1);
//    } else if (blockFace.equals(BlockFace.NORTH)) {
//      display.getPersistentDataContainer().set(variant.getWallBlockKey(), PersistentDataType.BYTE, (byte) 1);
//    } else if (blockFace.equals(BlockFace.EAST)) {
//      display.getPersistentDataContainer().set(variant.getWallBlockKey(), PersistentDataType.BYTE, (byte) 1);
//    } else if (blockFace.equals(BlockFace.SOUTH)) {
//      display.getPersistentDataContainer().set(variant.getWallBlockKey(), PersistentDataType.BYTE, (byte) 1);
//    } else if (blockFace.equals(BlockFace.WEST)) {
//      display.getPersistentDataContainer().set(variant.getWallBlockKey(), PersistentDataType.BYTE, (byte) 1);
//    }

    updateDisplayVisuals(display, variant, blockFace.getOppositeFace());

    Main.dyedTorches.put(block.getLocation(), display);

//    ParticleBuilder particleBuilder = new ParticleBuilder(Particle.FLAME);
//    particleBuilder.color(Color.AQUA);
//    particleBuilder.count(100);
//    particleBuilder.location(location);
//    particleBuilder.spawn();

    return display;
  }

  private static void updateDisplayVisuals(ItemDisplay display, DyedTorchesVariant variant, BlockFace facing) {
    ItemStack displayStack = TORCH_ITEMS.get(variant).clone();
    ItemMeta meta = displayStack.getItemMeta();
    var customData = meta.getCustomModelDataComponent();

    Transformation transformation = display.getTransformation();
    float angle = 0;
    boolean isWall = (facing != BlockFace.DOWN);

    switch (facing) {
      case NORTH -> angle = 90;
      case SOUTH -> angle = -90;
      case WEST -> angle = -180;
      case EAST -> angle = 0;
    }

    float modelData = 0;
    if (isWall) {
      modelData = variant.getCustomModelDataWall();
    }else {
      modelData = variant.getCustomModelData();
    }

    customData.setFloats(List.of(modelData));
    meta.setCustomModelDataComponent(customData);
    displayStack.setItemMeta(meta);

    if (!isWall) {
      transformation.getLeftRotation().setAngleAxis((float) Math.toRadians(angle), 0, 1, 0);
      transformation.getTranslation().add(0.5f,0.5f,0.5f);
      transformation.getScale().set(1.1);
    }else {
      transformation.getLeftRotation().setAngleAxis((float) Math.toRadians(angle), 0, 1, 0);

      switch (facing) {
        case WEST -> transformation.getTranslation().add(0.53f,0.49f,0.5f);
        case EAST -> transformation.getTranslation().add(0.47f,0.49f,0.5f);

        case NORTH -> transformation.getTranslation().add(0.5f,0.49f,0.53f);
        case SOUTH -> transformation.getTranslation().add(0.5f,0.49f,0.47f);
      }


      transformation.getScale().set(1.1);
    }

    display.setItemStack(displayStack);
    display.setTransformation(transformation);
  }

  public static void addDyedTorchRecipes() {
    RecipesAPI recipesAPI = new RecipesAPI(Main.getInstance(), true);

    for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {

      String dyeName = variant.getPrefix()+ "_dye";
      Material dyeMaterial = Material.getMaterial(dyeName.toUpperCase());

      if (dyeMaterial == null) {
        Main.getInstance().getLogger().warning("Could not find material: " + dyeName);
        continue;
      }

      ItemStack resultItem = getTorchItem(variant);
      String recipeName = variant.getRegistryName();

      ItemRecipe dyedTorch = new RecipeBuilder()
        .setType(RecipeType.CRAFTING_SHAPELESS)
        .setName(recipeName)
        .setResult(resultItem)
        .setAmount(1)
        .addIngredient(Material.TORCH, 'T')
        .addIngredient(dyeMaterial, 'D')
        .build();

      recipesAPI.addRecipe(dyedTorch);
    }
  }

  // --- Save and Load ---

  public static void saveDyedTorchWithItemDisplay() {
    File file = new File(Main.getInstance().getDataFolder(), "dyedTorches.yml");
    YamlConfiguration config = new YamlConfiguration();

    int i = 0;
    for (Map.Entry<Location, ItemDisplay> entry : Main.dyedTorches.entrySet()) {
      Location torch = entry.getKey();
      ItemDisplay display = entry.getValue();

      config.set("torches." + i + ".location", torch.getBlock().getLocation());
      config.set("torches." + i + ".uuid", display.getUniqueId().toString());
      i++;
    }

    try {
      config.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void loadDyedTorchWithItemDisplay() {
    File file = new File(Main.getInstance().getDataFolder(), "dyedTorches.yml");
    if (!file.exists()) return;

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    if (config.getConfigurationSection("torches") == null) return;

    for (String key : config.getConfigurationSection("torches").getKeys(false)) {
      Location loc = config.getLocation("torches." + key + ".location");
      String uuidStr = config.getString("torches." + key + ".uuid");

      if (loc == null || uuidStr == null) continue;

      if (!loc.isChunkLoaded()) {
        loc.getChunk().load();
      }

      UUID uuid = UUID.fromString(uuidStr);
      Entity entity = Bukkit.getEntity(uuid);

      if (entity instanceof ItemDisplay itemDisplay) {
        Main.dyedTorches.put(loc, itemDisplay);
      }
    }
  }
}
