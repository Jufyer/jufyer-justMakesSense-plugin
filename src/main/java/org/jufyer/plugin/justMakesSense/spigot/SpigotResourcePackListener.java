package org.jufyer.plugin.justMakesSense.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jufyer.plugin.justMakesSense.Main;

public class SpigotResourcePackListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (!player.isOnline()) return;

      player.setResourcePack(
          "https://download.mc-packs.net/pack/41883790e74d8471539ae56a3adf2164e5aab395.zip",
          "41883790e74d8471539ae56a3adf2164e5aab395"
      );

    }, 10L);
  }

}
