package dev.zontreck.otemod.blocks;

import dev.zontreck.ariaslib.util.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;


public class RotatableBlockCustomVoxels extends RotatableBlock
{
    private Map<Direction, VoxelShape> rotatedShapes = new HashMap<>();

    protected RotatableBlockCustomVoxels(Properties properties, VoxelShape north, VoxelShape south, VoxelShape east, VoxelShape west) {
        super(properties);
        rotatedShapes = Maps.of(new Maps.Entry<>(Direction.NORTH, north), new Maps.Entry<>(Direction.SOUTH, south), new Maps.Entry<>(Direction.WEST, west), new Maps.Entry<>(Direction.EAST, east), new Maps.Entry<>(Direction.NORTH, north), new Maps.Entry<>(Direction.DOWN, north));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return rotatedShapes.get(facing);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return true;
    }
}
