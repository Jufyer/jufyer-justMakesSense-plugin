package org.jufyer.plugin.justMakesSense.features.cauldron;

import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.justMakesSense.Main;

import static org.jufyer.plugin.justMakesSense.features.cauldron.CauldronHoneyListener.filledHoneyCauldronEntities;
import static org.jufyer.plugin.justMakesSense.features.cauldron.CauldronIceListener.filledIceCauldronEntities;

// Assuming these maps are stored in a accessible location,
// for example in your Main class or a dedicated CauldronManager.
// Update the references below to point to wherever these are actually defined.

public class CauldronDispenserListener implements Listener {

  @EventHandler
  public void onBlockPreDispense(BlockPreDispenseEvent event) {
    BlockData var3 = event.getBlock().getBlockData();
    if (var3 instanceof Directional directional) {
      if (directional.getFacing() == BlockFace.DOWN && !Main.getInstance().getConfig().getBoolean("allow-interaction-from-below")) {
        return;
      }

      Block block = event.getBlock().getRelative(directional.getFacing());

      // --- ADDED CHECK START ---
      // If the cauldron location is tracked in the custom Honey or Ice maps, stop the dispenser
      if (filledHoneyCauldronEntities.containsKey(block.getLocation()) ||
        filledIceCauldronEntities.containsKey(block.getLocation())) {
        return;
      }
      // --- ADDED CHECK END ---

      if (Tag.CAULDRONS.isTagged(block.getType())) {
        Material material;
        Material itemReplacement;
        Sound sound;
        label50:
        switch (event.getItemStack().getType()) {
          case WATER_BUCKET:
            if (!Main.getInstance().getConfig().getBoolean("enable-water")) return;
            material = Material.WATER_CAULDRON;
            sound = Sound.ITEM_BUCKET_EMPTY;
            itemReplacement = Material.BUCKET;
            break;
          case LAVA_BUCKET:
            if (!Main.getInstance().getConfig().getBoolean("enable-lava")) return;
            material = Material.LAVA_CAULDRON;
            sound = Sound.ITEM_BUCKET_EMPTY_LAVA;
            itemReplacement = Material.BUCKET;
            break;
          case POWDER_SNOW_BUCKET:
            if (!Main.getInstance().getConfig().getBoolean("enable-powder-snow")) return;
            material = Material.POWDER_SNOW_CAULDRON;
            sound = Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW;
            itemReplacement = Material.BUCKET;
            break;
          case BUCKET:
            if (block.getType() == Material.CAULDRON) return;
            material = Material.CAULDRON;
            switch (block.getType()) {
              case WATER_CAULDRON:
                if (!Main.getInstance().getConfig().getBoolean("enable-water")) return;
                sound = Sound.ITEM_BUCKET_FILL;
                itemReplacement = Material.WATER_BUCKET;
                break label50;
              case LAVA_CAULDRON:
                if (!Main.getInstance().getConfig().getBoolean("enable-lava")) return;
                sound = Sound.ITEM_BUCKET_FILL_LAVA;
                itemReplacement = Material.LAVA_BUCKET;
                break label50;
              case POWDER_SNOW_CAULDRON:
                if (!Main.getInstance().getConfig().getBoolean("enable-powder-snow")) return;
                sound = Sound.ITEM_BUCKET_FILL_POWDER_SNOW;
                itemReplacement = Material.POWDER_SNOW_BUCKET;
                break label50;
              default:
                return;
            }
          default:
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
          BlockState state = event.getBlock().getState(false);
          if (state instanceof Dispenser dispenser) {
            dispenser.getWorld().playSound(dispenser.getLocation(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
            dispenser.getInventory().setItem(event.getSlot(), new ItemStack(itemReplacement));
            block.setType(material);
            BlockData patt4368$temp = block.getBlockData();
            if (patt4368$temp instanceof Levelled levelled) {
              levelled.setLevel(levelled.getMaximumLevel());
              block.setBlockData(levelled);
            }
          }
        });
      }
    }
  }

  @EventHandler
  public void onBlockDispense(BlockDispenseEvent event) {
    if (event.getBlock().getType() == Material.DISPENSER) {
      BlockData var3 = event.getBlock().getBlockData();
      if (var3 instanceof Directional directional) {
        if (directional.getFacing() == BlockFace.DOWN && !Main.getInstance().getConfig().getBoolean("allow-interaction-from-below")) {
          return;
        }

        Block block = event.getBlock().getRelative(directional.getFacing());

        // --- ADDED CHECK START ---
        if (filledHoneyCauldronEntities.containsKey(block.getLocation()) ||
          filledIceCauldronEntities.containsKey(block.getLocation())) {
          return;
        }
        // --- ADDED CHECK END ---

        if (Tag.CAULDRONS.isTagged(block.getType())) {
          switch (event.getItem().getType()) {
            case WATER_BUCKET:
              if (!Main.getInstance().getConfig().getBoolean("enable-water")) return;
              break;
            case LAVA_BUCKET:
              if (!Main.getInstance().getConfig().getBoolean("enable-lava")) return;
              break;
            case POWDER_SNOW_BUCKET:
              if (!Main.getInstance().getConfig().getBoolean("enable-powder-snow")) return;
              break;
            default:
              return;
          }
          event.setCancelled(true);
        }
      }
    }
  }
}
