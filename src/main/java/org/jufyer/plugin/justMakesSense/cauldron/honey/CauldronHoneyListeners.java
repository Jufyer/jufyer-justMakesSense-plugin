package org.jufyer.plugin.justMakesSense.cauldron.honey;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.jufyer.plugin.justMakesSense.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CauldronHoneyListeners implements Listener {

  private final List<Location> fillingCauldrons = new ArrayList<>();
  private static final HashMap<Location, UUID> filledCauldronEntities = new HashMap<>();
  private final int checkRange = 10;

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Block block = event.getBlockPlaced();
    if (block.getType() == Material.CAULDRON) {
      checkAndStartFilling(block);
    } else if (block.getType() == Material.BEEHIVE || block.getType() == Material.BEE_NEST) {
      Block cauldron = getBlockBelow(block.getLocation(), checkRange, Material.CAULDRON);
      if (cauldron != null) checkAndStartFilling(cauldron);
    }
  }

  private void checkAndStartFilling(Block cauldron) {
    Block hive = getBlockAbove(cauldron.getLocation(), checkRange, Material.BEE_NEST, Material.BEEHIVE);
    if (hive != null && getHoney(hive) >= 5) {
      if (!filledCauldronEntities.containsKey(cauldron.getLocation().toBlockLocation()) &&
        !fillingCauldrons.contains(cauldron.getLocation().toBlockLocation())) {
        fillingCauldrons.add(cauldron.getLocation().toBlockLocation());
        createRunner(cauldron);
      }
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Location loc = event.getBlock().getLocation().toBlockLocation();

    // If the cauldron is broken
    if (event.getBlock().getType() == Material.CAULDRON) {
      removeHoneyDisplay(loc);
      fillingCauldrons.remove(loc);
    }

    // If a hive above is broken, stop the filling process if needed
    if (event.getBlock().getType() == Material.BEE_NEST || event.getBlock().getType() == Material.BEEHIVE) {
      Block cauldron = getBlockBelow(loc, checkRange, Material.CAULDRON);
      if (cauldron != null) fillingCauldrons.remove(cauldron.getLocation().toBlockLocation());
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getClickedBlock() == null || !event.getAction().isRightClick()) return;
    Location loc = event.getClickedBlock().getLocation().toBlockLocation();

    if (filledCauldronEntities.containsKey(loc)) {
      event.setCancelled(true); // Prevent normal bucket filling if any
      event.getPlayer().getInventory().addItem(new ItemStack(Material.HONEY_BLOCK));
      removeHoneyDisplay(loc);
      checkAndStartFilling(event.getClickedBlock());
    }
  }

  @EventHandler
  public void onHopperInventorySearch(HopperInventorySearchEvent event) {
    Location cauldronLoc = event.getBlock().getLocation().add(0, 1, 0).toBlockLocation();

    if (filledCauldronEntities.containsKey(cauldronLoc)) {
      if (event.getBlock().getState() instanceof Hopper hopper) {
        Inventory inv = hopper.getInventory();
        if (inv.addItem(new ItemStack(Material.HONEY_BLOCK)).isEmpty()) {
          removeHoneyDisplay(cauldronLoc);
          checkAndStartFilling(cauldronLoc.getBlock());
        }
      }
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

  private void removeHoneyDisplay(Location loc) {
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
          createHoneyCauldron(cauldron);
        }
      }
    }.runTaskLater(Main.getInstance(), (new Random().nextInt(5) + 1) * 20L * 20L);
  }

  private void createHoneyCauldron(Block cauldron) {
    Location spawnLoc = cauldron.getLocation().add(0.12, 0.2, 0.12);
    BlockDisplay display = (BlockDisplay) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.BLOCK_DISPLAY);
    display.setBlock(Bukkit.createBlockData(Material.HONEY_BLOCK));

    display.setTransformation(new Transformation(
      new Vector3f(0, 0, 0),
      new AxisAngle4f(0, 0, 0, 0),
      new Vector3f(0.75f, 0.8f, 0.75f),
      new AxisAngle4f(0, 0, 0, 0)
    ));

    filledCauldronEntities.put(cauldron.getLocation().toBlockLocation(), display.getUniqueId());
  }

  // --- Utility Methods ---

  private Block getBlockAbove(Location loc, int range, Material... types) {
    List<Material> typeList = Arrays.asList(types);
    for (int i = 1; i <= range; i++) {
      Block b = loc.clone().add(0, i, 0).getBlock();
      if (typeList.contains(b.getType())) return b;
      if (b.getType() != Material.AIR) break;
    }
    return null;
  }

  private Block getBlockBelow(Location loc, int range, Material type) {
    for (int i = 1; i <= range; i++) {
      Block b = loc.clone().subtract(0, i, 0).getBlock();
      if (b.getType() == type) return b;
      if (b.getType() != Material.AIR) break;
    }
    return null;
  }

  private int getHoney(Block block) {
    return (block.getBlockData() instanceof Beehive hive) ? hive.getHoneyLevel() : 0;
  }

  // --- Save and Load ---

  public static void saveFilledHoneyCauldrons() {
    File file = new File(Main.getInstance().getDataFolder(), "filledHoneyCauldrons.yml");
    YamlConfiguration config = new YamlConfiguration();
    int i = 0;
    for (Map.Entry<Location, UUID> entry : filledCauldronEntities.entrySet()) {
      config.set("cauldrons." + i + ".location", entry.getKey());
      config.set("cauldrons." + i + ".uuid", entry.getValue().toString());
      i++;
    }
    try { config.save(file); } catch (IOException e) { e.printStackTrace(); }
  }

  public static void loadFilledHoneyCauldrons() {
    File file = new File(Main.getInstance().getDataFolder(), "filledHoneyCauldrons.yml");
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
