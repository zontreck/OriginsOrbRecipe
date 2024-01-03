package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.blocks.entity.CompressionChamberBlockEntity;
import dev.zontreck.otemod.blocks.entity.ModEntities;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.EnergySyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CompressionChamberBlock extends BaseEntityBlock
{

    public CompressionChamberBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CompressionChamberBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!lvl.isClientSide())
        {
            BlockEntity be = lvl.getBlockEntity(pos);
            if(be instanceof CompressionChamberBlockEntity)
            {
                CompressionChamberBlockEntity entity = (CompressionChamberBlockEntity) be;
                NetworkHooks.openScreen(((ServerPlayer)player), entity, pos);

                ModMessages.sendToPlayer(new EnergySyncS2CPacket(entity.getEnergyStorage().getEnergyStored(), entity.getBlockPos()), (ServerPlayer)player);

            }else{
                throw new IllegalStateException("Our container is missing!");
            }
        }

        return InteractionResult.sidedSuccess(lvl.isClientSide);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock())
        {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if(be instanceof CompressionChamberBlockEntity)
            {
                ((CompressionChamberBlockEntity)be).doDrop();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {

        return createTickerHelper(pBlockEntityType, ModEntities.COMPRESSION_CHAMBER.get(), CompressionChamberBlockEntity::tick);
    }
}
