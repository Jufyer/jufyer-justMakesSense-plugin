package org.jufyer.plugin.justMakesSense.features.mobs.zombie.snow;

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
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.SkinData;

import java.util.ArrayList;
import java.util.List;

public class SnowZombie implements Listener {
  public static void spawn(Location loc) {
    NPCRegistry registry = CitizensAPI.getNPCRegistry();

    NPC npc = registry.createNPC(EntityType.PLAYER, "Snow Zombie");
    npc.addTrait(SnowZombieAITrait.class);
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
      "snow_zombie",
      SkinData.SNOW_ZOMBIE_SIGNATURE,
      SkinData.SNOW_ZOMBIE_VALUE
    );

    npc.data().setPersistent(NPC.Metadata.REMOVE_FROM_TABLIST, true);
    npc.data().setPersistent(NPC.Metadata.REMOVE_FROM_PLAYERLIST, true);
  }

  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {
    Biome[] biomesWherePossible = {
      Biome.FROZEN_PEAKS,
      Biome.JAGGED_PEAKS,
      Biome.SNOWY_TAIGA,
      Biome.SNOWY_SLOPES,
      Biome.GROVE,
      Biome.FROZEN_RIVER,
      Biome.FROZEN_OCEAN,
      Biome.SNOWY_PLAINS,
      Biome.ICE_SPIKES,
      Biome.SNOWY_BEACH,
      Biome.WINDSWEPT_GRAVELLY_HILLS,
      Biome.WINDSWEPT_FOREST,
      Biome.WINDSWEPT_HILLS,
      Biome.STONY_SHORE,
      Biome.OLD_GROWTH_SPRUCE_TAIGA,
      Biome.TAIGA,
      Biome.OLD_GROWTH_PINE_TAIGA,
      Biome.LUSH_CAVES,
      Biome.THE_VOID,
      Biome.RIVER,
      Biome.WARM_OCEAN,
      Biome.LUKEWARM_OCEAN,
      Biome.DEEP_LUKEWARM_OCEAN,
      Biome.OCEAN,
      Biome.DEEP_OCEAN,
      Biome.CHERRY_GROVE,
      Biome.MEADOW
    };
    List<Biome> biomesWhereSnowZombieSpawningIsPossible = new ArrayList<>(List.of(biomesWherePossible));


    if (event.getEntity().getType().equals(EntityType.ZOMBIE)) {
      if (biomesWhereSnowZombieSpawningIsPossible.contains(event.getLocation().getBlock().getBiome())) {
        spawn(event.getLocation());
        event.setCancelled(true);
      }
    }
  }

  private boolean isFreezingPossible(Biome biome, double height) {
    Biome[] biomesWherePossible = {
      Biome.FROZEN_PEAKS,
      Biome.JAGGED_PEAKS,
      Biome.SNOWY_TAIGA,
      Biome.SNOWY_SLOPES,
      Biome.GROVE,
      Biome.FROZEN_RIVER,
      Biome.FROZEN_OCEAN,
      Biome.SNOWY_PLAINS,
      Biome.ICE_SPIKES,
      Biome.SNOWY_BEACH,
      Biome.WINDSWEPT_GRAVELLY_HILLS,
      Biome.WINDSWEPT_FOREST,
      Biome.WINDSWEPT_HILLS,
      Biome.STONY_SHORE,
      Biome.OLD_GROWTH_SPRUCE_TAIGA,
      Biome.TAIGA,
      Biome.OLD_GROWTH_PINE_TAIGA,
      Biome.LUSH_CAVES,
      Biome.THE_VOID,
      Biome.RIVER,
      Biome.WARM_OCEAN,
      Biome.LUKEWARM_OCEAN,
      Biome.DEEP_LUKEWARM_OCEAN,
      Biome.OCEAN,
      Biome.DEEP_OCEAN,
      Biome.CHERRY_GROVE,
      Biome.MEADOW
    };

    Biome[] biomes112_128 = {
      Biome.WINDSWEPT_GRAVELLY_HILLS,
      Biome.WINDSWEPT_HILLS,
      Biome.WINDSWEPT_FOREST,
      Biome.STONY_SHORE
    };
    Biome[] biomes154_168 = {
      Biome.TAIGA,
      Biome.OLD_GROWTH_SPRUCE_TAIGA
    };
    Biome[] biomes192_208 = {
      Biome.OLD_GROWTH_PINE_TAIGA
    };


    for (Biome b : biomesWherePossible) {
      if (b.equals(biome)) {
        for (Biome biome112_128 : biomes112_128) {
          if (biome112_128.equals(biome)) {
            if (112 <= height && height <= 128) {
              return true;
            }else return false;
          }
        }

        for (Biome biome154_168 : biomes154_168) {
          if (biome154_168.equals(biome)) {
            if (154 <= height && height <= 168) {
              return true;
            }else return false;
          }
        }

        for (Biome biome192_208 : biomes192_208) {
          if (biome192_208.equals(biome)) {
            if (192 <= height && height <= 208) {
              return true;
            }else return false;
          }
        }

        return true;
      }
    }
    return false;
  }
}
