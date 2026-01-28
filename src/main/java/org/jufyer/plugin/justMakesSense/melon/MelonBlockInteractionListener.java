package org.jufyer.plugin.justMakesSense.melon;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.justMakesSense.Main;
import org.jufyer.plugin.justMakesSense.copperHoppper.CopperVariant;

import java.util.*;

public class MelonBlockInteractionListener implements Listener {
  private static HashMap<Location, ItemDisplay> melonBlocks = new HashMap<>();
  private static HashMap<Location, Stairs.Shape> melonBlockStairs = new HashMap<>();

  private final Map<Player, Long> lastInteractedBlockTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  private static final NamespacedKey STAGE_KEY = new NamespacedKey(Main.getInstance(), "MELON_BLOCK_STAGE");
  private static Set<Chunk> scannedChunks = new HashSet<>();

  private void spawnMelonBlock(Location loc) {
    Location spawnLoc = loc.toBlockLocation();
    spawnLoc.setPitch(0);
    spawnLoc.setYaw(0);

    if (spawnLoc.getBlock().getType() != Material.MELON) {
      spawnLoc.getBlock().setType(Material.MELON);
    }

    ItemDisplay display = (ItemDisplay) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.ITEM_DISPLAY);
    display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);

    display.getPersistentDataContainer().set(STAGE_KEY, PersistentDataType.INTEGER, 10);

    melonBlocks.put(spawnLoc, display);
  }

  private static boolean subtractMelonPiece(Location loc) {
    if (melonBlocks.containsKey(loc.toBlockLocation()) || melonBlockStairs.containsKey(loc.toBlockLocation())) {
      Block block = loc.toBlockLocation().getBlock();
      if (block.getBlockData() instanceof Stairs stairs) {
        ItemDisplay display = melonBlocks.get(loc.toBlockLocation());
        if (display == null) return false;

        Integer currentId = display.getPersistentDataContainer().get(STAGE_KEY, PersistentDataType.INTEGER);
        if (currentId == null) return false;
        int nextId = currentId + 10;

        if (nextId >= 50) {
          block.setType(Material.ACACIA_SLAB);
          melonBlockStairs.remove(loc.toBlockLocation());

          ItemStack itemStack = display.getItemStack();
          ItemMeta itemMeta = itemStack.getItemMeta();
          if (itemMeta != null) {
            var customData = itemMeta.getCustomModelDataComponent();
            customData.setFloats(List.of(60.0f));
            itemMeta.setCustomModelDataComponent(customData);
            itemStack.setItemMeta(itemMeta);
            display.setItemStack(itemStack);
          }

          display.getPersistentDataContainer().set(STAGE_KEY, PersistentDataType.INTEGER, 60);
          return true;
        }

        MelonVariants nextVariant = MelonVariants.getById(nextId);
        if (nextVariant == null) return false;

        ItemStack itemStack = display.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
          var customData = itemMeta.getCustomModelDataComponent();
          customData.setFloats(List.of(nextVariant.getCustomModelData()));
          itemMeta.setCustomModelDataComponent(customData);
          itemStack.setItemMeta(itemMeta);
          display.setItemStack(itemStack);
        }

        display.getPersistentDataContainer().set(STAGE_KEY, PersistentDataType.INTEGER, nextId);

        Stairs.Shape shape = stairs.getShape();
        switch (shape) {
          case INNER_LEFT -> shape = Stairs.Shape.STRAIGHT;
          case STRAIGHT -> shape = Stairs.Shape.OUTER_RIGHT;
        }
        stairs.setShape(shape);
        block.setBlockData(stairs);

        melonBlockStairs.put(loc.toBlockLocation(), shape);
        return true;

      } else if (block.getType().equals(Material.ACACIA_PLANKS) || block.getType().equals(Material.MELON)) {
        if (melonBlocks.containsKey(loc.toBlockLocation())) {

          block.setType(Material.ACACIA_STAIRS);
          Stairs.Shape shape = Stairs.Shape.INNER_LEFT;

          if (block.getBlockData() instanceof Stairs) {
            Stairs stairs = (Stairs) block.getBlockData();
            stairs.setShape(shape);
            block.setBlockData(stairs);
            melonBlockStairs.put(loc.toBlockLocation(), shape);
          }

          ItemDisplay display = melonBlocks.get(loc.toBlockLocation());

          ItemStack fullMelonBlockDisplayItem = new ItemStack(Material.LARGE_AMETHYST_BUD);
          ItemMeta itemMeta = fullMelonBlockDisplayItem.getItemMeta();
          if (itemMeta == null) return false;
          Float customModelDataFloat = MelonVariants.ONE_PIECE_EATEN.getCustomModelData();
          var customData = itemMeta.getCustomModelDataComponent();
          customData.setFloats(List.of(customModelDataFloat));

          itemMeta.setCustomModelDataComponent(customData);
          fullMelonBlockDisplayItem.setItemMeta(itemMeta);

          display.setItemStack(fullMelonBlockDisplayItem);

          display.getPersistentDataContainer().set(STAGE_KEY, PersistentDataType.INTEGER, MelonVariants.ONE_PIECE_EATEN.getId());
          return true;
        }
      } else if (block.getType().equals(Material.ACACIA_SLAB)) {
        if (melonBlocks.containsKey(loc.toBlockLocation())) {

          block.setType(Material.AIR);

          ItemDisplay display = melonBlocks.get(loc.toBlockLocation());
          display.remove();
          melonBlocks.remove(loc.toBlockLocation());
          melonBlockStairs.remove(loc.toBlockLocation());
          return true;
        }
      }
    }
    return false;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getClickedBlock() == null) return;
    if (melonBlocks.containsKey(event.getClickedBlock().getLocation().toBlockLocation())) {
      if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
      if (event.getPlayer().isSneaking()) return;

      Player player = event.getPlayer();
      long currentTime = System.currentTimeMillis();
      long lastPlacedTime = lastInteractedBlockTimes.getOrDefault(player, 0L);
      if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
        lastInteractedBlockTimes.put(player, currentTime);

        if (subtractMelonPiece(event.getClickedBlock().getLocation().toBlockLocation())) {
          player.setFoodLevel(Math.min(20, player.getFoodLevel() + 2));
          player.setSaturation(Math.min(20f, player.getSaturation() + 1.2f));

          player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
          player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.5f, 1.0f);

          Location loc = event.getClickedBlock().getLocation().toBlockLocation();
          loc.getWorld().spawnParticle(Particle.EGG_CRACK, loc.add(0.5, 0.5, 0.5), 10);
        }
      }
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (melonBlocks.containsKey(event.getBlock().getLocation().toBlockLocation())) {
      ItemDisplay display = melonBlocks.get(event.getBlock().getLocation().toBlockLocation());
      display.remove();
      melonBlocks.remove(event.getBlock().getLocation().toBlockLocation());
      if (melonBlockStairs.containsKey(event.getBlock().getLocation().toBlockLocation())) {
        melonBlockStairs.remove(event.getBlock().getLocation().toBlockLocation());
      }

      event.setDropItems(false);
      Location dropLoc = event.getBlock().getLocation();
      Random random = new Random();
      dropLoc.getWorld().dropItemNaturally(dropLoc, new ItemStack(Material.MELON_SLICE, random.nextInt(1,3)));
    }
  }

  @EventHandler
  public void onBlockGrow(BlockGrowEvent event) {
    if (event.getBlock().getType().equals(Material.MELON)) {
      spawnMelonBlock(event.getBlock().getLocation());
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    if (event.getBlock().getType().equals(Material.MELON)) {
      spawnMelonBlock(event.getBlock().getLocation());
    }
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    Chunk chunk = event.getChunk();
    if (scannedChunks.contains(chunk)) return;
    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
      for (int x = 0; x < 16; x++) {
        for (int z = 0; z < 16; z++) {
          for (int y = chunk.getWorld().getMinHeight(); y < chunk.getWorld().getMaxHeight(); y++) {
            Block block = chunk.getBlock(x, y, z);

            if (block.getType() == Material.MELON) {
              Location loc = block.getLocation().toBlockLocation();

              Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                if (!melonBlocks.containsKey(loc)) {
                  spawnMelonBlock(loc);
                }
              });
            }
          }
        }
      }
    });
  }

  public static void preserveStairShape() {
    Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
      for (Location loc : melonBlockStairs.keySet()) {
        Block block = loc.getBlock();
        if (block.getBlockData() instanceof Stairs) {
          Stairs stairs = (Stairs) block.getBlockData();
          stairs.setShape(melonBlockStairs.get(block.getLocation()));
          block.setBlockData(stairs);
        }
      }
    }, 1L, 1L);
  }
}
