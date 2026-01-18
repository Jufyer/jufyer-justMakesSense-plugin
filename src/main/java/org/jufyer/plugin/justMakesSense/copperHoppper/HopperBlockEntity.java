package org.jufyer.plugin.justMakesSense.copperHoppper;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.copperHoppper.listener.CopperHopperBlockListener;

import java.util.*;

public class HopperBlockEntity implements Listener {
  private static HashMap<Location, Integer> cooldownHoppers = new HashMap<>();

  public static void createRunner() {
    new org.bukkit.scheduler.BukkitRunnable() {
      @Override
      public void run() {
        pushItemsTick();
      }
    }.runTaskTimer(Main.getInstance(), 20L,1L);
  }

  //Run every tick
  public static void pushItemsTick() {
    List<Location> toRemove = new ArrayList<>();

    for (Location loc : new ArrayList<>(Main.loadedCopperHoppers)) {
      Block block = loc.getBlock();
      if (!loc.isChunkLoaded() || !block.getType().equals(Material.HOPPER)) {
        toRemove.add(loc);
        continue;
      }

      Hopper hopper = (Hopper) block.getState();
      CopperHopperBlockListener.oxidationCheck(hopper);

      Location hopperLoc = hopper.getLocation().clone();

      Integer cd = cooldownHoppers.get(hopperLoc);
      if (cd != null) {
        if (cd <= 1) {
          cooldownHoppers.remove(hopperLoc);
        } else {
          cooldownHoppers.put(hopperLoc, cd - 1);
          continue;
        }
      }

      if (block.isBlockPowered()) continue;
      if (block.isBlockIndirectlyPowered()) continue;

      Inventory hopperInventory = hopper.getInventory();

      boolean operation_successful = false;

      if (!hopperInventory.isEmpty()) {
        operation_successful = ejectOneItem(hopper);
      }

      if (!operation_successful) {
        operation_successful = pullOneItem(hopper);
      }

      if (!operation_successful) {
        operation_successful = pullOneItemFromFloor(hopper);
      }

      if (operation_successful) {
        for (CopperVariant variant : CopperVariant.values()) {
          if (hopper.getPersistentDataContainer().has(variant.getBlockKey())) {
            cooldownHoppers.put(hopper.getLocation(), variant.getSpeed());
          }
        }
      }
    }

    toRemove.forEach(Main.loadedCopperHoppers::remove);

  }

  public static boolean ejectOneItem(Hopper hopper) {
    if (!(hopper.getBlockData() instanceof Directional directional)) {
      return false;
    }
    BlockFace facing = directional.getFacing();

    Location targetLoc = hopper.getLocation().clone().add(
      facing.getModX(),
      facing.getModY(),
      facing.getModZ()
    );
    Block targetBlock = targetLoc.getBlock();


    if (!(targetBlock.getState() instanceof InventoryHolder)) return false;

    Inventory hopperInventory = hopper.getInventory();
    for (int slot = 0; slot < hopperInventory.getSize(); slot++) {
      ItemStack item = hopperInventory.getItem(slot);
      if (item == null || item.getType().equals(Material.AIR)) continue;

      if (item == null) continue;

      InventoryHolder inventoryHolder = (InventoryHolder) targetBlock.getState();
      Inventory targetInventory = inventoryHolder.getInventory();

      ItemStack itemClone = item.clone();
      itemClone.setAmount(1);
      HashMap<Integer, ItemStack> remaining = targetInventory.addItem(itemClone);

      if (remaining.isEmpty()) {
        item.setAmount(item.getAmount() - 1);
        Main.copperHopperItemCount.put(hopper, Main.copperHopperItemCount.getOrDefault(hopper, 0) +1);
        if (Main.getInstance().getCustomConfig().getInt("copper-hopper-item-count") > 0) {
          if (Main.copperHopperItemCount.get(hopper) >= Main.getInstance().getCustomConfig().getInt("copper-hopper-item-count")) {
            for (CopperVariant variant : CopperVariant.values()) {
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
            }
          }
        }
        // hopper.update();
        return true;
      }else {
        continue;
      }
    }

    return false;
  }

  public static boolean pullOneItem(Hopper hopper) {
    Location sourceLocation = hopper.getLocation().add(0, 1, 0);
    Block sourceBlock = sourceLocation.getBlock();

    if (sourceBlock.getState() instanceof InventoryHolder) {
      Inventory sourceInventory = ((InventoryHolder) sourceBlock.getState()).getInventory();

      for (int slot = 0; slot < sourceInventory.getSize(); slot++) {
        ItemStack itemInSource = sourceInventory.getItem(slot);
        if (itemInSource == null) continue;

        ItemStack itemClone = itemInSource.clone();
        itemClone.setAmount(1);
        HashMap<Integer, ItemStack> remaining = hopper.getInventory().addItem(itemClone);

        if (remaining.isEmpty()) {
          itemInSource.setAmount(itemInSource.getAmount() - 1);
          // hopper.update();
          return true;
        }else {
          continue;
        }
      }
    }

    return false;
  }

  public static boolean pullOneItemFromFloor(Hopper hopper) {
    Collection<Entity> entities = hopper.getLocation().getWorld().getNearbyEntities(hopper.getLocation().add(0,1,0), 0.5, 1.0, 0.5);

    for (Entity entity : entities) {
      if (entity instanceof Item) {
        ItemStack itemStack = ((Item) entity).getItemStack();

        ItemStack itemClone = itemStack.clone();
        itemClone.setAmount(1);
        HashMap<Integer, ItemStack> remaining = hopper.getInventory().addItem(itemClone);

        if (remaining.isEmpty()) {
          if (itemStack.getAmount() <= 1) {
            entity.remove();
            // hopper.update();
            return true;
          }
          itemStack.setAmount(itemStack.getAmount() - 1);
          // hopper.update();
          return true;
        }else {
          continue;
        }
      }
    }

    return false;
  }

  // canacel noraml hopper
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInventoryMoveItem(InventoryMoveItemEvent event) {
    InventoryHolder srcHolder = event.getSource().getHolder();
    InventoryHolder dstHolder = event.getDestination().getHolder();

    Hopper sourceHopper = null;
    Hopper destinationHopper = null;

    if (srcHolder instanceof Hopper h) {
      sourceHopper = h;
    }

    if (dstHolder instanceof Hopper h) {
      destinationHopper = h;
    }

    // Wenn kein Hopper beteiligt ist → ignorieren
    if (sourceHopper == null && destinationHopper == null) {
      return;
    }

    for (CopperVariant variant : CopperVariant.values()) {
      if ((sourceHopper != null &&
        sourceHopper.getPersistentDataContainer().has(variant.getBlockKey()))
        || (destinationHopper != null &&
        destinationHopper.getPersistentDataContainer().has(variant.getBlockKey()))) {

        event.setCancelled(true);
        return;
      }
    }
  }
}
