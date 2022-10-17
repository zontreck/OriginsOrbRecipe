package dev.zontreck.otemod.antigrief;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Handler 
{
    private static final String EXPLOSION_HEAL_TAG = "OTEEH";
    

    /*@OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onChunkLoad(final ChunkDataEvent.Load event)
    {
        final CompoundTag EHTag = event.getData().getCompound(EXPLOSION_HEAL_TAG);
        if(!EHTag.isEmpty())
        {
            final CompoundTag healer = EHTag.getCompound("healer");
            if(!healer.isEmpty()){
                // This would re-queue the healer
            }
        }
    }*/

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onDetonation(ExplosionEvent.Detonate event)
    {
        ServerLevel level = (ServerLevel)event.getLevel();

        Entity exploder = event.getExplosion().getExploder();

        if(exploder==null)return ; // TODO: Make this check config

        final Collection<StoredBlock> affectedBlocks = buildBlocks(level, event.getAffectedBlocks());
        final Collection<StoredBlock> toHeal = new ArrayList<StoredBlock>(affectedBlocks.size());

        for(final StoredBlock data : affectedBlocks)
        {
            // Check an exclusions list
            if(OTEServerConfig.EXCLUDE_DIMENSIONS.get().contains(data.getWorldPosition().Dimension))
                toHeal.add(data);
        }

        // Process Block Entities

        for(final StoredBlock sb : toHeal)
        {
            if(sb.getState().hasBlockEntity())
            {
                BlockEntity be = level.getBlockEntity(sb.getPos());
                if(be != null){
                    sb.setBlockEntity(be);
                }
            }
        }

        // Remove the existing blocks from the world to prevent item duplication
        // Begin
        for(StoredBlock data : toHeal)
        {
            if(data.getBlockEntity()!=null)
                data.getWorldPosition().getActualDimension().removeBlockEntity(data.getPos());

            data.getWorldPosition().getActualDimension().removeBlock(data.getPos(), false); // Is false to not drop item?
        }

        // Add to the healing queue
        HealerQueue.ToHeal.addAll(toHeal);
        HealerQueue.Shuffle();

    }

    private Collection<StoredBlock> buildBlocks(ServerLevel level, Collection<BlockPos> positions)
    {
        Collection<StoredBlock> healables = new LinkedList<StoredBlock>();
        for(final BlockPos pos : positions)
        {
            final BlockState state = level.getBlockState(pos);
            StoredBlock sb = new StoredBlock(pos, state, level);

            if(state !=null)
                healables.add(sb);
            
        }

        return healables;
    }
}
