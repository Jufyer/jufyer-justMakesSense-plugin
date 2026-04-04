package org.jufyer.plugin.justMakesSense.features.horse;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class StatisticsPreview implements Listener {
  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    if (event.getPlayer().isSneaking()) {
      if (event.getRightClicked().getType() == EntityType.HORSE) {
        Horse horse = (Horse) event.getRightClicked();
        Player player = event.getPlayer();

        player.sendMessage("§6🐴 Horse Statistics §r");
        player.sendMessage("§c♥ Health: §f" + String.format("%.2f", horse.getAttribute(Attribute.MAX_HEALTH).getValue()));
        player.sendMessage("§b💨 Speed: §f" + String.format("%.2f", horse.getAttribute(Attribute.MOVEMENT_SPEED).getValue()));
        player.sendMessage("§a⬆ Jump Strength: §f" + String.format("%.2f", horse.getAttribute(Attribute.JUMP_STRENGTH).getValue()));
      }
    }
  }
}
