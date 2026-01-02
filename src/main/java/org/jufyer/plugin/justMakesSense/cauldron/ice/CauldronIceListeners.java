package org.jufyer.plugin.justMakesSense.cauldron.ice;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.jufyer.plugin.justMakesSense.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CauldronIceListeners implements Listener {

  private final List<Location> fillingCauldrons = new ArrayList<>();
  private static final HashMap<Location, UUID> filledCauldronEntities = new HashMap<>();

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
            Block cauldron = block;
            if (isFreezingPossible(block.getBiome(), block.getLocation().getZ())) {
              if (!filledCauldronEntities.containsKey(cauldron.getLocation().toBlockLocation()) && !fillingCauldrons.contains(cauldron.getLocation().toBlockLocation())) {
                cauldronsWithWater.add(cauldron.getLocation());
                fillingCauldrons.add(cauldron.getLocation().toBlockLocation());
                createRunner(cauldron.getLocation().getBlock());
              }
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
            Block cauldron = block;
            if (isFreezingPossible(block.getBiome(), block.getLocation().getZ())) {
              if (!filledCauldronEntities.containsKey(cauldron.getLocation().toBlockLocation()) && !fillingCauldrons.contains(cauldron.getLocation().toBlockLocation())) {
                cauldronsWithWater.add(cauldron.getLocation());
                fillingCauldrons.add(cauldron.getLocation().toBlockLocation());
                createRunner(cauldron.getLocation().getBlock());
              }
            }
          }
        }
      }
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Location loc = event.getBlock().getLocation().toBlockLocation();

    // If the cauldron is broken
    if (event.getBlock().getType() == Material.CAULDRON) {
      removeIceDisplay(loc);
      fillingCauldrons.remove(loc);
    }
  }

  @EventHandler
  public void onBlockPistonExtend(BlockPistonExtendEvent event) {
    handlePistonMove(event.getBlocks(), event.getDirection());
  }

  @EventHandler
  public void onBlockPistonRetract(BlockPistonRetractEvent event) {
    handlePistonMove(event.getBlocks(), event.getDirection());
  }

  private void handlePistonMove(List<Block> blocks, BlockFace direction) {
    HashMap<Location, UUID> moving = new HashMap<>();
    for (Block b : blocks) {
      Location oldLoc = b.getLocation().toBlockLocation();
      if (filledCauldronEntities.containsKey(oldLoc)) {
        UUID id = filledCauldronEntities.remove(oldLoc);
        Location newLoc = oldLoc.clone().add(direction.getDirection()).toBlockLocation();

        Entity entity = Bukkit.getEntity(id);
        if (entity != null) {
          entity.teleport(newLoc.clone().add(0.12, 0.2, 0.12));
        }
        moving.put(newLoc, id);
      }
    }
    filledCauldronEntities.putAll(moving);
  }

  private void removeIceDisplay(Location loc) {
    UUID id = filledCauldronEntities.remove(loc);
    if (id != null) {
      Entity entity = Bukkit.getEntity(id);
      if (entity != null) entity.remove();
    }
  }

  private void createRunner(Block cauldron) {
    new org.bukkit.scheduler.BukkitRunnable() {
      @Override
      public void run() {
        Location loc = cauldron.getLocation().toBlockLocation();
        if (fillingCauldrons.contains(loc)) {
          fillingCauldrons.remove(loc);
          createIceCauldron(cauldron);
        }
      }
    }.runTaskLater(Main.getInstance(), (new Random().nextInt(5) + 1) * 20L * 20L);
  }

  private void createIceCauldron(Block cauldron) {
    cauldron.setType(Material.CAULDRON);
    Location spawnLoc = cauldron.getLocation().add(0.12, 0.2, 0.12);
    BlockDisplay display = (BlockDisplay) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.BLOCK_DISPLAY);
    display.setBlock(Bukkit.createBlockData(Material.ICE));

    display.setTransformation(new Transformation(
      new Vector3f(0, 0, 0),
      new AxisAngle4f(0, 0, 0, 0),
      new Vector3f(0.75f, 0.8f, 0.75f),
      new AxisAngle4f(0, 0, 0, 0)
    ));

    filledCauldronEntities.put(cauldron.getLocation().toBlockLocation(), display.getUniqueId());
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

  // --- Save and Load ---

  public static void saveFilledIceCauldrons() {
    File file = new File(Main.getInstance().getDataFolder(), "filledIceCauldrons.yml");
    YamlConfiguration config = new YamlConfiguration();
    int i = 0;
    for (Map.Entry<Location, UUID> entry : filledCauldronEntities.entrySet()) {
      config.set("cauldrons." + i + ".location", entry.getKey());
      config.set("cauldrons." + i + ".uuid", entry.getValue().toString());
      i++;
    }
    try { config.save(file); } catch (IOException e) { e.printStackTrace(); }
  }

  public static void loadFilledIceCauldrons() {
    File file = new File(Main.getInstance().getDataFolder(), "filledIceCauldrons.yml");
    if (!file.exists()) return;

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    if (config.getConfigurationSection("cauldrons") == null) return;

    for (String key : config.getConfigurationSection("cauldrons").getKeys(false)) {
      try {
        Location loc = config.getLocation("cauldrons." + key + ".location");
        String uuidStr = config.getString("cauldrons." + key + ".uuid");

        // Safety check: ensure the location and world actually exist
        if (loc != null && loc.getWorld() != null && uuidStr != null) {
          filledCauldronEntities.put(loc.toBlockLocation(), UUID.fromString(uuidStr));
        }
      } catch (Exception e) {
        Bukkit.getLogger().warning("Could not load cauldron entry '" + key + "'. Skipping...");
      }
    }
  }
}
