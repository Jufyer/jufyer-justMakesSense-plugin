package org.jufyer.plugin.justMakesSense.banner.bannerOnBoats;

import net.kyori.adventure.resource.ResourcePackCallback;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jufyer.plugin.justMakesSense.Main;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BannerOnBoatsListeners implements Listener {
  public static final NamespacedKey BANNER_ARMORSTAND_KEY = new NamespacedKey(Main.getInstance(), "BANNER_ARMORSTAND");
  public static final NamespacedKey TEMPORARY_BANNER_ARMORSTAND_KEY = new NamespacedKey(Main.getInstance(), "TEMPORARY_BANNER_ARMORSTAND");

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();

    if (entity instanceof Boat) {
      if (player.isSneaking()) {
        if (player.getItemInHand().getType().name().endsWith("_BANNER")) {
          if (entity.getPassengers().toArray().length < 2) {
            ItemStack banner = player.getItemInHand();

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
              if (player.getGameMode() != GameMode.CREATIVE){
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() -1);
              }
            }, 1);

            if (banner.getType() == Material.WHITE_BANNER) {
              ItemMeta bannerMeta = banner.getItemMeta();
              bannerMeta.setCustomModelData(Main.CMDWhiteBoatBanner);
              banner.setItemMeta(bannerMeta);
            }

            ArmorStand as = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
            as.setCanMove(false);
            as.setVisible(false);
            as.setGravity(false);
            as.setBasePlate(false);
            as.setArms(false);
            as.setPersistent(true);

            as.setHelmet(banner);

            ItemStack helmet = as.getHelmet();
            helmet.setAmount(1);
            as.setHelmet(helmet);

            as.getPersistentDataContainer().set(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

            ArmorStand tempAs = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
            tempAs.setCanMove(false);
            tempAs.setVisible(false);
            tempAs.setGravity(false);
            tempAs.setBasePlate(false);
            tempAs.setArms(false);
            tempAs.setPersistent(true);

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
  }

  @EventHandler
  public void onPlayerExitVehicle(VehicleExitEvent event) {
    if (event.getExited() instanceof Player) {
      if (event.getVehicle() instanceof Boat boat) {
        for (Entity passenger : boat.getPassengers()) {
          if (passenger instanceof ArmorStand armorStand &&
            armorStand.getPersistentDataContainer().has(BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE)) {
            ArmorStand tempAs = (ArmorStand) boat.getWorld().spawnEntity(boat.getLocation(), EntityType.ARMOR_STAND);
            tempAs.setCanMove(false);
            tempAs.setVisible(false);
            tempAs.setGravity(false);
            tempAs.setBasePlate(false);
            tempAs.setArms(false);
            tempAs.setPersistent(true);

            tempAs.getPersistentDataContainer().set(TEMPORARY_BANNER_ARMORSTAND_KEY, PersistentDataType.BYTE, (byte) 1);

            ArmorStand bannerArmorStand = (CraftArmorStand) boat.getPassengers().getLast();
            ItemStack banner = bannerArmorStand.getHelmet();

            if (banner.getType() == Material.WHITE_BANNER) {
              ItemMeta bannerMeta = banner.getItemMeta();
              bannerMeta.setCustomModelData(Main.CMDWhiteBoatBanner);
              banner.setItemMeta(bannerMeta);
            }

            ArmorStand as = (ArmorStand) boat.getWorld().spawnEntity(boat.getLocation(), EntityType.ARMOR_STAND);
            as.setCanMove(false);
            as.setVisible(false);
            as.setGravity(false);
            as.setBasePlate(false);
            as.setArms(false);
            as.setPersistent(true);
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
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      if (player.isOnline()) {
        sendModernResourcePack(player);
      }
    }, 10L);
  }

  public void sendModernResourcePack(Player player) {
    //TODO: Rmove Comments but keep code...
//    ResourcePackInfo packInfo = ResourcePackInfo.resourcePackInfo()
//      .id(UUID.randomUUID())
//      .uri(URI.create("https://download.mc-packs.net/pack/1a58c4e4d70af41c60b44dc9a2f298f894865ce8.zip"))
//      .hash("1a58c4e4d70af41c60b44dc9a2f298f894865ce8")
//      .build();
//
//    ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
//      .packs(packInfo)
//      .required(true)
//      .prompt(Component.text("This server requires a custom resource pack to play!", NamedTextColor.GREEN))
//      .build();
//
//    player.sendResourcePacks(request);
  }
}
