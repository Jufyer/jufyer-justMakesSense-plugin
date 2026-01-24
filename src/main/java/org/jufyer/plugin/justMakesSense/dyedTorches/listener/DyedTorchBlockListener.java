package org.jufyer.plugin.justMakesSense.dyedTorches.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
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
import org.jufyer.plugin.justMakesSense.copperHoppper.CopperVariant;
import org.jufyer.plugin.justMakesSense.dyedTorches.DyedTorchesVariant;

import java.util.List;
import java.util.Random;

public class DyedTorchBlockListener implements Listener {

  /* Registration */
  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    if (!Main.scannedChunks.contains(event.getChunk())) {
      Chunk chunk = event.getChunk();

      for (Entity entity : chunk.getEntities()) {
        if (entity instanceof ItemDisplay) {
          if (Main.dyedTorches.containsValue(entity)) {
            for (DyedTorchesVariant variant : DyedTorchesVariant.values()) {
              if (entity.getPersistentDataContainer().has(variant.getBlockKey()) || entity.getPersistentDataContainer().has(variant.getWallBlockKey())) {
                if (!Main.dyedTorches.containsKey(entity.getLocation().getBlock().getLocation())) {
                  Main.dyedTorches.put(entity.getLocation().getBlock().getLocation(), (ItemDisplay) entity);
                }
              }
            }
          }
        }
      }
    }
  }
}
