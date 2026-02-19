package org.jufyer.plugin.justMakesSense.features.waterbottle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.SplashPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jufyer.plugin.justMakesSense.Main;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WatterBottleConvertLavaListener implements Listener {
  @EventHandler
  public void onPotionSplash(PotionSplashEvent event) {
    if (!Main.getInstance().isFeatureEnabledInWorld("water-bottles-convert-lava", event.getEntity().getWorld())) {
      return;
    }
    if (event.getPotion() instanceof SplashPotion potion) {
      ItemStack item = potion.getItem();
      PotionMeta meta = (PotionMeta) item.getItemMeta();
      if (meta == null || meta.getBasePotionType() != PotionType.WATER) {
        return;
      }

      Bukkit.getScheduler().runTaskTimer(Main.getInstance(), (task) -> {
        if (!potion.isDead() && potion.getLocation().getBlock().getType() != Material.LAVA) {
          return;
        }

        task.cancel();

        Random random = new Random();
        int remaining = random.nextInt(6, 10);
        if (remaining != convertLava(potion.getLocation(), 0.50, 0, remaining)) {
          event.getPotion().getLocation().getWorld().playSound(potion, Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
        }
      }, 1L, 1L);
    }
  }

  /**
   * Attempts to convert the lava in the given radius to obsidian.
   *
   * @param location the location
   * @param radius the radius around the location to check for lava
   * @param skipChance chance to skip a lava block
   * @param remaining how much is left to convert
   * @return how much is left to convert
   */
  private int convertLava(@Nonnull Location location, double radius, int skipChance, int remaining) {
    if (remaining == 0 || skipChance >= 100) {
      return remaining;
    }

    for (Location check : around(location, radius)) {
      if (check.getBlock().getType() == Material.LAVA) {
        check.getBlock().setType(Material.OBSIDIAN);
        remaining -= 1;
      }
      if (remaining == 0) {
        return 0;
      }
    }

    return convertLava(location, radius + 0.5, skipChance + 25, remaining);
  }

  /**
   * Retrieves all locations around a location.
   *
   * @param location the location
   * @param radius the radius around the location
   * @return all blocks around the location
   */
  @Nonnull
  private static List<Location> around(@Nonnull Location location, double radius) {
    Location corner1 = location.clone().add(+radius, +radius, +radius);
    Location corner2 = location.clone().add(-radius, -radius, -radius);
    return between(corner1, corner2);
  }

  /**
   * Retrieves all locations between two locations.
   *
   * @param corner1 the first corner
   * @param corner2 the second corner
   * @return all blocks between the two locations
   */
  @Nonnull
  private static List<Location> between(@Nonnull Location corner1, @Nonnull Location corner2) {
    int x1 = corner1.getBlockX(), y1 = corner1.getBlockY(), z1 = corner1.getBlockZ();
    int x2 = corner2.getBlockX(), y2 = corner2.getBlockY(), z2 = corner2.getBlockZ();
    Location min = new Location(corner1.getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
    Location max = new Location(corner1.getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));

    List<Location> locations = new ArrayList<>();
    for (int y=max.getBlockY(); y>=min.getBlockY(); y--) {
      for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
        for (int z=min.getBlockZ(); z<=max.getBlockZ(); z++) {
          locations.add(new Location(corner1.getWorld(), x, y, z));
        }
      }
    }
    return locations;
  }
}
