package org.jufyer.plugin.justMakesSense.features.mobs.zombie.jungle;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jufyer.plugin.justMakesSense.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JungleZombieAITrait extends Trait {
  private static final NamespacedKey poisonCloudKey = new NamespacedKey(Main.getInstance(), "POISON_CLOUD");
  private static final List<AreaEffectCloud> poisonClouds = new ArrayList<>();

  private static final double DETECTION_RANGE = 40.0;
  private static final double FOLLOW_RANGE = 16.0;
  private static final double ATTACK_RANGE = 2.0;
  private static final double MOVEMENT_SPEED = 0.7;
  private static final int ATTACK_COOLDOWN = 20;
  private static final int SPAWN_POISON_COOLDOWN = 40;
  private static final float ATTACK_DAMAGE = 3.0f;
  private static final int POISON_LIFETIME = 200;

  private Player target;
  private int attackCooldown = 0;
  private int poisonSpawnCooldown = 0;
  private int idleTicks = 0;
  private Location lastTargetLoc;

  public JungleZombieAITrait() {
    super("JungleZombieAI");
  }

  @Override
  public void run() {
    if (!npc.isSpawned()) return;

    Entity entity = npc.getEntity();
    if (!(entity instanceof LivingEntity)) return;

    Navigator nav = npc.getNavigator();

    decreaseCooldowns();
    burnZombie();

    // Target acquisition and validation
    if (target == null || !isValidTarget(target)) {
      target = findNearestValidPlayer();
      lastTargetLoc = null;

      if (target == null) {
        idleTicks++;
        if (idleTicks > 100 && nav.isNavigating()) {
          nav.cancelNavigation();
        }
        lookAtNearestPlayer();
        return;
      }
    }

    idleTicks = 0;

    lineOfSightCheck(nav);
    attackAndChase(nav);
  }

  private boolean isValidTarget(Player p) {
    if (p == null || !p.isOnline() || p.isDead()) {
      return false;
    }

    if (p.getGameMode() == org.bukkit.GameMode.CREATIVE ||
      p.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
      return false;
    }

    Location zombieLoc = npc.getEntity().getLocation();
    double distance = p.getLocation().distance(zombieLoc);

    return distance <= DETECTION_RANGE;
  }

  private Player findNearestValidPlayer() {
    Location loc = npc.getEntity().getLocation();
    LivingEntity zombie = (LivingEntity) npc.getEntity();

    return loc.getWorld().getPlayers().stream()
      .filter(this::isValidTarget)
      .filter(p -> {
        // Vanilla zombies can detect through some blocks but not all
        double dist = p.getLocation().distance(loc);
        return dist <= FOLLOW_RANGE || zombie.hasLineOfSight(p);
      })
      .min((p1, p2) -> Double.compare(
        p1.getLocation().distance(loc),
        p2.getLocation().distance(loc)
      ))
      .orElse(null);
  }

  private Player findNearestPlayer() {
    Location loc = npc.getEntity().getLocation();
    LivingEntity zombie = (LivingEntity) npc.getEntity();

    return loc.getWorld().getPlayers().stream()
      .filter(p -> {
        // Vanilla zombies can detect through some blocks but not all
        double dist = p.getLocation().distance(loc);
        return dist <= FOLLOW_RANGE || zombie.hasLineOfSight(p);
      })
      .min((p1, p2) -> Double.compare(
        p1.getLocation().distance(loc),
        p2.getLocation().distance(loc)
      ))
      .orElse(null);
  }

  private void performAttack(Player player) {
    Entity entity = npc.getEntity();

    if (entity instanceof Player) {
      ((Player) entity).swingMainHand();
    }

    float damage = ATTACK_DAMAGE;
    switch (player.getWorld().getDifficulty()) {
      case EASY:
        damage = 2.0f;
        break;
      case NORMAL:
        damage = 3.0f;
        break;
      case HARD:
        damage = 4.5f;
        break;
      default:
        break;
    }

    player.damage(damage, entity);

    Vector direction = player.getLocation().toVector().subtract(entity.getLocation().toVector());

    if (direction.lengthSquared() > 0) {
      direction
        .normalize()
        .subtract(entity.getLocation().toVector())
        .multiply(0.0001)
        .setY(0.1);
    } else {
      direction = new Vector(0, 0, 0);
    }

    player.setVelocity(player.getVelocity().add(direction));

    if (poisonSpawnCooldown == 0) {
      player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1));
      spawnCloud(entity.getLocation());
      poisonSpawnCooldown = SPAWN_POISON_COOLDOWN;
    }
  }

  private void spawnCloud(Location location) {
    AreaEffectCloud cloud = (AreaEffectCloud) location.getWorld().spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
    cloud.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 20,1), true);

    cloud.getPersistentDataContainer().set(poisonCloudKey, PersistentDataType.BOOLEAN, true);
    poisonClouds.add(cloud);

    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
      cloud.remove();
    }, POISON_LIFETIME);
  }

  private void lineOfSightCheck(Navigator nav) {
    Entity entity = npc.getEntity();
    if (!(entity instanceof LivingEntity)) return;

    LivingEntity zombie = (LivingEntity) entity;

    Location targetLoc = target.getLocation();

    if (!zombie.hasLineOfSight(target)) {
      if (lastTargetLoc == null) {
        lastTargetLoc = targetLoc.clone();
      } else if (lastTargetLoc.distance(targetLoc) > 5) {
        // Target moved significantly without LOS
        target = null;
        nav.cancelNavigation();
        return;
      }
    } else {
      lastTargetLoc = targetLoc.clone();
    }
  }

  private void decreaseCooldowns() {
    if (attackCooldown > 0) {
      attackCooldown--;
    }
    if (poisonSpawnCooldown > 0) {
      poisonSpawnCooldown--;
    }
  }

  private void attackAndChase(Navigator nav) {
    Entity entity = npc.getEntity();
    if (!(entity instanceof LivingEntity)) return;

    LivingEntity zombie = (LivingEntity) entity;

    Location targetLoc = target.getLocation();
    Location zombieLoc = zombie.getLocation();
    double distance = zombieLoc.distance(targetLoc);

    if (distance <= ATTACK_RANGE) {
      nav.cancelNavigation();

      // Face the target
      Vector direction = targetLoc.toVector().subtract(zombieLoc.toVector());
      Location lookAt = zombieLoc.clone();
      lookAt.setDirection(direction);
      zombie.teleport(lookAt);

      if (attackCooldown == 0) {
        performAttack(target);
        attackCooldown = ATTACK_COOLDOWN;
      }
    } else if (distance <= FOLLOW_RANGE) {
      if (!nav.isNavigating() ||
        nav.getTargetAsLocation() == null ||
        nav.getTargetAsLocation().distance(targetLoc) > 2) {
        nav.setTarget(target, true);
        nav.getLocalParameters().speedModifier((float) MOVEMENT_SPEED);
        nav.getLocalParameters().stuckAction(null);
        nav.getLocalParameters().distanceMargin(1.5);
      }
    }

    else if (distance > FOLLOW_RANGE && distance <= DETECTION_RANGE) {
      if (nav.isNavigating()) {
        nav.setTarget(target, true);
      } else {
        target = null;
      }
    } else {
      target = null;
      nav.cancelNavigation();
    }
  }

  private final Random random = new Random();

  private void burnZombie() {
    if (!(npc.getEntity() instanceof LivingEntity zombie)) return;
    World world = zombie.getWorld();

    // 1. Muss Tag sein
    if (!world.isDayTime()) return;

    // 2. Regen / Gewitter verhindert Brennen
    if (world.hasStorm() || world.isThundering()) return;

    // 3. Zombie darf nicht im Wasser sein
    if (zombie.isInWaterOrRain()) return;

    Location loc = zombie.getLocation();

    // 4. Sky-Light prüfen (Vanilla benutzt > 0.5F)
    float skyLight = loc.getBlock().getLightFromSky() / 15.0F;
    if (skyLight <= 0.5F) return;

    // 5. Himmel muss sichtbar sein
    if (!(zombie.getWorld().getHighestBlockAt(loc).getY() <= loc.getY())) return;

    // 6. Zufallschance (Vanilla-ähnlich)
    if (random.nextFloat() >= (skyLight - 0.4F) / 2.0F) return;

    // 7. Helm-Logik
    ItemStack helmet = zombie.getEquipment().getHelmet();
    if (helmet != null && helmet.getType() != Material.AIR) {
      if (helmet.getType().getMaxDurability() > 0) {
        helmet.setDurability((short) (helmet.getDurability() + random.nextInt(2)));
        if (helmet.getDurability() >= helmet.getType().getMaxDurability()) {
          zombie.getEquipment().setHelmet(null);
        }
      }
      return;
    }

    // 8. Feuer setzen (8 Sekunden)
    zombie.setFireTicks(8 * 20);
  }


  private void lookAtNearestPlayer() {
    if (!npc.isSpawned()) return;

    Player zombie = (Player) npc.getEntity();
    if (zombie == null) return;

    Player player = findNearestPlayer();
    if (player == null) return;

    Location targetLoc = player.getLocation();
    Location zombieLoc = zombie.getLocation();

    Vector direction = targetLoc.toVector().subtract(zombieLoc.toVector());
    Location lookAt = zombieLoc.clone();
    lookAt.setDirection(direction);
    zombie.teleport(lookAt);
  }

  public static void removeAllPoisonClouds() {
    for (AreaEffectCloud cloud : poisonClouds) {
      cloud.remove();
    }
    poisonClouds.clear();
  }
}
