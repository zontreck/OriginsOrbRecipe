package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.blocks.entity.ItemScrubberBlockEntity;
import dev.zontreck.otemod.blocks.entity.ModEntities;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.EnergySyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ItemScrubberBlock extends HorizontalDirectionalBlock implements EntityBlock
{

    public ItemScrubberBlock(Properties p_54120_) {
        super(p_54120_);
        //registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level lvl, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock()!=newState.getBlock())
        {
            BlockEntity bE = lvl.getBlockEntity(pos);
            if(bE instanceof ItemScrubberBlockEntity)
            {
                ((ItemScrubberBlockEntity)bE).doDrop();
            }
        }

        super.onRemove(state, lvl, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(!lvl.isClientSide())
        {
            BlockEntity be = lvl.getBlockEntity(pos);
            if(be instanceof ItemScrubberBlockEntity)
            {
                ItemScrubberBlockEntity entity = (ItemScrubberBlockEntity)be;
                NetworkHooks.openGui(((ServerPlayer)player), entity, pos);

                ModMessages.sendToPlayer(new EnergySyncS2CPacket(entity.getEnergyStorage().getEnergyStored(), entity.getBlockPos()), (ServerPlayer)player);

            }else{
                throw new IllegalStateException("Our container is missing!");
            }
        }

        return InteractionResult.sidedSuccess(lvl.isClientSide);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemScrubberBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModEntities.ITEM_SCRUBBER.get(), ItemScrubberBlockEntity::tick);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
