package org.jufyer.plugin.justMakesSense.features.doubledoor;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoubleDoorSupport implements Listener {
  @EventHandler
  public void onInteractWithDoor(PlayerInteractEvent event) {
    if (!isDoorBlock(event.getClickedBlock()) || !event.getAction().isRightClick()) {
      return;
    }

    Block doorBlock = event.getClickedBlock();
    BlockState doorState = doorBlock.getState();
    Door door = (Door) doorState.getBlockData();

    boolean newDoorState = !door.isOpen();

    switch(door.getFacing()) {
      case NORTH: {
        switch(door.getHinge()) {
          case LEFT:
            Block eastBlock = doorBlock.getRelative(BlockFace.EAST, 1);
            updateNextDoor(doorBlock, eastBlock, newDoorState);
            break;
          case RIGHT:
            Block westBlock = doorBlock.getRelative(BlockFace.WEST, 1);
            updateNextDoor(doorBlock, westBlock, newDoorState);
            break;
        }
        break;
      }
      case SOUTH: {
        switch (door.getHinge()) {
          case LEFT:
            Block westBlock = doorBlock.getRelative(BlockFace.WEST, 1);
            updateNextDoor(doorBlock, westBlock, newDoorState);
            break;
          case RIGHT:
            Block eastBlock = doorBlock.getRelative(BlockFace.EAST, 1);
            updateNextDoor(doorBlock, eastBlock, newDoorState);
            break;
        }
        break;
      }
      case EAST: {
        switch (door.getHinge()) {
          case LEFT:
            Block southBlock = doorBlock.getRelative(BlockFace.SOUTH, 1);
            updateNextDoor(doorBlock, southBlock, newDoorState);
            break;
          case RIGHT:
            Block northBlock = doorBlock.getRelative(BlockFace.NORTH, 1);
            updateNextDoor(doorBlock, northBlock, newDoorState);
            break;
        }
        break;
      }
      case WEST: {
        switch (door.getHinge()) {
          case LEFT:
            Block northBlock = doorBlock.getRelative(BlockFace.NORTH, 1);
            updateNextDoor(doorBlock, northBlock, newDoorState);
            break;
          case RIGHT:
            Block southBlock = doorBlock.getRelative(BlockFace.SOUTH, 1);
            updateNextDoor(doorBlock, southBlock, newDoorState);
            break;
        }
        break;
      }
    }
  }

  private void updateNextDoor(Block doorBlock, Block block, boolean open) {
    Block blockBelowDoor = doorBlock.getWorld().getBlockAt(doorBlock.getLocation().add(0, -1, 0));
    Block blockBelowNext = block.getWorld().getBlockAt(block.getLocation().add(0, -1, 0));

    if (isDoorBlock(blockBelowDoor) && !isDoorBlock(blockBelowNext)) {
      return;
    }
    if (!isDoorBlock(blockBelowDoor) && isDoorBlock(blockBelowNext)) {
      return;
    }

    BlockState doorState = doorBlock.getState();
    Door originalDoor = (Door) doorState.getBlockData();
    if (isDoorBlock(block)) {
      BlockState nextDoorState = block.getState();
      Door otherDoor = (Door) nextDoorState.getBlockData();
      if (!originalDoor.getHinge().equals(otherDoor.getHinge())) {
        setDoorOpen(block, open);
      }
    }
  }

  public static boolean isDoorBlock(Block block) {
    if (block == null) return false;
    if (block.getType().toString().contains("IRON")) return false;
    return block.getType().toString().contains("DOOR") && !block.getType().toString().contains("TRAP");
  }

  public static void setDoorOpen(Block block, boolean open) {
    Door door = (Door) block.getBlockData();
    door.setOpen(open);
    block.setBlockData(door);
  }
}
