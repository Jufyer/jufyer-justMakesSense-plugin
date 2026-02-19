package org.jufyer.plugin.justMakesSense.features.sponge;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jufyer.plugin.justMakesSense.Main;

import javax.annotation.Nonnull;

public class SpongeIgniteListener implements Listener {
  @EventHandler
  public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
    Player player = event.getPlayer();

    if (event.getClickedBlock() == null) return;
    if (!Main.getInstance().isFeatureEnabledInWorld("sponge-ignite", event.getClickedBlock().getWorld())) {
      return;
    }
    if (event.getItem() == null) return;
    if (event.getClickedBlock().getType() != Material.WET_SPONGE) return;
    if (!event.getAction().isRightClick()) return;

    if (!event.getItem().getType().equals(Material.FLINT_AND_STEEL) && !event.getItem().getType().equals(Material.FIRE_CHARGE)) {
      return;
    }

    event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
    event.getClickedBlock().setType(Material.SPONGE);
    event.getClickedBlock().getWorld().spawnParticle(Particle.LARGE_SMOKE, event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.01);
    event.setCancelled(true);

    switch (event.getItem().getType()) {
      case Material.FIRE_CHARGE -> event.getItem().setAmount(event.getItem().getAmount() - 1);
    }
  }
}
