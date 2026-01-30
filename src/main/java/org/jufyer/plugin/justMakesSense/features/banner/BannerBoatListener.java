package org.jufyer.plugin.justMakesSense.features.banner;

import org.bukkit.*;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.justMakesSense.Main;

public class BannerBoatListener implements Listener {
  public static final NamespacedKey BANNER_ARMORSTAND_KEY = new NamespacedKey(Main.getInstance(), "BANNER_ARMORSTAND");
  public static final NamespacedKey TEMPORARY_BANNER_ARMORSTAND_KEY = new NamespacedKey(Main.getInstance(), "TEMPORARY_BANNER_ARMORSTAND");


  /**
   * @param location Location where the Armor Stand is summoned
   * @return Armor Stand with no visibility, no gravity, no movement, no arms. Persistent
   */
  private ArmorStand spawnDummyArmorStand(Location location) {
    ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    as.setCanMove(false);
    as.setVisible(false);
    as.setGravity(false);
    as.setBasePlate(false);
    as.setArms(false);
    as.setPersistent(true);

    return as;
  }

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();

    if (!(entity instanceof Boat)) return;
    if (!(player.isSneaking())) return;

    if (player.getItemInHand().getType().name().endsWith("_BANNER")) {
      if (entity.getPassengers().toArray().length < 2) {
        ItemStack banner = player.getItemInHand();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
          if (player.getGameMode() != GameMode.CREATIVE) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
          }
        }, 1);

        if (banner.getType() == Material.WHITE_BANNER) {
          ItemMeta bannerMeta = banner.getItemMeta();
          bannerMeta.setCustomModelData(Main.CMDWhiteBoatBanner);
          banner.setItemMeta(bannerMeta);
        }

        ArmorStand as = spawnDummyArmorStand(entity.getLocation());
        as.setHelmet(banner);
        ItemStack helmet = as.getHelmet();
        helmet.setAmount(1);
        as.setHelmet(helmet);
        as.getPersistentDataContainer().set(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

        ArmorStand tempAs = spawnDummyArmorStand(entity.getLocation());
        tempAs.getPersistentDataContainer().set(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

        entity.addPassenger(tempAs);
        entity.addPassenger(as);

        tempAs.setRotation(entity.getYaw(), entity.getPitch());
        as.setRotation(entity.getYaw(), entity.getPitch());
      }
    } else if (!entity.getPassengers().isEmpty()) {
      if (entity.getPassengers().get(0) instanceof ArmorStand) {
        Entity passenger = entity.getPassengers().get(0);
        if (passenger.getPersistentDataContainer().has(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE)) {
          passenger.remove();
          ArmorStand as = (ArmorStand) entity.getPassengers().getLast();
          ItemStack item = as.getHelmet();
          as.setHelmet(new ItemStack(Material.AIR));
          Location loc = as.getLocation();

          entity.getPassengers().getLast().remove();
          loc.getWorld().dropItemNaturally(loc, item);
        }
      }
    } else if (!entity.getPassengers().isEmpty()) {
      if (entity.getPassengers().get(0) instanceof ArmorStand) {
        Entity passenger = entity.getPassengers().get(0);
        if (passenger.getPersistentDataContainer().has(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE)) {
          passenger.remove();
          entity.addPassenger(player);
        }
      }
    }
  }

  @EventHandler
  public void onPlayerExitVehicle(VehicleExitEvent event) {
    if (!(event.getExited() instanceof Player)) return;
    if (!(event.getVehicle() instanceof Boat boat)) return;

    for (Entity passenger : boat.getPassengers()) {
      if (passenger instanceof ArmorStand armorStand &&
        armorStand.getPersistentDataContainer().has(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE)) {

        ArmorStand tempAs = spawnDummyArmorStand(boat.getLocation());
        tempAs.getPersistentDataContainer().set(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

        ArmorStand bannerArmorStand = (CraftArmorStand) boat.getPassengers().getLast();
        ItemStack banner = bannerArmorStand.getHelmet();

        if (banner.getType() == Material.WHITE_BANNER) {
          ItemMeta bannerMeta = banner.getItemMeta();
          bannerMeta.setCustomModelData(Main.CMDWhiteBoatBanner);
          banner.setItemMeta(bannerMeta);
        }

        ArmorStand as = spawnDummyArmorStand(boat.getLocation());
        as.setHelmet(banner);
        as.getPersistentDataContainer().set(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

        boat.getPassengers().getLast().remove();

        boat.addPassenger(tempAs);
        boat.addPassenger(as);

        tempAs.setRotation(boat.getYaw(), boat.getPitch());
        as.setRotation(boat.getYaw(), boat.getPitch());
        break;
      }
    }
  }
}
