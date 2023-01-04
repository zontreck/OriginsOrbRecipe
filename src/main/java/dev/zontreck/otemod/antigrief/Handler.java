package dev.zontreck.otemod.antigrief;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid =  OTEMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        Collection<StoredBlock> toHeal = new ArrayList<StoredBlock>(affectedBlocks.size());

        Block tnt = Blocks.TNT;

        for(final StoredBlock data : affectedBlocks)
        {
            // Check an exclusions list
            if(!OTEServerConfig.EXCLUDE_DIMENSIONS.get().contains(data.getWorldPosition().Dimension))
                if(!data.getState().is(tnt))
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

            data.getWorldPosition().getActualDimension().destroyBlock(data.getPos(), false);
        }

        // Add to the healing queue
        List<StoredBlock> mainList = new ArrayList<>();
        mainList.addAll(toHeal);
        mainList = HealerQueue.removeSame(mainList);

        HealerWorker work = new HealerWorker(mainList);
        HealerQueue.ManagerInstance.registerWorker(work);
        Thread tx = new Thread(work);
        tx.start();

        HealerQueue.ToHeal.addAll(mainList);


        HealerQueue.Pass=0;

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
