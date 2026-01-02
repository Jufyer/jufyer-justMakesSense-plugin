package org.jufyer.plugin.justMakesSense;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.justMakesSense.cauldron.honey.CauldronHoneyListeners;
import org.jufyer.plugin.justMakesSense.cauldron.ice.CauldronIceListeners;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin implements Listener {

  private FileConfiguration customConfig;
  private static Main instance;
  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    createCustomConfig();

    getLogger().info("The following features are enabled: ");
    if (getCustomConfig().getBoolean("Cauldron rework")){
      getLogger().info("Cauldron rework");

      Bukkit.getPluginManager().registerEvents(new CauldronHoneyListeners(), this);
      Bukkit.getPluginManager().registerEvents(new CauldronIceListeners(), this);

      Bukkit.getScheduler().runTaskLater(this, () -> {
        CauldronHoneyListeners.loadFilledHoneyCauldrons();
        CauldronIceListeners.loadFilledIceCauldrons();
        getLogger().info("Successfully loaded honey cauldrons after world initialization.");
      }, 1L);
    }
  }

  @Override
  public void onDisable(){
    if (getCustomConfig().getBoolean("Cauldron rework")){
      CauldronHoneyListeners.saveFilledHoneyCauldrons();
      CauldronIceListeners.saveFilledIceCauldrons();
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
      customConfig.addDefault("Cauldron rework", true);

      customConfig.save(customConfigFile);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }
}
