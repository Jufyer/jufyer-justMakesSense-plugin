package org.jufyer.plugin.justMakesSense.features.mobs.zombie;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ZombieDeathListener implements Listener {
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
      event.deathMessage(null);
      NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getEntity());

      event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH));
      for (ItemStack item : event.getEntity().getInventory().getContents()) {
        if (item == null) continue;
        event.getDrops().add(item);
      }

      CitizensAPI.getNPCRegistry().deregister(npc);
    }
  }
}
