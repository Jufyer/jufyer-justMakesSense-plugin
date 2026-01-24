package org.jufyer.plugin.justMakesSense;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResourcePackListeners implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (!player.isOnline()) return;

      List<ResourcePackInfo> packs = new ArrayList<>();

      if (Main.getInstance().getCustomConfig().getBoolean("banner-on-boats")) {
        packs.add(createPack(
          "https://download.mc-packs.net/pack/71d29e4e502078d9fc7fc8846b55be9c8fb13471.zip",
          "71d29e4e502078d9fc7fc8846b55be9c8fb13471"
        ));
      }

      if (Main.getInstance().getCustomConfig().getBoolean("copper-hopper")) {
        packs.add(createPack(
          "https://download.mc-packs.net/pack/984833b2b8ad4fdfd421f8f9a056fbf48648a2d2.zip",
          "984833b2b8ad4fdfd421f8f9a056fbf48648a2d2"
        ));
      }

      if (Main.getInstance().getCustomConfig().getBoolean("dyed-torches")) {
        packs.add(createPack(
          "https://download.mc-packs.net/pack/52bb3578b13879a17b3f13ee06333197d2d231a2.zip",
          "52bb3578b13879a17b3f13ee06333197d2d231a2"
        ));
      }

      if (!packs.isEmpty()) {
        ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
          .packs(packs)
          .required(true)
          .prompt(Component.text("This server requires a custom resource pack to play!", NamedTextColor.GREEN))
          .build();

        player.sendResourcePacks(request);
      }

    }, 10L);
  }

  private ResourcePackInfo createPack(String url, String hash) {
    return ResourcePackInfo.resourcePackInfo()
      .id(UUID.randomUUID())
      .uri(URI.create(url))
      .hash(hash)
      .build();
  }
}
