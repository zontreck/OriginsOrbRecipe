package dev.zontreck.thresholds.blocks.types;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.EnumMap;

/** Pane block with sensible culling */
public class AlternatePaneBlock extends IronBarsBlock {
    public static final EnumMap<Direction,BooleanProperty> DIRECTIONS;
    static {
      DIRECTIONS = new EnumMap<>(Direction.class);
      DIRECTIONS.put(Direction.NORTH, NORTH);
      DIRECTIONS.put(Direction.EAST, EAST);
      DIRECTIONS.put(Direction.SOUTH, SOUTH);
      DIRECTIONS.put(Direction.WEST, WEST);
    }
  
    public AlternatePaneBlock(Properties builder) {
      super(builder);
    }
  
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
      // cull top and bottom if all props that we have are contained in the above or below
      if (adjacentBlockState.getBlock() == this && side.getAxis().isVertical()) {
        for (Direction dir : Plane.HORIZONTAL) {
          BooleanProperty prop = DIRECTIONS.get(dir);
          if (state.getValue(prop) && !adjacentBlockState.getValue(prop)) {
            return false;
          }
        }
        return true;
      }
      return super.skipRendering(state, adjacentBlockState, side);
    }
  }
  