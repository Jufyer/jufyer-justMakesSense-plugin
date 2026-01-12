package org.jufyer.plugin.justMakesSense.glisteringMelon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlisteringMelonEatListeners implements Listener {

  private static final int FOOD = 6;           // hunger points
  private static final float SATURATION = 14.4f;
  private static final boolean GIVE_REGEN = true;

  @EventHandler
  public void onUse(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_AIR &&
      event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    ItemStack item = event.getItem();
    if (item == null || item.getType() != Material.GLISTERING_MELON_SLICE) return;

    Player player = event.getPlayer();

    event.setCancelled(true);

    // Consume item
    item.setAmount(item.getAmount() - 1);

    // Restore hunger
    player.setFoodLevel(Math.min(20, player.getFoodLevel() + FOOD));
    player.setSaturation(Math.min(20f, player.getSaturation() + SATURATION));

    // Optional regeneration (like golden food)
    if (GIVE_REGEN) {
      player.addPotionEffect(
        new PotionEffect(PotionEffectType.REGENERATION, 4*20, 1)
      );
    }

    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.5f, 1.0f);

    player.swingMainHand();
  }
}
