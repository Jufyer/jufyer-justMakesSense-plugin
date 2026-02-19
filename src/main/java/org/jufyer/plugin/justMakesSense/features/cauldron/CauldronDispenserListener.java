package org.jufyer.plugin.justMakesSense.features.cauldron;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.justMakesSense.Main;

import static org.jufyer.plugin.justMakesSense.features.cauldron.CauldronHoneyListener.filledHoneyCauldronEntities;
import static org.jufyer.plugin.justMakesSense.features.cauldron.CauldronIceListener.filledIceCauldronEntities;

public class CauldronDispenserListener implements Listener {

  private final Main plugin;

  public CauldronDispenserListener(Main plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockDispense(BlockDispenseEvent event) {
    if (!plugin.isFeatureEnabledInWorld("cauldron-dispenser-support", event.getBlock().getWorld())) {
      return;
    }

    if (event.getBlock().getType() != Material.DISPENSER) {
      return;
    }

    BlockData blockData = event.getBlock().getBlockData();
    if (!(blockData instanceof Directional)) {
      return;
    }

    Directional directional = (Directional) blockData;


    Block targetBlock = event.getBlock().getRelative(directional.getFacing());

    // Check if it's a custom honey or ice cauldron
    if (filledHoneyCauldronEntities.containsKey(targetBlock.getLocation()) ||
      filledIceCauldronEntities.containsKey(targetBlock.getLocation())) {
      return;
    }

    // Check if target is a cauldron
    if (!Tag.CAULDRONS.isTagged(targetBlock.getType())) {
      return;
    }

    Material itemType = event.getItem().getType();
    Material newCauldronType = null;
    Material replacementItem = null;
    Sound sound = null;

    // Handle bucket -> cauldron filling
    switch (itemType) {
      case WATER_BUCKET:
        newCauldronType = Material.WATER_CAULDRON;
        replacementItem = Material.BUCKET;
        sound = Sound.ITEM_BUCKET_EMPTY;
        break;

      case LAVA_BUCKET:
        newCauldronType = Material.LAVA_CAULDRON;
        replacementItem = Material.BUCKET;
        sound = Sound.ITEM_BUCKET_EMPTY_LAVA;
        break;

      case POWDER_SNOW_BUCKET:
        newCauldronType = Material.POWDER_SNOW_CAULDRON;
        replacementItem = Material.BUCKET;
        sound = Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW;
        break;

      case BUCKET:
        // Handle cauldron -> bucket emptying
        if (targetBlock.getType() == Material.CAULDRON) return;

        newCauldronType = Material.CAULDRON;

        switch (targetBlock.getType()) {
          case WATER_CAULDRON:
            replacementItem = Material.WATER_BUCKET;
            sound = Sound.ITEM_BUCKET_FILL;
            break;

          case LAVA_CAULDRON:
            replacementItem = Material.LAVA_BUCKET;
            sound = Sound.ITEM_BUCKET_FILL_LAVA;
            break;

          case POWDER_SNOW_CAULDRON:
            replacementItem = Material.POWDER_SNOW_BUCKET;
            sound = Sound.ITEM_BUCKET_FILL_POWDER_SNOW;
            break;

          default:
            return;
        }
        break;

      default:
        return;
    }

    // Cancel the default dispense behavior
    event.setCancelled(true);

    // Apply the cauldron change and item replacement
    final Material finalCauldronType = newCauldronType;
    final Material finalReplacementItem = replacementItem;
    final Sound finalSound = sound;

    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
      BlockState state = event.getBlock().getState();
      if (state instanceof Dispenser) {
        Dispenser dispenser = (Dispenser) state;

        // Play sound
        dispenser.getWorld().playSound(
          dispenser.getLocation(),
          finalSound,
          SoundCategory.BLOCKS,
          1.0F,
          1.0F
        );

        // Find and replace the item in the dispenser
        for (int i = 0; i < dispenser.getInventory().getSize(); i++) {
          ItemStack item = dispenser.getInventory().getItem(i);
          if (item != null && item.getType() == itemType) {
            dispenser.getInventory().setItem(i, new ItemStack(finalReplacementItem));
            break;
          }
        }

        // Change the cauldron
        targetBlock.setType(finalCauldronType);

        // Set fill level if applicable
        BlockData cauldronData = targetBlock.getBlockData();
        if (cauldronData instanceof Levelled) {
          Levelled levelled = (Levelled) cauldronData;
          levelled.setLevel(levelled.getMaximumLevel());
          targetBlock.setBlockData(levelled);
        }
      }
    }, 1L);
  }
}
