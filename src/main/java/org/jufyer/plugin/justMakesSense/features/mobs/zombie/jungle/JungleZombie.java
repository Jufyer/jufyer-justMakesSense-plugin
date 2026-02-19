package org.jufyer.plugin.justMakesSense.features.mobs.zombie.jungle;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.SkinData;

public class JungleZombie implements Listener {
  public static void spawn(Location loc) {
    if (!Main.getInstance().isFeatureEnabledInWorld("jungle-zombie", loc.getWorld())) {
      return;
    }

    NPCRegistry registry = CitizensAPI.getNPCRegistry();

    NPC npc = registry.createNPC(EntityType.PLAYER, "Jungle Zombie");
    npc.addTrait(JungleZombieAITrait.class);
    npc.setProtected(false);

    setSkin(npc);
    npc.spawn(loc);
    npc.data().set(NPC.Metadata.NAMEPLATE_VISIBLE, false);

    Player zombiePlayer = (Player) npc.getEntity();
    zombiePlayer.setHealth(10);
  }

  private static void setSkin(NPC npc) {
    SkinTrait skin = npc.getOrAddTrait(SkinTrait.class);

    skin.setSkinPersistent(
      "jungle_zombie",
      SkinData.JUNGLE_ZOMBIE_SIGNATURE,
      SkinData.JUNGLE_ZOMBIE_VALUE
    );

    npc.data().setPersistent(NPC.Metadata.REMOVE_FROM_TABLIST, true);
    npc.data().setPersistent(NPC.Metadata.REMOVE_FROM_PLAYERLIST, true);
  }

  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {
    if (!Main.getInstance().isFeatureEnabledInWorld("jungle-zombie", event.getEntity().getWorld())) {
      return;
    }

    if (event.getEntity().getType().equals(EntityType.ZOMBIE)) {
      if (event.getLocation().getBlock().getBiome().equals(Biome.JUNGLE)
        || event.getLocation().getBlock().getBiome().equals(Biome.BAMBOO_JUNGLE)
        || event.getLocation().getBlock().getBiome().equals(Biome.SPARSE_JUNGLE)) {

        spawn(event.getLocation());
        event.setCancelled(true);
      }
    }
  }
}
