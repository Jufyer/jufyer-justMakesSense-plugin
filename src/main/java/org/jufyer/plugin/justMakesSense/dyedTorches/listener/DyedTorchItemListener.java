package org.jufyer.plugin.justMakesSense.dyedTorches.listener;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.TileState;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.copperHoppper.CopperVariant;
import org.jufyer.plugin.justMakesSense.copperHoppper.HopperRegistry;
import org.jufyer.plugin.justMakesSense.dyedTorches.DyedTorchesRegistry;
import org.jufyer.plugin.justMakesSense.dyedTorches.DyedTorchesVariant;

import java.util.*;

public class DyedTorchItemListener implements Listener {
  private final Map<Player, Long> lastPlacedBlockTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();
    if (item != null && item.hasItemMeta()) {
      ItemMeta meta = item.getItemMeta();
      for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
        if (meta != null && meta.getPersistentDataContainer().has(variant.getItemKey())) {
          if (event.getAction().isRightClick()) {
            if ((!event.getPlayer().isSneaking()) && event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof TileState) return;
            Player player = event.getPlayer();
            long currentTime = System.currentTimeMillis();

            long lastPlacedTime = lastPlacedBlockTimes.getOrDefault(player, 0L);

            if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
              if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR && !event.getClickedBlock().isLiquid()) {
                if (event.getBlockFace().equals(BlockFace.UP)){
                  Location loc = event.getClickedBlock().getLocation().add(0, 1, 0);
                  createBlock(variant, loc, BlockFace.UP);
                  lastPlacedBlockTimes.put(player, currentTime);
                  if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                  }
                }else if (event.getBlockFace().equals(BlockFace.WEST)){
                  Location loc = event.getClickedBlock().getLocation().add(-1, 0, 0);
                  createBlock(variant, loc, BlockFace.WEST);
                  lastPlacedBlockTimes.put(player, currentTime);
                  if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                  }
                }else if (event.getBlockFace().equals(BlockFace.EAST)){
                  Location loc = event.getClickedBlock().getLocation().add(1, 0, 0);
                  createBlock(variant, loc, BlockFace.EAST);
                  lastPlacedBlockTimes.put(player, currentTime);
                  if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                  }
                }if (event.getBlockFace().equals(BlockFace.NORTH)){
                  Location loc = event.getClickedBlock().getLocation().add(0, 0, -1);
                  createBlock(variant, loc, BlockFace.NORTH);
                  lastPlacedBlockTimes.put(player, currentTime);
                  if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                  }
                }else if (event.getBlockFace().equals(BlockFace.SOUTH)){
                  Location loc = event.getClickedBlock().getLocation().add(0, 0, 1);
                  createBlock(variant, loc, BlockFace.SOUTH);
                  lastPlacedBlockTimes.put(player, currentTime);
                  if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                  }
                }
                player.swingMainHand();
              }
            } else {
              event.setCancelled(true);
            }
          }
        }
      }
    }
  }

//  @EventHandler
//  public void onBlockBreak(BlockBreakEvent event) {
//    if (event.getBlock().getType().equals(Material.TORCH) || event.getBlock().getType().equals(Material.WALL_TORCH)) {
//      Location blockLoc = event.getBlock().getLocation();
//      if (Main.dyedTorches.containsKey(blockLoc)) {
//        ItemDisplay display = Main.dyedTorches.get(blockLoc);
//        for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
//          if (display.getPersistentDataContainer().has(variant.getBlockKey()) || display.getPersistentDataContainer().has(variant.getWallBlockKey())) {
//            Main.dyedTorches.get(blockLoc).remove();
//            Main.dyedTorches.remove(blockLoc);
//
//            blockLoc.getWorld().dropItemNaturally(blockLoc.add(0,0.3,0), DyedTorchesRegistry.DyedTorchItem1to1(variant));
//          }
//        }
//      }
//    }
//  }
//
//  @EventHandler
//  public void onBlockDropItem(BlockDropItemEvent event) {
//    Location blockLoc = event.getBlock().getLocation().toBlockLocation();
//    boolean rmItems = false;
//    List<Item> itemsToRm = new ArrayList<>();
//    for (Item item : event.getItems()) {
//      if (item.getItemStack().getType().equals(Material.TORCH)) {
//        if (Main.dyedTorches.containsKey(blockLoc)) {
//          ItemDisplay display = Main.dyedTorches.get(blockLoc);
//          for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
//            if (display.getPersistentDataContainer().has(variant.getBlockKey()) || display.getPersistentDataContainer().has(variant.getWallBlockKey())) {
//              Main.dyedTorches.get(blockLoc).remove();
//              Main.dyedTorches.remove(blockLoc);
//
//              blockLoc.getWorld().dropItemNaturally(blockLoc.clone().add(0, 0.3, 0), DyedTorchesRegistry.DyedTorchItem1to1(variant));
//              rmItems = true;
//              itemsToRm.add(item);
//              break;
//            }
//          }
//        }
//      }
//    }
//    if (rmItems) {
//      for (Item itemToRm : itemsToRm) {
//        event.getItems().remove(itemToRm);
//      }
//      itemsToRm.clear();
//    }
//  }

//  @EventHandler
//  public void onBlockPhysics(BlockPhysicsEvent event) {
//    if (event.getSourceBlock().getType().equals(Material.TORCH)) return;
//    Block block = event.getBlock();
//    if (block.getType() != Material.TORCH && block.getType() != Material.WALL_TORCH) {
//      return;
//    }
//
//    Location loc = block.getLocation().toBlockLocation();
//
//    if (!Main.dyedTorches.containsKey(loc)) return;
//
//    ItemDisplay display = Main.dyedTorches.get(loc);
//    for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
//      if (display.getPersistentDataContainer().has(variant.getBlockKey()) || display.getPersistentDataContainer().has(variant.getWallBlockKey())) {
//
//        block.setType(Material.AIR, false);
//        display.remove();
//        Main.dyedTorches.remove(loc);
//
//        block.getWorld().dropItemNaturally(loc.clone().add(0, 0.3, 0), DyedTorchesRegistry.DyedTorchItem1to1(variant));
//        break;
//      }
//    }
//  }

  private void destroyDyedTorch(Location loc) {
    if (!Main.dyedTorches.containsKey(loc)) return;

    ItemDisplay display = Main.dyedTorches.get(loc);
    Block block = loc.getBlock();

    for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
      if (display.getPersistentDataContainer().has(variant.getBlockKey()) || display.getPersistentDataContainer().has(variant.getWallBlockKey())) {
        block.setType(Material.AIR, false);
        display.remove();
        Main.dyedTorches.remove(loc);

        block.getWorld().dropItemNaturally(loc.clone().add(0, 0.3, 0), DyedTorchesRegistry.DyedTorchItem1to1(variant));
        return;
      }
    }
  }

  @EventHandler
  public void onBlockFromTo(BlockFromToEvent e) {
    if (e.getToBlock().getType() == Material.TORCH || e.getToBlock().getType() == Material.WALL_TORCH) {
      destroyDyedTorch(e.getToBlock().getLocation());
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Block block = e.getBlock();

    if (block.getType() == Material.TORCH || block.getType() == Material.WALL_TORCH) {
      destroyDyedTorch(block.getLocation());
      return;
    }

    List<Location> torches = getTorchLocations(block);
    if (!torches.isEmpty()) {
      e.setCancelled(true);
      torches.forEach(this::destroyDyedTorch);
    }
  }


  @EventHandler
  public void onBlockPistonExtend(BlockPistonExtendEvent e) {
    List<Location> torches = new ArrayList<>();

    for (Block b : e.getBlocks()) {
      torches.addAll(getTorchLocations(b));
    }

    if (!torches.isEmpty()) {
      e.setCancelled(true);
      torches.forEach(this::destroyDyedTorch);
    }
  }

  @EventHandler
  public void onBlockPistonRetract(BlockPistonRetractEvent e) {
    List<Location> torches = new ArrayList<>();

    for (Block b : e.getBlocks()) {
      torches.addAll(getTorchLocations(b));
    }

    if (!torches.isEmpty()) {
      e.setCancelled(true);
      torches.forEach(this::destroyDyedTorch);
    }
  }

  private List<Location> getTorchLocations(Block b) {
    List<Location> locations = new ArrayList<>();

    // Torch oben
    if (b.getRelative(BlockFace.UP).getType() == Material.TORCH) {
      locations.add(b.getRelative(BlockFace.UP).getLocation());
    }

    // Wall torches
    BlockFace[] faces = {
      BlockFace.WEST, BlockFace.EAST,
      BlockFace.SOUTH, BlockFace.NORTH
    };

    for (BlockFace face : faces) {
      if (b.getRelative(face).getType() == Material.WALL_TORCH) {
        locations.add(b.getRelative(face).getLocation());
      }
    }

    return locations;
  }


  private void createBlock(DyedTorchesVariant variant, Location loc, BlockFace blockFace) {
    if (blockFace == BlockFace.NORTH){
      DyedTorchesRegistry.spawnDyedTorchDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.EAST) {
      DyedTorchesRegistry.spawnDyedTorchDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.SOUTH) {
      DyedTorchesRegistry.spawnDyedTorchDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.WEST) {
      DyedTorchesRegistry.spawnDyedTorchDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.UP) {
      DyedTorchesRegistry.spawnDyedTorchDisplay(variant, loc, blockFace);
    }
  }
}
