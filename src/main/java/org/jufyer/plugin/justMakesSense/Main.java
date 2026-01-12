package org.jufyer.plugin.justMakesSense;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.justMakesSense.banner.bannerOnBeds.BannerOnBedsListeners;
import org.jufyer.plugin.justMakesSense.banner.bannerOnBoats.BannerOnBoatsListeners;
import org.jufyer.plugin.justMakesSense.cauldron.dispenser.CauldronDispenserListeners;
import org.jufyer.plugin.justMakesSense.cauldron.honey.CauldronHoneyListeners;
import org.jufyer.plugin.justMakesSense.cauldron.ice.CauldronIceListeners;
import org.jufyer.plugin.justMakesSense.cauldron.removeDye.CauldronRemoveDyeListeners;
import org.jufyer.plugin.justMakesSense.copperHoppper.HopperRegistry;
import org.jufyer.plugin.justMakesSense.glisteringMelon.GlisteringMelonEatListeners;
import org.jufyer.plugin.justMakesSense.recpies.api.RecipeType;
import org.jufyer.plugin.justMakesSense.recpies.api.RecipesAPI;
import org.jufyer.plugin.justMakesSense.recpies.impl.domains.ItemRecipe;
import org.jufyer.plugin.justMakesSense.recpies.impl.domains.recipes.RecipeBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class Main extends JavaPlugin implements Listener {

  private FileConfiguration customConfig;
  private static Main instance;
  public static Main getInstance() {
    return instance;
  }

  public static final int CMDWhiteBoatBanner = 23821521;

  private RecipesAPI recipesAPI;

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    //createCustomConfig();

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
      }

      Bukkit.getPluginManager().registerEvents(new CauldronDispenserListeners(), this);

    // Glistering Melon
      if (getCustomConfig().getBoolean("enable-edible-glistering-melon")) {
        Bukkit.getPluginManager().registerEvents(new GlisteringMelonEatListeners(), this);
      }

    // Banner
      Bukkit.getPluginManager().registerEvents(new BannerOnBoatsListeners(), this);
      Bukkit.getPluginManager().registerEvents(new BannerOnBedsListeners(), this);

    recipesAPI = new RecipesAPI(this, true);
    //TODO: Customize
    ItemRecipe recipe1 = new RecipeBuilder()
      .setType(RecipeType.CRAFTING_SHAPELESS)
      .setName("example-simple")
      .setResult(new ItemStack(Material.DIAMOND))
      .setAmount(64)
      .addIngredient(Material.DIRT)
      .build();
    recipesAPI.addRecipe(recipe1);
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
  }

  public FileConfiguration getCustomConfig() {
    return this.customConfig;
  }

  private void createCustomConfig() {
    File customConfigFile = new File(getDataFolder(), "config.yml");
    if (!customConfigFile.exists()) {
      getDataFolder().mkdirs();
      try {
        customConfigFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    customConfig = new YamlConfiguration();

    try {
      customConfig.load(customConfigFile);
      customConfig.options().copyDefaults(true);

      //TODO: Add comments to config file

      //Ice Cauldron
      customConfig.addDefault("enable-ice-cauldrons", true);

      //Honey Cauldron
      customConfig.addDefault("enable-honey-cauldrons", true);

      //Dispenser cauldron
      customConfig.addDefault("allow-interaction-from-below", true);
      customConfig.addDefault("enable-water", true);
      customConfig.addDefault("enable-lava", true);
      customConfig.addDefault("enable-powder-snow", true);

      //Remove dye cauldron
      customConfig.addDefault("enable-remove-dye-cauldrons", true);

      //Glistering Melon edible
      customConfig.addDefault("enable-edible-glistering-melon", true);

      customConfig.save(customConfigFile);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }
}
