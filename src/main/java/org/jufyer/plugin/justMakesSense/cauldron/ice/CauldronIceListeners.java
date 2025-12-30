package org.jufyer.plugin.justMakesSense.cauldron.ice;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CauldronIceListeners implements Listener {

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    Chunk chunk = event.getChunk();

    int minHeight = chunk.getWorld().getMinHeight();
    int maxHeight = chunk.getWorld().getMaxHeight();
    List<Location> cauldronsWithWater = new ArrayList<>();

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = minHeight; y < maxHeight; y++) {
          Block block = chunk.getBlock(x, y, z);

          if (block.getType() == Material.WATER_CAULDRON) {
            if (isFreezingPossible(block.getBiome(), block.getLocation().getZ())) {
              cauldronsWithWater.add(block.getLocation());
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Chunk chunk = event.getPlayer().getChunk();

    int minHeight = chunk.getWorld().getMinHeight();
    int maxHeight = chunk.getWorld().getMaxHeight();
    List<Location> cauldronsWithWater = new ArrayList<>();

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = minHeight; y < maxHeight; y++) {
          Block block = chunk.getBlock(x, y, z);

          if (block.getType() == Material.WATER_CAULDRON) {
            if (isFreezingPossible(block.getBiome(), block.getLocation().getZ())) {
              cauldronsWithWater.add(block.getLocation());
            }
          }
        }
      }
    }
  }


  private boolean isFreezingPossible(Biome biome, double height) {
    Biome[] biomesWherePossible = {
      Biome.FROZEN_PEAKS,
      Biome.JAGGED_PEAKS,
      Biome.SNOWY_TAIGA,
      Biome.SNOWY_SLOPES,
      Biome.GROVE,
      Biome.FROZEN_RIVER,
      Biome.FROZEN_OCEAN,
      Biome.SNOWY_PLAINS,
      Biome.ICE_SPIKES,
      Biome.SNOWY_BEACH,
      Biome.WINDSWEPT_GRAVELLY_HILLS,
      Biome.WINDSWEPT_FOREST,
      Biome.WINDSWEPT_HILLS,
      Biome.STONY_SHORE,
      Biome.OLD_GROWTH_SPRUCE_TAIGA,
      Biome.TAIGA,
      Biome.OLD_GROWTH_PINE_TAIGA,
      Biome.LUSH_CAVES,
      Biome.THE_VOID,
      Biome.RIVER,
      Biome.WARM_OCEAN,
      Biome.LUKEWARM_OCEAN,
      Biome.DEEP_LUKEWARM_OCEAN,
      Biome.OCEAN,
      Biome.DEEP_OCEAN,
      Biome.CHERRY_GROVE,
      Biome.MEADOW
    };

    Biome[] biomes112_128 = {
      Biome.WINDSWEPT_GRAVELLY_HILLS,
      Biome.WINDSWEPT_HILLS,
      Biome.WINDSWEPT_FOREST,
      Biome.STONY_SHORE
    };
    Biome[] biomes154_168 = {
      Biome.TAIGA,
      Biome.OLD_GROWTH_SPRUCE_TAIGA
    };
    Biome[] biomes192_208 = {
      Biome.OLD_GROWTH_PINE_TAIGA
    };


    for (Biome b : biomesWherePossible) {
      if (b.equals(biome)) {
        for (Biome biome112_128 : biomes112_128) {
          if (biome112_128.equals(biome)) {
            if (112 <= height && height <= 128) {
              return true;
            }else return false;
          }
        }

        for (Biome biome154_168 : biomes154_168) {
          if (biome154_168.equals(biome)) {
            if (154 <= height && height <= 168) {
              return true;
            }else return false;
          }
        }

        for (Biome biome192_208 : biomes192_208) {
          if (biome192_208.equals(biome)) {
            if (192 <= height && height <= 208) {
              return true;
            }else return false;
          }
        }

        return true;
      }
    }
    return false;
  }
}
