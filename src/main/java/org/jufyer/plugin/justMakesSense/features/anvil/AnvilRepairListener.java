package org.jufyer.plugin.justMakesSense.features.anvil;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilRepairListener implements Listener {
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getClickedBlock() == null) return;
    if (event.getItem() == null) return;

    if (event.getClickedBlock().getBlockData() instanceof Directional directional) {
      if (event.getClickedBlock().getType().equals(Material.CHIPPED_ANVIL) || event.getClickedBlock().getType().equals(Material.DAMAGED_ANVIL)) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        if (itemStack.getType().equals(Material.IRON_BLOCK)) {
          Block block = event.getClickedBlock();
          Material blockType = block.getType();

          boolean success = false;
          BlockFace face = directional.getFacing();
          switch (blockType) {
            case CHIPPED_ANVIL -> {
              block.setType(Material.ANVIL);
              success = true;
            }
            case DAMAGED_ANVIL -> {
              block.setType(Material.CHIPPED_ANVIL);
              success = true;
            }
          }
          if (success) {
            itemStack.setAmount(itemStack.getAmount() - 1);

            Block newBlock = event.getClickedBlock().getLocation().getBlock();
            Directional newDirectional = (Directional) newBlock.getBlockData();
            newDirectional.setFacing(face);
            newBlock.setBlockData(newDirectional);

            event.setCancelled(true);
          }
        }
      }
    }
  }
}
