package org.jufyer.plugin.justMakesSense;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.justMakesSense.features.anvil.AnvilRepairListener;
import org.jufyer.plugin.justMakesSense.features.banner.BannerBoatListener;
import org.jufyer.plugin.justMakesSense.features.cauldron.CauldronDispenserListener;
import org.jufyer.plugin.justMakesSense.features.cauldron.CauldronHoneyListener;
import org.jufyer.plugin.justMakesSense.features.cauldron.CauldronIceListener;
import org.jufyer.plugin.justMakesSense.features.cauldron.CauldronDyeListener;
import org.jufyer.plugin.justMakesSense.features.copperhopper.CopperHopperBlock;
import org.jufyer.plugin.justMakesSense.features.copperhopper.CopperHopperRegistry;
import org.jufyer.plugin.justMakesSense.features.copperhopper.listener.CopperHopperBlockListener;
import org.jufyer.plugin.justMakesSense.features.copperhopper.listener.CopperHopperItemListener;
import org.jufyer.plugin.justMakesSense.features.dyedtorches.DyedTorchRegistry;
import org.jufyer.plugin.justMakesSense.features.dyedtorches.listener.DyedTorchBlockListener;
import org.jufyer.plugin.justMakesSense.features.dyedtorches.listener.DyedTorchItemListener;
import org.jufyer.plugin.justMakesSense.features.mobs.pets.PetProtectListener;
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.jungle.JungleZombie;
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.jungle.JungleZombieAITrait;
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.snow.SnowZombie;
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.snow.SnowZombieAITrait;
import org.jufyer.plugin.justMakesSense.features.melon.GlisteringMelonEatListener;
import org.jufyer.plugin.justMakesSense.features.mobs.goat.GoatDropListener;
import org.jufyer.plugin.justMakesSense.features.mobs.husk.HuskDropListener;
import org.jufyer.plugin.justMakesSense.features.melon.MelonBlockInteractionListener;
import org.jufyer.plugin.justMakesSense.features.mobs.zombie.*;

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
      Bukkit.getPluginManager().registerEvents(new CauldronIceListener(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        CauldronIceListener.loadFilledIceCauldrons();
        getLogger().info("Successfully loaded ice cauldrons after world initialization.");
      }, 1L);
    }

    // Honey Cauldrons
    if (getCustomConfig().getBoolean("enable-honey")) {
      getLogger().info("Honey cauldrons enabled");
      Bukkit.getPluginManager().registerEvents(new CauldronHoneyListener(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        CauldronHoneyListener.loadFilledHoneyCauldrons();
        getLogger().info("Successfully loaded honey cauldrons after world initialization.");
      }, 1L);
    }

    // Remove dye with cauldron
    if (getCustomConfig().getBoolean("enable-remove-dye")) {
      Bukkit.getPluginManager().registerEvents(new CauldronDyeListener(), this);
      getLogger().info("Remove-Dye cauldrons enabled");
    }

    Bukkit.getPluginManager().registerEvents(new CauldronDispenserListener(), this);

    // Glistering Melon
    if (getCustomConfig().getBoolean("enable-edible-glistering-melon")) {
      Bukkit.getPluginManager().registerEvents(new GlisteringMelonEatListener(), this);
      getLogger().info("Edible Glistering Melons enabled");
    }

    // Banner
    if (getCustomConfig().getBoolean("banner-on-boats")) {
      Bukkit.getPluginManager().registerEvents(new BannerBoatListener(), this);
      getLogger().info("Banner on boats enabled");
    }

    // Copper Hopper
    if (getCustomConfig().getBoolean("copper-hopper")) {
      Bukkit.getScheduler().runTaskLater(this, () -> {
        CopperHopperRegistry.loadHopperWithItemDisplay();
        getLogger().info("Successfully loaded copper hoppers after world initialization.");
      }, 1L);

      CopperHopperRegistry.createCopperHopperItems();
      CopperHopperRegistry.addCopperHopperRecipes();

      getServer().getPluginManager().registerEvents(new CopperHopperItemListener(), this);
      getServer().getPluginManager().registerEvents(new CopperHopperBlockListener(), this);

      getServer().getPluginManager().registerEvents(new CopperHopperBlock(), this);
      CopperHopperBlock.createRunner();

      getLogger().info("Copper hopper enabled");
    }

    // Husks Drop Sand
    if (getCustomConfig().getBoolean("husks-drop-sand")) {
      Bukkit.getPluginManager().registerEvents(new HuskDropListener(), this);
      getLogger().info("Husks drop sand enabled");
    }

    // Goat Drop mutton
    if (getCustomConfig().getBoolean("goat-drop-mutton")) {
      Bukkit.getPluginManager().registerEvents(new GoatDropListener(), this);
      getLogger().info("Goat drop mutton enabled");
    }

    // Dyed torches
    if (getCustomConfig().getBoolean("dyed-torches")) {
      DyedTorchRegistry.createDyedTorchItems();
      DyedTorchRegistry.addDyedTorchRecipes();

      Bukkit.getPluginManager().registerEvents(new DyedTorchBlockListener(), this);
      Bukkit.getPluginManager().registerEvents(new DyedTorchItemListener(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        DyedTorchRegistry.loadDyedTorchWithItemDisplay();
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

        Bukkit.getPluginManager().registerEvents(new ZombieDeathListener(), this);
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

        Bukkit.getPluginManager().registerEvents(new ZombieDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new SnowZombie(), this);
        getLogger().info("Snow Zombie enabled");
      }, 1L);
    }

    // Right click on melon blocks to eat a slice
    if (getCustomConfig().getBoolean("melon-block-update")) {
      Bukkit.getPluginManager().registerEvents(new MelonBlockInteractionListener(), this);
      MelonBlockInteractionListener.preserveStairShape();

      Bukkit.getScheduler().runTaskLater(this, () -> {
        MelonBlockInteractionListener.loadMelonBlockWithItemDisplay();
        getLogger().info("Successfully loaded melon blocks after world initialization.");
      }, 1L);

      getLogger().info("Melon Block update enabled");
    }

    // Repair anvil with Iron Block
    if (getCustomConfig().getBoolean("repair-anvil")) {
      Bukkit.getPluginManager().registerEvents(new AnvilRepairListener(), this);
      getLogger().info("Anvil repair enabled");
    }

    // Protect Pets
    if (getCustomConfig().getBoolean("protect-pets")) {
      Bukkit.getPluginManager().registerEvents(new PetProtectListener(), this);
      getLogger().info("Protect pets enabled");
    }
  }

  @Override
  public void onDisable() {
    if (getCustomConfig().getBoolean("enable-ice")) {
      CauldronIceListener.saveFilledIceCauldrons();
    }
    if (getCustomConfig().getBoolean("enable-honey")) {
      CauldronHoneyListener.saveFilledHoneyCauldrons();
    }
    if (getCustomConfig().getBoolean("copper-hopper")) {
      CopperHopperRegistry.saveHopperWithItemDisplay();
    }
    if (getCustomConfig().getBoolean("dyed-torches")) {
      DyedTorchRegistry.saveDyedTorchWithItemDisplay();
    }
    if (getCustomConfig().getBoolean("jungle-zombie")) {
      JungleZombieAITrait.removeAllPoisonClouds();
    }
    if (getCustomConfig().getBoolean("snow-zombie")) {
      SnowZombieAITrait.removeAllSlownessClouds();
    }
    if (getCustomConfig().getBoolean("melon-block-update")) {
      MelonBlockInteractionListener.saveMelonBlockWithItemDisplay();
    }
  }

  public FileConfiguration getCustomConfig() {
    return this.getConfig();
  }
}
