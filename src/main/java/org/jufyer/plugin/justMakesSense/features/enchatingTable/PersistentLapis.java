package org.jufyer.plugin.justMakesSense.features.enchatingTable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.justMakesSense.Main;

import java.util.HashMap;
import java.util.Map;

public class PersistentLapis implements Listener {

  private static final NamespacedKey LAPIS_KEY = new NamespacedKey(Main.getInstance(), "LapisCount");

  private final Map<Block, Integer> lapisCache = new HashMap<>();

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    if (event.getInventory().getType() != InventoryType.ENCHANTING) return;

    Block tableBlock = null;
    if (event.getInventory().getLocation() != null) {
      tableBlock = event.getInventory().getLocation().getBlock();
    }

    int lapisCount = 0;

    if (tableBlock != null && lapisCache.containsKey(tableBlock)) {
      lapisCount = lapisCache.get(tableBlock);
    }

    if (lapisCount == 0 && tableBlock != null) {
      BlockState state = tableBlock.getState();
      if (state instanceof TileState tileState) {
        Integer stored = tileState.getPersistentDataContainer().get(LAPIS_KEY, PersistentDataType.INTEGER);
        if (stored != null) lapisCount = stored;
      }
    }

    if (lapisCount > 0) {
      event.getInventory().setItem(1, new ItemStack(Material.LAPIS_LAZULI, lapisCount));
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    if (event.getInventory().getType() != InventoryType.ENCHANTING) return;

    ItemStack lapisStack = event.getInventory().getItem(1);
    int lapisCount = (lapisStack != null) ? lapisStack.getAmount() : 0;

    Block tableBlock = null;
    if (event.getInventory().getLocation() != null) {
      tableBlock = event.getInventory().getLocation().getBlock();
    }

    if (tableBlock != null) {
      lapisCache.put(tableBlock, lapisCount);

      BlockState state = tableBlock.getState();
      if (state instanceof TileState tileState) {
        tileState.getPersistentDataContainer().set(LAPIS_KEY, PersistentDataType.INTEGER, lapisCount);
        tileState.update();
      }
    }

    if (lapisStack != null) {
      ItemStack clone = lapisStack.clone();
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
        event.getPlayer().getInventory().removeItem(clone);
      }, 1L);
    }
  }
}
