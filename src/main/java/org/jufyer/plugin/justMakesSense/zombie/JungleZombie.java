package org.jufyer.plugin.justMakesSense.zombie;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jufyer.plugin.justMakesSense.Main;

public class JungleZombie extends Zombie {
  public static NamespacedKey JungleZombieKey = new NamespacedKey(Main.getInstance(), "JUNGLE_ZOMBIE");
  //public static NamespacedKey JungleZombieArmorstandKey = new NamespacedKey(Main.getInstance(), "JUNGLE_ZOMBIE_ARMORSTAND");

  public JungleZombie(Location loc) {
    super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());

    this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
    this.getBukkitEntity().getPersistentDataContainer().set(JungleZombieKey, PersistentDataType.BOOLEAN, true);
    this.setInvulnerable(false);
    this.setCustomNameVisible(false);

    this.persist = true;

    ((CraftWorld) loc.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

  }
}
