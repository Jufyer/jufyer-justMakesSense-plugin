package org.jufyer.plugin.justMakesSense.features.mobs.husk;

import org.bukkit.Material;
import org.bukkit.entity.Husk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class HuskDropListener implements Listener {
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
   if (event.getEntity() instanceof Husk) {
     event.getDrops().add(new ItemStack(Material.SAND));
   }
  }
}
