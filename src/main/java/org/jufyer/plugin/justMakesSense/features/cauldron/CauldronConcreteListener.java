package org.jufyer.plugin.justMakesSense.features.cauldron;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jufyer.plugin.justMakesSense.Main;

public class CauldronConcreteListener implements Listener {

  private final Main plugin;

  public CauldronConcreteListener(Main plugin) {
    this.plugin = plugin;
  }

  private Material[] colouredConcrete = {
    Material.LIGHT_GRAY_CONCRETE,
    Material.GRAY_CONCRETE,
    Material.BLACK_CONCRETE,
    Material.BROWN_CONCRETE,
    Material.RED_CONCRETE,
    Material.YELLOW_CONCRETE,
    Material.ORANGE_CONCRETE,
    Material.LIME_CONCRETE,
    Material.GREEN_CONCRETE,
    Material.CYAN_CONCRETE,
    Material.LIGHT_BLUE_CONCRETE,
    Material.BLUE_CONCRETE,
    Material.PURPLE_CONCRETE,
    Material.MAGENTA_CONCRETE,
    Material.PINK_CONCRETE
  };

  private boolean isDyedConcretePowder(Material itemMaterial) {
    Material[] colouredWool = {
      Material.LIGHT_GRAY_CONCRETE_POWDER,
      Material.GRAY_CONCRETE_POWDER,
      Material.BLACK_CONCRETE_POWDER,
      Material.BROWN_CONCRETE_POWDER,
      Material.RED_CONCRETE_POWDER,
      Material.YELLOW_CONCRETE_POWDER,
      Material.ORANGE_CONCRETE_POWDER,
      Material.LIME_CONCRETE_POWDER,
      Material.GREEN_CONCRETE_POWDER,
      Material.CYAN_CONCRETE_POWDER,
      Material.LIGHT_BLUE_CONCRETE_POWDER,
      Material.BLUE_CONCRETE_POWDER,
      Material.PURPLE_CONCRETE_POWDER,
      Material.MAGENTA_CONCRETE_POWDER,
      Material.PINK_CONCRETE_POWDER
    };

    for (Material mat : colouredWool) {
      if (mat == itemMaterial) {
        return true;
      }
    }

    return false;
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (!plugin.isFeatureEnabledInWorld("cauldron-concrete", event.getItemDrop().getWorld())) {
      return;
    }
    Item drop = event.getItemDrop();
    if (!isDyedConcretePowder(drop.getItemStack().getType())) {
      return;
    }

    Bukkit.getScheduler().runTaskTimer(Main.getInstance(), (task) -> {
      if (drop.isDead()) {
        task.cancel();
        return;
      }

      Location location = drop.getLocation();
      Block block = location.getBlock();
      if (block.getType() != Material.WATER_CAULDRON || !(block.getBlockData() instanceof Levelled)) {
        return;
      }

      task.cancel();

      drop.getWorld().playSound(drop, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, 1, 1);

      drop.getWorld().dropItem(location, new ItemStack(Material.getMaterial(drop.getItemStack().getType().name().replace("_POWDER", "")), drop.getItemStack().getAmount()));

      drop.remove();


      BlockData blockData = block.getBlockData();
      if (blockData instanceof Levelled) {
        Levelled levelled = (Levelled) blockData;
        if (levelled.getLevel() > 0) {
          if (levelled.getLevel() == 1) {
            block.setType(Material.CAULDRON);
          }else {
            levelled.setLevel(levelled.getLevel() - 1);
            block.setBlockData(levelled); // Update visual state
          }
        }
      }
    }, 1L, 1L);
  }
}
