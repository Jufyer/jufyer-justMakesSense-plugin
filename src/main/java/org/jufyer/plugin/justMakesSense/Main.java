package org.jufyer.plugin.justMakesSense;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.justMakesSense.banner.bannerOnBoats.BannerOnBoatsListeners;
import org.jufyer.plugin.justMakesSense.cauldron.dispenser.CauldronDispenserListeners;
import org.jufyer.plugin.justMakesSense.cauldron.honey.CauldronHoneyListeners;
import org.jufyer.plugin.justMakesSense.cauldron.ice.CauldronIceListeners;
import org.jufyer.plugin.justMakesSense.cauldron.removeDye.CauldronRemoveDyeListeners;
import org.jufyer.plugin.justMakesSense.copperHoppper.HopperBlockEntity;
import org.jufyer.plugin.justMakesSense.copperHoppper.HopperRegistry;
import org.jufyer.plugin.justMakesSense.copperHoppper.listener.CopperHopperBlockListener;
import org.jufyer.plugin.justMakesSense.copperHoppper.listener.CopperHopperItemListener;
import org.jufyer.plugin.justMakesSense.dyedTorches.DyedTorchesRegistry;
import org.jufyer.plugin.justMakesSense.dyedTorches.listener.DyedTorchBlockListener;
import org.jufyer.plugin.justMakesSense.dyedTorches.listener.DyedTorchItemListener;
import org.jufyer.plugin.justMakesSense.glisteringMelon.GlisteringMelonEatListeners;
import org.jufyer.plugin.justMakesSense.goat.GoatDropMuttonListener;
import org.jufyer.plugin.justMakesSense.husk.HuskDropSandListener;
import org.jufyer.plugin.justMakesSense.melon.MelonBlockInteractionListener;
import org.jufyer.plugin.justMakesSense.zombie.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin implements Listener {

  private static Main instance;
  public static Main getInstance() {
    return instance;
  }

  public static Set<Location> loadedCopperHoppers = new HashSet<>();
  public static Set<Chunk> scannedChunks = new HashSet<>();
  public static HashMap<Location, ItemDisplay> copperHoppers = new HashMap<>();
  public static HashMap<Location, Integer> copperHopperItemCount = new HashMap<>();

  public static HashMap<Location, ItemDisplay> dyedTorches = new HashMap<>();

  public static final int CMDWhiteBoatBanner = 23821521;

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();

    Bukkit.getPluginManager().registerEvents(new ResourcePackListeners(), this);
    getLogger().info("The following features are enabled: ");

    // Cauldron Rework
    // Ice Cauldrons
    if (getCustomConfig().getBoolean("enable-ice")){
      getLogger().info("Ice cauldrons enabled");
      Bukkit.getPluginManager().registerEvents(new CauldronIceListeners(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        CauldronIceListeners.loadFilledIceCauldrons();
        getLogger().info("Successfully loaded ice cauldrons after world initialization.");
      }, 1L);
    }

    // Honey Cauldrons
    if (getCustomConfig().getBoolean("enable-honey")) {
      getLogger().info("Honey cauldrons enabled");
      Bukkit.getPluginManager().registerEvents(new CauldronHoneyListeners(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        CauldronHoneyListeners.loadFilledHoneyCauldrons();
        getLogger().info("Successfully loaded honey cauldrons after world initialization.");
      }, 1L);
    }

    // Remove dye with cauldron
    if (getCustomConfig().getBoolean("enable-remove-dye")) {
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

    // Copper Hopper
    if (getCustomConfig().getBoolean("copper-hopper")) {
      Bukkit.getScheduler().runTaskLater(this, () -> {
        HopperRegistry.loadHopperWithItemDisplay();
        getLogger().info("Successfully loaded copper hoppers after world initialization.");
      }, 1L);

      HopperRegistry.createCopperHopperItems();
      HopperRegistry.addCopperHopperRecipes();

      getServer().getPluginManager().registerEvents(new CopperHopperItemListener(), this);
      getServer().getPluginManager().registerEvents(new CopperHopperBlockListener(), this);

      getServer().getPluginManager().registerEvents(new HopperBlockEntity(), this);
      HopperBlockEntity.createRunner();

      getLogger().info("Copper hopper enabled");
    }

    // Husks Drop Sand
    if (getCustomConfig().getBoolean("husks-drop-sand")) {
      Bukkit.getPluginManager().registerEvents(new HuskDropSandListener(), this);
      getLogger().info("Husks drop sand enabled");
    }

    // Goat Drop mutton
    if (getCustomConfig().getBoolean("goat-drop-mutton")) {
      Bukkit.getPluginManager().registerEvents(new GoatDropMuttonListener(), this);
      getLogger().info("Goat drop mutton enabled");
    }

    // Dyed torches
    if (getCustomConfig().getBoolean("dyed-torches")) {
      DyedTorchesRegistry.createDyedTorchItems();
      DyedTorchesRegistry.addDyedTorchRecipes();

      Bukkit.getPluginManager().registerEvents(new DyedTorchBlockListener(), this);
      Bukkit.getPluginManager().registerEvents(new DyedTorchItemListener(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        DyedTorchesRegistry.loadDyedTorchWithItemDisplay();
        getLogger().info("Successfully loaded dyed torches after world initialization.");
      }, 1L);
    }

    Bukkit.getScheduler().runTaskLater(this, () -> {
      if (getCustomConfig().getBoolean("jungle-zombie") || getCustomConfig().getBoolean("snow-zombie")){
        if (!Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
          getLogger().severe("Citizens not found but the enabled features require it! Plugin disabled.");
          getLogger().severe("Look at the config and turn of any feature that requires Citizens or install the plugin from this link for free:");
          getLogger().severe("https://ci.citizensnpcs.co/job/Citizens2/");
          getServer().getPluginManager().disablePlugin(this);
        }else {
          getLogger().info("Successfully loaded Citizens.");
        }
      }
    }, 1L);

    // Jungle Zombie
    if (getCustomConfig().getBoolean("jungle-zombie")) {
      Bukkit.getScheduler().runTaskLater(this, () -> {
        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(
          net.citizensnpcs.api.trait.TraitInfo.create(JungleZombieAITrait.class)
            .withName("JungleZombieAI")
        );

        Bukkit.getPluginManager().registerEvents(new CustomZombieDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new JungleZombie(), this);
        getLogger().info("Jungle Zombie enabled");
      }, 1L);
    }

    // Snow Zombie
    if (getCustomConfig().getBoolean("snow-zombie")) {
      Bukkit.getScheduler().runTaskLater(this, () -> {
        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(
          net.citizensnpcs.api.trait.TraitInfo.create(SnowZombieAITrait.class)
            .withName("SnowZombieAI")
        );

        Bukkit.getPluginManager().registerEvents(new CustomZombieDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new SnowZombie(), this);
        getLogger().info("Snow Zombie enabled");
      }, 1L);
    }

    // Right click on melon blocks to eat a slice
    if (getCustomConfig().getBoolean("melon-block-update")) {
      Bukkit.getPluginManager().registerEvents(new MelonBlockInteractionListener(), this);
      MelonBlockInteractionListener.preserveStairShape();
    }

  }

  @Override
  public void onDisable() {
    if (getCustomConfig().getBoolean("enable-ice")) {
      CauldronIceListeners.saveFilledIceCauldrons();
    }
    if (getCustomConfig().getBoolean("enable-honey")) {
      CauldronHoneyListeners.saveFilledHoneyCauldrons();
    }
    if (getCustomConfig().getBoolean("copper-hopper")) {
      HopperRegistry.saveHopperWithItemDisplay();
    }
    if (getCustomConfig().getBoolean("dyed-torches")) {
      DyedTorchesRegistry.saveDyedTorchWithItemDisplay();
    }
    if (getCustomConfig().getBoolean("jungle-zombie")) {
      JungleZombieAITrait.removeAllPoisonClouds();
    }
    if (getCustomConfig().getBoolean("snow-zombie")) {
      SnowZombieAITrait.removeAllSlownessClouds();
    }
  }

  public FileConfiguration getCustomConfig() {
    return this.getConfig();
  }
}
