package org.jufyer.plugin.justMakesSense.features.melon;

import org.bukkit.NamespacedKey;
import org.jufyer.plugin.justMakesSense.Main;

public enum MelonVariants {
  FULL_BLOCK(10,"FULL_BLOCK", "ONE_PIECE_EATEN_BLOCK"),
  ONE_PIECE_EATEN(20, "ONE_PIECE_EATEN_BLOCK", "TWO_PIECES_EATEN_BLOCK"),
  TWO_PIECES_EATEN(30, "TWO_PIECES_EATEN_BLOCK", "THREE_PIECES_EATEN_BLOCK"),
  THREE_PIECES_EATEN(40, "THREE_PIECES_EATEN_BLOCK", "FOUR_PIECES_EATEN_BLOCK"),
  FOUR_PIECES_EATEN(50, "FOUR_PIECES_EATEN_BLOCK", "");

  private final int id;
  private final String prefix;
  private final String nextPrefix;

  MelonVariants(int id, String prefix, String nextPrefix) {
    this.id = id;
    this.prefix = prefix;
    this.nextPrefix = nextPrefix;
  }

  public int getId() {
    return this.id;
  }

  public static MelonVariants getById(int id) {
    for (MelonVariants variant : values()) {
      if (variant.getId() == id) {
        return variant;
      }
    }
    return null;
  }

  public float getCustomModelData() {
    return this.id;
  }

  public NamespacedKey getBlockKey() {
    return new NamespacedKey(Main.getInstance(), this.prefix);
  }

  public NamespacedKey getNextBlockKey() {
    return new NamespacedKey(Main.getInstance(), this.nextPrefix);
  }
}
