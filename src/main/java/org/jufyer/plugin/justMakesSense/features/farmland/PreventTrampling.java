package org.jufyer.plugin.justMakesSense.features.farmland;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PreventTrampling implements Listener {
  @EventHandler
  public void onEntityInteract(EntityInteractEvent event) {
    if (event.getBlock().getType() == Material.FARMLAND) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() == Action.PHYSICAL) {
      if (event.getClickedBlock() == null) return;
      if (event.getClickedBlock().getType() == Material.FARMLAND) {
        event.setCancelled(true);
      }
    }
  }
}
