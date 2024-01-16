package dev.zontreck.thresholds.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class UnstableSingularity extends SimpleFoiledItem
{

    public UnstableSingularity(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockState block = pContext.getLevel().getBlockState(pContext.getClickedPos());

        if(block.getBlock().defaultDestroyTime() < 0)
        {
            pContext.getLevel().explode(pContext.getPlayer(), pContext.getClickedPos().getX(), pContext.getClickedPos().getY(), pContext.getClickedPos().getZ(), 16, true, Level.ExplosionInteraction.TNT);

            pContext.getLevel().setBlock(pContext.getClickedPos(), Blocks.AIR.defaultBlockState(), 0, 0);

            pContext.getItemInHand().shrink(1);

        }

        return InteractionResult.CONSUME;

    }
}
