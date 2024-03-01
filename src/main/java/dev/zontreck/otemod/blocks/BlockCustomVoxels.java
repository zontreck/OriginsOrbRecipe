package dev.zontreck.otemod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCustomVoxels extends PartialTransparentBlock
{
    private VoxelShape superShape;
    protected BlockCustomVoxels(Properties p_54120_, VoxelShape shape) {
        super(p_54120_);
        this.superShape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return superShape;
    }
}
