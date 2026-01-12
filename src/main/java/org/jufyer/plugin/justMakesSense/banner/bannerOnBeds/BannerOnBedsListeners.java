package org.jufyer.plugin.justMakesSense.banner.bannerOnBeds;

import org.bukkit.Tag;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class BannerOnBedsListeners implements Listener {

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
      if (event.getClickedBlock() != null && Tag.BEDS.isTagged(event.getClickedBlock().getType())) {
        ItemStack item = event.getItem();
        if (item != null && Tag.BANNERS.isTagged(item.getType())) {
          event.setCancelled(true);

          Bed bedData = (Bed) event.getClickedBlock().getBlockData();
          if (bedData.getPart() != Bed.Part.FOOT) return;

          ItemDisplay itemDisplay = (ItemDisplay) event.getClickedBlock().getWorld().spawnEntity(
            event.getClickedBlock().getLocation().add(0.5, 0.563, 0.5),
            EntityType.ITEM_DISPLAY
          );

          itemDisplay.setItemStack(item);
          itemDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);

          float rotationAngle = 0;
          switch (bedData.getFacing()) {
            case NORTH -> rotationAngle = (float) Math.PI;
            case SOUTH -> rotationAngle = 0;
            case WEST -> rotationAngle = (float) (Math.PI / 2);
            case EAST -> rotationAngle = (float) (-Math.PI / 2);
          }

          Transformation transformation = new Transformation( //TODO: adjust
            new Vector3f(0, 0, 0.5f), // translation
            new AxisAngle4f(-rotationAngle, 0, 1, 0), //left rotation
            new Vector3f(1.01f, 1.01f, 1.01f), //scale
            new AxisAngle4f((float) (Math.PI / 2), 1, 0, 0) //right rotation
          );

          itemDisplay.setTransformation(transformation);
        }
      }
    }
  }
}
