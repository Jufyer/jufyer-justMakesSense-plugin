package org.jufyer.plugin.justMakesSense.features.dyedtorches.listener;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.features.dyedtorches.DyedTorchVariant;

public class DyedTorchBlockListener implements Listener {

  /* Registration */
  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    if (!Main.getInstance().isFeatureEnabledInWorld("dyed-torches", event.getWorld())) {
      return;
    }

    if (!Main.scannedChunks.contains(event.getChunk())) {
      Chunk chunk = event.getChunk();

      for (Entity entity : chunk.getEntities()) {
        if (entity instanceof ItemDisplay) {
          if (Main.dyedTorches.containsValue(entity)) {
            for (DyedTorchVariant variant : DyedTorchVariant.values()) {
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
