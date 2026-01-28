package org.jufyer.plugin.justMakesSense.features.mobs.pets;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PetProtectListener implements Listener {
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player player)) return;
    if (!(event.getEntity() instanceof Tameable tameable)) return;
    if (!tameable.isTamed()) return;
    if (tameable.getOwner() == null) return;

    if (!player.isSneaking()) {
      if (tameable.getOwner().getUniqueId() == player.getUniqueId()) {
        event.setCancelled(true);
      }
    }
  }
}
