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

public class CauldronDirtListener implements Listener {
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    Item dirt = event.getItemDrop();
    if (dirt.getItemStack().getType() != Material.DIRT) {
      return;
    }

    Bukkit.getScheduler().runTaskTimer(Main.getInstance(), (task) -> {
      if (dirt.isDead()) {
        task.cancel();
        return;
      }

      Location location = dirt.getLocation();
      Block block = location.getBlock();
      if (block.getType() != Material.WATER_CAULDRON || !(block.getBlockData() instanceof Levelled)) {
        return;
      }

      task.cancel();

      dirt.getWorld().playSound(dirt, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, 1, 1);
      dirt.getWorld().dropItem(location, new ItemStack(Material.MUD, dirt.getItemStack().getAmount()));
      dirt.remove();


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
