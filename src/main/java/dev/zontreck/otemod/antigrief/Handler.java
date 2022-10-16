package dev.zontreck.otemod.antigrief;

import java.util.Collection;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Handler 
{
    private static final String EXPLOSION_HEAL_TAG = "OTEEH";
    

    @OnlyIn(Dist.DEDICATED_SERVER)
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
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onDetonation(ExplosionEvent.Detonate event)
    {
        ServerLevel level = (ServerLevel)event.getLevel();

        Entity exploder = event.getExplosion().getExploder();

        if(exploder==null)return ; // TODO: Make this check config

        final Collection<BlockState> affectedBlocks = buildBlocks(level, event.getAffectedBlocks());
    }

    private Collection<BlockState> buildBlocks(ServerLevel level, Collection<BlockPos> positions)
    {
        for(final BlockPos pos : positions)
        {
            final BlockState state = level.getBlockState(pos);

            
        }
    }
}
