package org.jufyer.plugin.justMakesSense.features.copperhopper.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.features.copperhopper.CopperVariant;

import java.util.*;

public class CopperHopperBlockListener implements Listener {

  /* Block Logic */
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    if (block == null) return;

    if (!Main.getInstance().isFeatureEnabledInWorld("copper-hopper", block.getWorld())) {
      return;
    }

    ItemStack itemStack = event.getItem();

    if (itemStack != null && block.getType().equals(Material.HOPPER)) {
      Hopper hopper = (Hopper) block.getState();
      Location location = block.getLocation().toBlockLocation();
      for (CopperVariant variant : CopperVariant.values()) {
        if (hopper.getPersistentDataContainer().has(variant.getBlockKey(), PersistentDataType.INTEGER)) {
          if (isWaxed(variant)) {
            if (isAxe(itemStack)) {
              event.setCancelled(true);
              switch (variant) {
                case WAXED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.NORMAL.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.NORMAL.getSpeed());
                  hopper.update();
                  break;
                case WAXED_EXPOSED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.EXPOSED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.EXPOSED.getSpeed());
                  hopper.update();
                  break;
                case WAXED_WEATHERED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.WEATHERED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WEATHERED.getSpeed());
                  hopper.update();
                  break;
                case WAXED_OXIDIZED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.OXIDIZED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.OXIDIZED.getSpeed());
                  hopper.update();
                  break;
              }

              if (hopper.getBlockData() instanceof  Directional directional) {
                updateHopperItemDisplayTexture(hopper, !directional.getFacing().equals(BlockFace.DOWN));
                hopper.setTransferCooldown(variant.getSpeed());
              }

              location.getWorld().spawnParticle(Particle.WAX_OFF, location.toBlockLocation().add(0.5, 1 , 0.5), 10);
              return;
            }
          } else if (!isWaxed(variant)) {
            if (itemStack.getType().equals(Material.HONEYCOMB)) {
              event.setCancelled(true);
              switch (variant) {
                case NORMAL:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.WAXED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WAXED.getSpeed());
                  hopper.update();
                  break;
                case EXPOSED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.WAXED_EXPOSED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WAXED_EXPOSED.getSpeed());
                  hopper.update();
                  break;
                case WEATHERED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.WAXED_WEATHERED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WAXED_WEATHERED.getSpeed());
                  hopper.update();
                  break;
                case OXIDIZED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.WAXED_OXIDIZED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WAXED_OXIDIZED.getSpeed());
                  hopper.update();
                  break;
              }

              if (hopper.getBlockData() instanceof  Directional directional) {
                updateHopperItemDisplayTexture(hopper, !directional.getFacing().equals(BlockFace.DOWN));
                hopper.setTransferCooldown(variant.getSpeed());
              }

              location.getWorld().spawnParticle(Particle.WAX_ON, location.toBlockLocation().add(0.5, 1 , 0.5), 10);
              return;
            }else if (isAxe(itemStack)) {
              event.setCancelled(true);
              switch (variant) {
                case EXPOSED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.NORMAL.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.NORMAL.getSpeed());
                  hopper.update();
                  break;
                case WEATHERED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.EXPOSED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.EXPOSED.getSpeed());
                  hopper.update();
                  break;
                case OXIDIZED:
                  hopper.getPersistentDataContainer().remove(variant.getBlockKey());
                  hopper.getPersistentDataContainer().set(CopperVariant.WEATHERED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WEATHERED.getSpeed());
                  hopper.update();
                  break;
              }

              if (hopper.getBlockData() instanceof  Directional directional) {
                updateHopperItemDisplayTexture(hopper, !directional.getFacing().equals(BlockFace.DOWN));
                hopper.setTransferCooldown(variant.getSpeed());
              }
              if (variant != CopperVariant.NORMAL) {
                location.getWorld().spawnParticle(Particle.SCRAPE, location.toBlockLocation().add(0.5, 1 , 0.5), 10);
              }
              return;
            }
          }
        }
      }
    }
  }

  //Random Ticks abhänig
  // 64/1125 chanche für pre Oxidation --> ca. 20 Minutes
  // check in 4 blöcke range
  // Wenn copper hopper mit < level oxi --> pre oxi ends
  // int non_waxed = alle non waxed in 4 blöcke range
  // int non_waxed_higher = alle non waxed in 4 blöcke range mit > oxi level
  // int base = non_waxed + 1 / non_waxed_higher + 1
  // double modifier = 0,75 (wenn normal copper hopper ) || 1 (wenn exposed oder wheaterd copper hopper)
  // Chanche am ende:
  // base * ( modifier * modifier )

//  private void oxidationCheck(Hopper hopper) {
//    Random random = new Random();
//    int radius = 4;
//    int chancePreOxidation = random.nextInt(1125);
//    if (chancePreOxidation < 64) {
//      Location location = hopper.getLocation();
//      for (int x = -radius; x <= radius; x++) {
//        for (int y = -radius; y <= radius; y++) {
//          for (int z = -radius; z <= radius; z++) {
//            Block block = location.clone().add(x, y, z).getBlock();
//            if (Main.copperHoppers.get(hopper) != null) {
//              for (CopperVariant copperVariant : CopperVariant.values()) {
//                if (hopper.getPersistentDataContainer().has(copperVariant.getBlockKey(), PersistentDataType.INTEGER)) {
//                  if (copperVariant.equals(CopperVariant.NORMAL) || copperVariant.equals(CopperVariant.WEATHERED) || copperVariant.equals(CopperVariant.EXPOSED)) {
//                    if (block.getType().equals(Material.HOPPER)) {
//                      for (CopperVariant blockVariant : CopperVariant.values()) {
//                        Hopper blockHopper = (Hopper) block.getState();
//                        if (blockHopper.getPersistentDataContainer().has(blockVariant.getBlockKey(), PersistentDataType.INTEGER)) {
//                          if (!isWaxed(copperVariant) && isWaxed(blockVariant)) {
//                            if (blockVariant.getCustomModelData() >= copperVariant.getCustomModelData()) {
//                              double non_waxed = 0;
//                              double non_waxed_higher = 0;
//                              for (int x1 = -radius; x1 <= radius; x1++) {
//                                for (int y1 = -radius; y1 <= radius; y1++) {
//                                  for (int z1 = -radius; z1 <= radius; z1++) {
//                                    Block allBlocks = location.clone().add(x, y, z).getBlock();
//                                    if (allBlocks.getType().equals(Material.HOPPER)) {
//                                      Hopper allBlocksHopper = (Hopper) allBlocks.getState();
//                                      for (CopperVariant allBlocksVariant : CopperVariant.values()) {
//                                        if (allBlocksHopper.getPersistentDataContainer().has(allBlocksVariant.getBlockKey(), PersistentDataType.INTEGER)) {
//                                          if (!isWaxed(allBlocksVariant)) {
//                                            non_waxed++;
//                                            if (allBlocksVariant.getCustomModelData() < copperVariant.getCustomModelData()) {
//                                              non_waxed_higher++;
//                                            }
//                                          }
//                                        }
//                                      }
//                                    }
//                                  }
//                                }
//                              }
//
//                              non_waxed++;
//                              non_waxed_higher++;
//                              double base = non_waxed / non_waxed_higher;
//                              double modifier = 1.0;
//
//                              if (copperVariant.equals(CopperVariant.NORMAL)) {
//                                modifier = 0.75;
//                              }
//
//                              modifier = modifier * modifier;
//                              double chance = base * modifier;
//                              if (random.nextDouble() < chance) {
//                                CopperVariant variant = copperVariant;
//                                switch (variant) {
//                                  case NORMAL:
//                                    hopper.getPersistentDataContainer().remove(variant.getBlockKey());
//                                    hopper.getPersistentDataContainer().set(CopperVariant.EXPOSED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.EXPOSED.getSpeed());
//                                    hopper.update();
//                                    break;
//                                  case EXPOSED:
//                                    hopper.getPersistentDataContainer().remove(variant.getBlockKey());
//                                    hopper.getPersistentDataContainer().set(CopperVariant.WEATHERED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.WEATHERED.getSpeed());
//                                    hopper.update();
//                                    break;
//                                  case WEATHERED:
//                                    hopper.getPersistentDataContainer().remove(variant.getBlockKey());
//                                    hopper.getPersistentDataContainer().set(CopperVariant.OXIDIZED.getBlockKey(), PersistentDataType.INTEGER, CopperVariant.OXIDIZED.getSpeed());
//                                    hopper.update();
//                                    break;
//                                }
//
//                                if (hopper.getBlockData() instanceof  Directional directional) {
//                                  updateHopperItemDisplayTexture(hopper, !directional.getFacing().equals(BlockFace.DOWN));
//                                }
//                              }
//                            }
//                          }
//                        }
//                      }
//                    }
//                  }
//                }
//              }
//            }
//          }
//        }
//      }
//    }
//  }

  public static void oxidationCheck(Hopper hopper) {
    Random random = new Random();

    if (new Random().nextInt(1365) != 0) return;

    int randInteger = random.nextInt(1125);
    if (randInteger >= 64) return;

    Location loc = hopper.getLocation();
    World world = loc.getWorld();
    if (world == null) return;

    CopperVariant currentVariant = getCopperVariant(hopper);
    if (currentVariant == null || isWaxed(currentVariant)) return;
    if (currentVariant == CopperVariant.OXIDIZED) return;

    int radius = 4;
    double nonWaxed = 0;
    double nonWaxedHigher = 0;

    for (int x = -radius; x <= radius; x++) {
      for (int y = -radius; y <= radius; y++) {
        for (int z = -radius; z <= radius; z++) {
          Block block = world.getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);

          if (block.getType() == Material.HOPPER) {
            Hopper neighborHopper = (Hopper) block.getState();
            CopperVariant neighborVariant = getCopperVariant(neighborHopper);

            if (neighborVariant != null && !isWaxed(neighborVariant)) {
              nonWaxed++;
              if (neighborVariant.getCustomModelData() > currentVariant.getCustomModelData()) {
                nonWaxedHigher++;
              }
            }
          }
        }
      }
    }

    nonWaxed++;
    nonWaxedHigher++;

    double a = nonWaxed;
    double b = nonWaxedHigher;
    double c = (b + 1.0) / (a + 1.0);
    double m = (currentVariant == CopperVariant.NORMAL) ? 0.75 : 1.0;

    double chance = m * (c * c); // mc^2

    if (random.nextDouble() < chance) {
      updateToNextStage(hopper, currentVariant);
    }
  }

  /* Registration */
  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    if (!Main.getInstance().isFeatureEnabledInWorld("copper-hopper", event.getWorld())) {
      return;
    }

    if (!Main.scannedChunks.contains(event.getChunk())) {
      Chunk chunk = event.getChunk();

      for (BlockState state : chunk.getTileEntities()) {
        if (state.getBlock().getType().equals(Material.HOPPER)) {
          Hopper hopper = (Hopper) state;
          for (CopperVariant variant : CopperVariant.values()){
            if (hopper.getPersistentDataContainer().has(variant.getBlockKey())) {
              if (!Main.loadedCopperHoppers.contains(hopper.getLocation())) {
                Main.loadedCopperHoppers.add(hopper.getLocation());
              }
            }
          }
        }
      }
    }
  }

  /* Helper Methods */
  private static boolean isWaxed(CopperVariant variant) {
    if (variant.equals(CopperVariant.WAXED)
      || variant.equals(CopperVariant.WAXED_EXPOSED)
      || variant.equals(CopperVariant.WAXED_WEATHERED)
      || variant.equals(CopperVariant.WAXED_OXIDIZED)) {
      return true;
    }

    return false;
  }

  private boolean isAxe(ItemStack itemStack) {

    if (itemStack.getType().equals(Material.WOODEN_AXE)) return true;
    if (itemStack.getType().equals(Material.STONE_AXE)) return true;
    if (itemStack.getType().equals(Material.COPPER_AXE)) return true;
    if (itemStack.getType().equals(Material.IRON_AXE)) return true;
    if (itemStack.getType().equals(Material.GOLDEN_AXE)) return true;
    if (itemStack.getType().equals(Material.DIAMOND_AXE)) return true;
    if (itemStack.getType().equals(Material.NETHERITE_AXE)) return true;

    return false;
  }

  private static void updateHopperItemDisplayTexture(Hopper hopper, Boolean sideView) {
    if (Main.copperHoppers.get(hopper.getLocation()) != null) {
      ItemDisplay display = Main.copperHoppers.get(hopper.getLocation());
      ItemStack itemStack = display.getItemStack();
      ItemMeta itemMeta = itemStack.getItemMeta();
      Float customModelDataFloat;
      var customData = itemMeta.getCustomModelDataComponent();

      for (CopperVariant variant : CopperVariant.values()) {
        if (hopper.getPersistentDataContainer().has(variant.getBlockKey(), PersistentDataType.INTEGER)) {
          if (sideView) {
            customModelDataFloat = variant.getCustomModelDataSide();
            customData.setFloats(List.of(customModelDataFloat));
            itemMeta.setCustomModelDataComponent(customData);
            itemStack.setItemMeta(itemMeta);
            display.setItemStack(itemStack);
          }else {
            customModelDataFloat = variant.getCustomModelData();
            customData.setFloats(List.of(customModelDataFloat));
            itemMeta.setCustomModelDataComponent(customData);
            itemStack.setItemMeta(itemMeta);
            display.setItemStack(itemStack);
          }
        }
      }
    }
  }

  // Hilfsmethode um PDC-Iterationen zu vermeiden
  private static CopperVariant getCopperVariant(Hopper hopper) {
    for (CopperVariant variant : CopperVariant.values()) {
      if (hopper.getPersistentDataContainer().has(variant.getBlockKey(), PersistentDataType.INTEGER)) {
        return variant;
      }
    }
    return null;
  }

  private static void updateToNextStage(Hopper hopper, CopperVariant current) {
    CopperVariant next = null;
    if (current == CopperVariant.NORMAL) next = CopperVariant.EXPOSED;
    else if (current == CopperVariant.EXPOSED) next = CopperVariant.WEATHERED;
    else if (current == CopperVariant.WEATHERED) next = CopperVariant.OXIDIZED;

    if (next != null) {
      hopper.getPersistentDataContainer().remove(current.getBlockKey());
      hopper.getPersistentDataContainer().set(next.getBlockKey(), PersistentDataType.INTEGER, next.getSpeed());
      hopper.update();

      if (hopper.getBlockData() instanceof Directional directional) {
        updateHopperItemDisplayTexture(hopper, !directional.getFacing().equals(BlockFace.DOWN));
      }
    }
  }
}
