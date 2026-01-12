package org.jufyer.plugin.justMakesSense.cauldron.removeDye;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CauldronRemoveDyeListeners implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getClickedBlock() == null || !event.getAction().isRightClick()) return;

    Block block = event.getClickedBlock();
    if (block.getType() == Material.WATER_CAULDRON) {
      BlockData blockData = block.getBlockData();
      if (blockData instanceof Levelled) {
        Levelled levelled = (Levelled) blockData;
        if (levelled.getLevel() > 0) {
          ItemStack item = event.getItem();
          if (item == null || item.getType() == Material.AIR) return;

          Player player = event.getPlayer();
          Material itemMaterial = item.getType();

          if (isDye(itemMaterial)) {
            removeDyeDye(player, item, event, levelled, block);
          } else if (isDyedBed(itemMaterial)) {
            removeBedDye(player, item, event, levelled, block);
          } else if (isDyedCandle(itemMaterial)) {
            removeCandleDye(player, item, event, levelled, block);
          } else if (isDyedWool(itemMaterial)) {
            removeWoolDye(player, item, event, levelled, block);
          } else if (isDyedCarpet(itemMaterial)) {
            removeCarpetDye(player, item, event, levelled, block);
          } else if (isDyedTerracotta(itemMaterial)) {
            removeTerracottaDye(player, item, event, levelled, block);
          } else if (isDyedConcrete(itemMaterial)) {
            removeConcreteDye(player, item, event, levelled, block);
          } else if (isDyedConcretePowder(itemMaterial)) {
            removeConcretePowderDye(player, item, event, levelled, block);
          } else if (isDyedStainedGlass(itemMaterial)) {
            removeStainedGlassDye(player, item, event, levelled, block);
          } else if (isDyedStainedGlassPane(itemMaterial)) {
            removeStainedGlassPaneDye(player, item, event, levelled, block);
          } else if (isDyedBundle(itemMaterial)) {
            removeBundleDye(player, item, event, levelled, block);
          } else if (isDyedHarness(itemMaterial)) {
            removeHarnessDye(player, item, event, levelled, block);
          }
        }
      }
    }
  }

  private boolean isDye(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_DYE,
      Material.GRAY_DYE,
      Material.BLACK_DYE,
      Material.BROWN_DYE,
      Material.RED_DYE,
      Material.YELLOW_DYE,
      Material.ORANGE_DYE,
      Material.LIME_DYE,
      Material.GREEN_DYE,
      Material.CYAN_DYE,
      Material.LIGHT_BLUE_DYE,
      Material.BLUE_DYE,
      Material.PURPLE_DYE,
      Material.MAGENTA_DYE,
      Material.PINK_DYE
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeDyeDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_DYE));

    event.setCancelled(true);
  }

  private boolean isDyedBed(Material itemMaterial) {
    Material[] colouredBeds = {
      Material.LIGHT_GRAY_BED,
      Material.GRAY_BED,
      Material.BLACK_BED,
      Material.BROWN_BED,
      Material.RED_BED,
      Material.YELLOW_BED,
      Material.ORANGE_BED,
      Material.LIME_BED,
      Material.GREEN_BED,
      Material.CYAN_BED,
      Material.LIGHT_BLUE_BED,
      Material.BLUE_BED,
      Material.PURPLE_BED,
      Material.MAGENTA_BED,
      Material.PINK_BED
    };

    for (Material mat : colouredBeds) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeBedDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_BED));

    event.setCancelled(true);
  }

  private boolean isDyedCandle(Material itemMaterial) {
    Material[] colouredCandle = {
      Material.WHITE_CANDLE,
      Material.LIGHT_GRAY_CANDLE,
      Material.GRAY_CANDLE,
      Material.BLACK_CANDLE,
      Material.BROWN_CANDLE,
      Material.RED_CANDLE,
      Material.ORANGE_CANDLE,
      Material.YELLOW_CANDLE,
      Material.LIME_CANDLE,
      Material.GREEN_CANDLE,
      Material.CYAN_CANDLE,
      Material.LIGHT_BLUE_CANDLE,
      Material.BLUE_CANDLE,
      Material.PURPLE_CANDLE,
      Material.MAGENTA_CANDLE,
      Material.PINK_CANDLE
    };

    for (Material mat : colouredCandle) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeCandleDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.CANDLE));

    event.setCancelled(true);
  }

  private boolean isDyedWool(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_WOOL,
      Material.GRAY_WOOL,
      Material.BLACK_WOOL,
      Material.BROWN_WOOL,
      Material.RED_WOOL,
      Material.YELLOW_WOOL,
      Material.ORANGE_WOOL,
      Material.LIME_WOOL,
      Material.GREEN_WOOL,
      Material.CYAN_WOOL,
      Material.LIGHT_BLUE_WOOL,
      Material.BLUE_WOOL,
      Material.PURPLE_WOOL,
      Material.MAGENTA_WOOL,
      Material.PINK_WOOL
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeWoolDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_WOOL));

    event.setCancelled(true);
  }

  private boolean isDyedCarpet(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_CARPET,
      Material.GRAY_CARPET,
      Material.BLACK_CARPET,
      Material.BROWN_CARPET,
      Material.RED_CARPET,
      Material.YELLOW_CARPET,
      Material.ORANGE_CARPET,
      Material.LIME_CARPET,
      Material.GREEN_CARPET,
      Material.CYAN_CARPET,
      Material.LIGHT_BLUE_CARPET,
      Material.BLUE_CARPET,
      Material.PURPLE_CARPET,
      Material.MAGENTA_CARPET,
      Material.PINK_CARPET
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeCarpetDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_CARPET));

    event.setCancelled(true);
  }

  private boolean isDyedTerracotta(Material itemMaterial) {
    Material[] colouredWool = {
      Material.WHITE_TERRACOTTA,
      Material.LIGHT_GRAY_TERRACOTTA,
      Material.GRAY_TERRACOTTA,
      Material.BLACK_TERRACOTTA,
      Material.BROWN_TERRACOTTA,
      Material.RED_TERRACOTTA,
      Material.YELLOW_TERRACOTTA,
      Material.ORANGE_TERRACOTTA,
      Material.LIME_TERRACOTTA,
      Material.GREEN_TERRACOTTA,
      Material.CYAN_TERRACOTTA,
      Material.LIGHT_BLUE_TERRACOTTA,
      Material.BLUE_TERRACOTTA,
      Material.PURPLE_TERRACOTTA,
      Material.MAGENTA_TERRACOTTA,
      Material.PINK_TERRACOTTA
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeTerracottaDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.TERRACOTTA));

    event.setCancelled(true);
  }

  private boolean isDyedConcrete(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_CONCRETE,
      Material.GRAY_CONCRETE,
      Material.BLACK_CONCRETE,
      Material.BROWN_CONCRETE,
      Material.RED_CONCRETE,
      Material.YELLOW_CONCRETE,
      Material.ORANGE_CONCRETE,
      Material.LIME_CONCRETE,
      Material.GREEN_CONCRETE,
      Material.CYAN_CONCRETE,
      Material.LIGHT_BLUE_CONCRETE,
      Material.BLUE_CONCRETE,
      Material.PURPLE_CONCRETE,
      Material.MAGENTA_CONCRETE,
      Material.PINK_CONCRETE
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeConcreteDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_CONCRETE));

    event.setCancelled(true);
  }

  private boolean isDyedConcretePowder(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_CONCRETE_POWDER,
      Material.GRAY_CONCRETE_POWDER,
      Material.BLACK_CONCRETE_POWDER,
      Material.BROWN_CONCRETE_POWDER,
      Material.RED_CONCRETE_POWDER,
      Material.YELLOW_CONCRETE_POWDER,
      Material.ORANGE_CONCRETE_POWDER,
      Material.LIME_CONCRETE_POWDER,
      Material.GREEN_CONCRETE_POWDER,
      Material.CYAN_CONCRETE_POWDER,
      Material.LIGHT_BLUE_CONCRETE_POWDER,
      Material.BLUE_CONCRETE_POWDER,
      Material.PURPLE_CONCRETE_POWDER,
      Material.MAGENTA_CONCRETE_POWDER,
      Material.PINK_CONCRETE_POWDER
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeConcretePowderDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_CONCRETE_POWDER));

    event.setCancelled(true);
  }

  private boolean isDyedStainedGlass(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_STAINED_GLASS,
      Material.GRAY_STAINED_GLASS,
      Material.BLACK_STAINED_GLASS,
      Material.BROWN_STAINED_GLASS,
      Material.RED_STAINED_GLASS,
      Material.YELLOW_STAINED_GLASS,
      Material.ORANGE_STAINED_GLASS,
      Material.LIME_STAINED_GLASS,
      Material.GREEN_STAINED_GLASS,
      Material.CYAN_STAINED_GLASS,
      Material.LIGHT_BLUE_STAINED_GLASS,
      Material.BLUE_STAINED_GLASS,
      Material.PURPLE_STAINED_GLASS,
      Material.MAGENTA_STAINED_GLASS,
      Material.PINK_STAINED_GLASS
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeStainedGlassDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_STAINED_GLASS));

    event.setCancelled(true);
  }

  private boolean isDyedStainedGlassPane(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_STAINED_GLASS_PANE,
      Material.GRAY_STAINED_GLASS_PANE,
      Material.BLACK_STAINED_GLASS_PANE,
      Material.BROWN_STAINED_GLASS_PANE,
      Material.RED_STAINED_GLASS_PANE,
      Material.YELLOW_STAINED_GLASS_PANE,
      Material.ORANGE_STAINED_GLASS_PANE,
      Material.LIME_STAINED_GLASS_PANE,
      Material.GREEN_STAINED_GLASS_PANE,
      Material.CYAN_STAINED_GLASS_PANE,
      Material.LIGHT_BLUE_STAINED_GLASS_PANE,
      Material.BLUE_STAINED_GLASS_PANE,
      Material.PURPLE_STAINED_GLASS_PANE,
      Material.MAGENTA_STAINED_GLASS_PANE,
      Material.PINK_STAINED_GLASS_PANE
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeStainedGlassPaneDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));

    event.setCancelled(true);
  }

  private boolean isDyedBundle(Material itemMaterial) {
    Material[] colouredWool = {
      Material.WHITE_BUNDLE,
      Material.LIGHT_GRAY_BUNDLE,
      Material.GRAY_BUNDLE,
      Material.BLACK_BUNDLE,
      Material.BROWN_BUNDLE,
      Material.RED_BUNDLE,
      Material.YELLOW_BUNDLE,
      Material.ORANGE_BUNDLE,
      Material.LIME_BUNDLE,
      Material.GREEN_BUNDLE,
      Material.CYAN_BUNDLE,
      Material.LIGHT_BLUE_BUNDLE,
      Material.BLUE_BUNDLE,
      Material.PURPLE_BUNDLE,
      Material.MAGENTA_BUNDLE,
      Material.PINK_BUNDLE
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeBundleDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.BUNDLE));

    event.setCancelled(true);
  }

  private boolean isDyedHarness(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_HARNESS,
      Material.GRAY_HARNESS,
      Material.BLACK_HARNESS,
      Material.BROWN_HARNESS,
      Material.RED_HARNESS,
      Material.YELLOW_HARNESS,
      Material.ORANGE_HARNESS,
      Material.LIME_HARNESS,
      Material.GREEN_HARNESS,
      Material.CYAN_HARNESS,
      Material.LIGHT_BLUE_HARNESS,
      Material.BLUE_HARNESS,
      Material.PURPLE_HARNESS,
      Material.MAGENTA_HARNESS,
      Material.PINK_HARNESS
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  private void removeHarnessDye(Player player, ItemStack item, PlayerInteractEvent event, Levelled levelled, Block block) {
    PlayerInventory inventory = player.getInventory();
    item.setAmount(item.getAmount() - 1);
    if (item.getAmount() <= 0) {
      if (event.getHand() == null) return;
      inventory.setItem(event.getHand(), null);
    }

    if (levelled.getLevel() == 1) {
      block.setType(Material.CAULDRON);
    }else {
      levelled.setLevel(levelled.getLevel() - 1);
      block.setBlockData(levelled); // Update visual state
    }

    player.getInventory().addItem(new ItemStack(Material.WHITE_HARNESS));

    event.setCancelled(true);
  }
}
