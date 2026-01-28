package org.jufyer.plugin.justMakesSense.features.dyedtorches;

import org.bukkit.NamespacedKey;
import org.jufyer.plugin.justMakesSense.Main;

public enum DyedTorchVariant {
  BLACK(10, "black"),
  BLUE(20, "blue"),
  BROWN(30, "brown"),
  CYAN(40, "cyan"),
  GRAY(50, "gray"),
  GREEN(60, "green"),
  LIGHT_BLUE(70, "light_blue"),
  LIGHT_GRAY(80, "light_gray"),
  LIME(90, "lime"),
  MAGENTA(100, "magenta"),
  ORANGE(110, "orange"),
  PINK(120, "pink"),
  PURPLE(130, "purple"),
  RED(140, "red"),
  WHITE(150, "white"),
  YELLOW(160, "yellow");

  private final int id;
  private final String prefix;

  DyedTorchVariant(int id, String prefix) {
    this.id = id;
    this.prefix = prefix;
  }

  public float getCustomModelData() {
    return (float) this.id;
  }

  public float getCustomModelDataWall() {
    return (float) this.id * 17;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getRegistryName() {
    return this.prefix + "_torch";
  }

  public String getRegistryNameWall() {
    return this.prefix + "_torch_wall";
  }

  public NamespacedKey getItemKey() {
    return new NamespacedKey(Main.getInstance(), this.name() + "_DYED_TORCH_ITEM_KEY");
  }

  public NamespacedKey getBlockKey() {
    return new NamespacedKey(Main.getInstance(), this.name() + "_DYED_TORCH_BLOCK_KEY");
  }

  public NamespacedKey getWallItemKey() {
    return new NamespacedKey(Main.getInstance(), this.name() + "_DYED_TORCH_WALL_ITEM_KEY");
  }

  public NamespacedKey getWallBlockKey() {
    return new NamespacedKey(Main.getInstance(), this.name() + "_DYED_TORCH_WALL_BLOCK_KEY");
  }
}
