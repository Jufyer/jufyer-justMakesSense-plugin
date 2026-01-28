package org.jufyer.plugin.justMakesSense.features.copperhopper.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.features.copperhopper.CopperVariant;
import org.jufyer.plugin.justMakesSense.features.copperhopper.CopperHopperRegistry;

import java.util.HashMap;
import java.util.Map;

public class CopperHopperItemListener implements Listener {
  private final Map<Player, Long> lastPlacedBlockTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();
    if (item != null && item.hasItemMeta()) {
      ItemMeta meta = item.getItemMeta();
      for (CopperVariant variant : CopperVariant.values()) {
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
                }else if (event.getBlockFace().equals(BlockFace.DOWN)){
                  Location loc = event.getClickedBlock().getLocation().add(0, -1, 0);
                  createBlock(variant, loc, BlockFace.DOWN);
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

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.getBlock().getType().equals(Material.HOPPER)) {
//      Location blockLoc = event.getBlock().getLocation();
//      for (ItemDisplay itemDisplay : blockLoc.getWorld().getEntitiesByClass(ItemDisplay.class)) {
//        if (itemDisplay.getLocation().getBlockX() == blockLoc.getBlockX() &&
//          itemDisplay.getLocation().getBlockY() == blockLoc.getBlockY() &&
//          itemDisplay.getLocation().getBlockZ() == blockLoc.getBlockZ()) {
//          for (CopperVariant variant : CopperVariant.values()) {
//            ItemMeta meta = itemDisplay.getItemStack().getItemMeta();
//            if (meta != null && meta.getPersistentDataContainer().has(variant.getItemKey())) {
//              itemDisplay.remove();
//              itemDisplay.getWorld().dropItemNaturally(itemDisplay.getLocation(), HopperRegistry.CopperHopperItem1to1(variant));
//            }
//          }
//        }
//      }
      Hopper hopper = (Hopper) event.getBlock().getState();
      if (Main.copperHoppers.containsKey(hopper.getLocation())) {
        for (CopperVariant variant : CopperVariant.values()) {
          if (hopper.getPersistentDataContainer().has(variant.getBlockKey())) {
            Main.copperHoppers.get(hopper.getLocation()).remove();
            Main.copperHoppers.remove(hopper.getLocation());
            hopper.getLocation().getWorld().dropItemNaturally(hopper.getLocation(), CopperHopperRegistry.CopperHopperItem1to1(variant));
            break;
          }
        }
      }
    }
  }

  private void createBlock(CopperVariant variant, Location loc, BlockFace blockFace) {
    if (blockFace == BlockFace.NORTH){
      CopperHopperRegistry.spawnCopperHopperDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.EAST) {
      CopperHopperRegistry.spawnCopperHopperDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.SOUTH) {
      CopperHopperRegistry.spawnCopperHopperDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.WEST) {
      CopperHopperRegistry.spawnCopperHopperDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.UP) {
      CopperHopperRegistry.spawnCopperHopperDisplay(variant, loc, blockFace);
    } else if (blockFace == BlockFace.DOWN) {
      CopperHopperRegistry.spawnCopperHopperDisplay(variant, loc, blockFace);
    }
  }
}
