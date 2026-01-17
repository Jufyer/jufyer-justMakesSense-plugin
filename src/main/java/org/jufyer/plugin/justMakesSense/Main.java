package org.jufyer.plugin.justMakesSense;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.justMakesSense.banner.bannerOnBeds.BannerOnBedsListeners;
import org.jufyer.plugin.justMakesSense.banner.bannerOnBoats.BannerOnBoatsListeners;
import org.jufyer.plugin.justMakesSense.cauldron.dispenser.CauldronDispenserListeners;
import org.jufyer.plugin.justMakesSense.cauldron.honey.CauldronHoneyListeners;
import org.jufyer.plugin.justMakesSense.cauldron.ice.CauldronIceListeners;
import org.jufyer.plugin.justMakesSense.cauldron.removeDye.CauldronRemoveDyeListeners;
import org.jufyer.plugin.justMakesSense.copperHoppper.HopperBlockEntity;
import org.jufyer.plugin.justMakesSense.copperHoppper.HopperRegistry;
import org.jufyer.plugin.justMakesSense.copperHoppper.listener.CopperHopperBlockListener;
import org.jufyer.plugin.justMakesSense.copperHoppper.listener.CopperHopperItemListener;
import org.jufyer.plugin.justMakesSense.glisteringMelon.GlisteringMelonEatListeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin implements Listener {

  private static Main instance;
  public static Main getInstance() {
    return instance;
  }

  //Banner on boats
  // https://download.mc-packs.net/pack/71d29e4e502078d9fc7fc8846b55be9c8fb13471.zip
  // 71d29e4e502078d9fc7fc8846b55be9c8fb13471

  //Copper Hoppers
  // https://download.mc-packs.net/pack/8dc9547f7273b0d2b8d24987d1c758b1eb74dfac.zip
  // 8dc9547f7273b0d2b8d24987d1c758b1eb74dfac


  public static Set<Location> loadedCopperHoppers = new HashSet<>();
  public static Set<Chunk> scannedChunks = new HashSet<>();

  public static HashMap<Hopper, ItemDisplay> copperHoppers = new HashMap<>();

  public static final int CMDWhiteBoatBanner = 23821521;

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    //createCustomConfig();

    Bukkit.getPluginManager().registerEvents(new ResourcePackListeners(), this);
    getLogger().info("The following features are enabled: ");

    // Cauldron Rework

      if (getCustomConfig().getBoolean("enable-ice-cauldrons")){
        getLogger().info("Ice cauldrons enabled");
        Bukkit.getPluginManager().registerEvents(new CauldronIceListeners(), this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
          CauldronIceListeners.loadFilledIceCauldrons();
          getLogger().info("Successfully loaded ice cauldrons after world initialization.");
        }, 1L);
      }

      if (getCustomConfig().getBoolean("enable-honey-cauldrons")) {
        getLogger().info("Honey cauldrons enabled");
        Bukkit.getPluginManager().registerEvents(new CauldronHoneyListeners(), this);

        Bukkit.getScheduler().runTaskLater(this, () -> {
          CauldronHoneyListeners.loadFilledHoneyCauldrons();
          getLogger().info("Successfully loaded honey cauldrons after world initialization.");
        }, 1L);
      }

      if (getCustomConfig().getBoolean("enable-remove-dye-cauldrons")) {
        Bukkit.getPluginManager().registerEvents(new CauldronRemoveDyeListeners(), this);
        getLogger().info("Remove-Dye cauldrons enabled");
      }

      Bukkit.getPluginManager().registerEvents(new CauldronDispenserListeners(), this);

    // Glistering Melon
      if (getCustomConfig().getBoolean("enable-edible-glistering-melon")) {
        Bukkit.getPluginManager().registerEvents(new GlisteringMelonEatListeners(), this);
        getLogger().info("Edible Glistering Melons enabled");
      }

    // Banner
      if (getCustomConfig().getBoolean("banner-on-boats")) {
        Bukkit.getPluginManager().registerEvents(new BannerOnBoatsListeners(), this);
        getLogger().info("Banner on boats enabled");
      }
      if (getCustomConfig().getBoolean("banner-on-beds")) {
        Bukkit.getPluginManager().registerEvents(new BannerOnBedsListeners(), this);
        getLogger().info("Banner on beds enabled");
      }

    // Copper Hopper
    if (getCustomConfig().getBoolean("copper-hopper")) {
      HopperRegistry.loadHopperWithItemDisplay();

      HopperRegistry.createCopperHopperItems();
      HopperRegistry.addCopperHopperRecipes();

      getServer().getPluginManager().registerEvents(new CopperHopperItemListener(), this);
      getServer().getPluginManager().registerEvents(new CopperHopperBlockListener(), this);

      getServer().getPluginManager().registerEvents(new HopperBlockEntity(), this);
      HopperBlockEntity.createRunner();

      getLogger().info("Copper hopper enabled");
    }
  }

  @Override
  public void onDisable() {
    if (getCustomConfig() == null) {
      return;
    }

    if (getCustomConfig().getBoolean("enable-ice-cauldrons")) {
      CauldronIceListeners.saveFilledIceCauldrons();
    }
    if (getCustomConfig().getBoolean("enable-honey-cauldrons")) {
      CauldronHoneyListeners.saveFilledHoneyCauldrons();
    }
    if (getCustomConfig().getBoolean("copper-hopper")) {
      HopperRegistry.saveHopperWithItemDisplay();
    }
  }

  public FileConfiguration getCustomConfig() {
    return this.getConfig();
  }
}
