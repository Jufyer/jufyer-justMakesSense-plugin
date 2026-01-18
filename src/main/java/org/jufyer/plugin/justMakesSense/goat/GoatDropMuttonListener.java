package org.jufyer.plugin.justMakesSense.goat;

import org.bukkit.Material;
import org.bukkit.entity.Goat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class GoatDropMuttonListener implements Listener {
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if (event.getEntity() instanceof Goat) {
      event.getDrops().add(new ItemStack(Material.MUTTON));
    }
  }
}
