package org.jufyer.plugin.justMakesSense.copperHoppper;

import org.bukkit.NamespacedKey;
import org.jufyer.plugin.justMakesSense.Main;

public enum CopperVariant {
  NORMAL(10, "copper_", 4),
  EXPOSED(20, "exposed_copper_", 6),
  WEATHERED(30, "weathered_copper_", 8),
  OXIDIZED(40, "oxidized_copper_", 12),
  WAXED(50, "waxed_copper_", 4),
  WAXED_EXPOSED(60, "waxed_exposed_copper_", 6),
  WAXED_WEATHERED(70, "waxed_weathered_copper_", 8),
  WAXED_OXIDIZED(80, "waxed_oxidized_copper_", 12);

  private final int id;
  private final String prefix;
  private final int speed;

  CopperVariant(int id, String prefix, Integer speed) {
    this.id = id;
    this.prefix = prefix;
    this.speed = speed;
  }

  public float getCustomModelData() {
    return (float) this.id;
  }

  public float getCustomModelDataSide() {
    return (float) this.id + 100;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getRegistryName() {
    return this.prefix + "hopper";
  }

  public String getChestName() {
    return this.prefix + "chest";
  }

  public NamespacedKey getItemKey() {
    return new NamespacedKey(Main.getInstance(), this.name() + "_COPPER_HOPPER_ITEM_KEY");
  }

  public NamespacedKey getBlockKey() {
    return new NamespacedKey(Main.getInstance(), this.name() + "_COPPER_HOPPER_BLOCK_KEY");
  }

  public int getSpeed() {
    return this.speed;
  }
}
