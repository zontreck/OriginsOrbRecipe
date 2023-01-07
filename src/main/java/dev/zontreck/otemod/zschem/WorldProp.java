package dev.zontreck.otemod.zschem;

import java.util.function.Supplier;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.event.level.ExplosionEvent;

public class WorldProp extends SavedData implements Supplier<Object>
{
    private Level world;
    private BlockContainerList task;
    static final String KEY = OTEMod.MOD_ID + ":" + WorldProp.class.getSimpleName();

    public WorldProp(){
        task = new BlockContainerList();
    }

    public void onTick()
    {
        task.tick();
    }

    public void onDetonate(ExplosionEvent.Detonate ev)
    {
        Level w = ev.getLevel();
        int maxTicks = 0;

        for(BlockPos p : ev.getAffectedBlocks())
        {
            BlockState bsExplode = w.getBlockState(p);
            if(!isValid(bsExplode))continue;

            if(!bsExplode.isAir()){
                int ticks = OTEServerConfig.HEALER_TIMER.get() + w.random.nextInt();
                if(ticks > maxTicks){
                    maxTicks = ticks;
                }

                
                addHeal(p, bsExplode, world);
            }
        }
        maxTicks ++;
        for(BlockPos p : ev.getAffectedBlocks())
        {
            BlockState bsE = w.getBlockState(p);
            if(!isValid(bsE))continue;
            if(!bsE.isAir()){
                addHeal(p, bsE, world);
            }
        }
    }

    private void addHeal(BlockPos p, BlockState s, Level w)
    {
        task.add(new StoredBlock(p, s, (ServerLevel)w));
        world.removeBlockEntity(p);
        world.setBlock(p, Blocks.AIR.defaultBlockState(), 7);
    }

    private boolean isValid(BlockState bs)
    {
        if(bs.is(BlockTags.DOORS) || bs.is(BlockTags.BEDS) || bs.is(BlockTags.TALL_FLOWERS)){
            return false;
        } else return true;
    }

    public CompoundTag save(CompoundTag tag){
        
        return (CompoundTag) tag.put("task", task.save(tag));

    }

    public void load(CompoundTag tag)
    {
        CompoundTag ct = tag.getCompound("task");
        task = BlockContainerList.load(ct);
    }

    public static WorldProp acquire(ServerLevel w)
    {
        DimensionDataStorage dds = w.getDataStorage();
        WorldProp wp = dds.computeIfAbsent(p->{
            WorldProp swp = new WorldProp();
            swp.load(p);
            return swp;
        }, ()->{
            return new WorldProp();
        }, KEY);
        wp.world = w;
        return wp;

    }

    
    @Override
    public boolean isDirty(){
        return true;
    }

    @Override
    public Object get()
    {
        return this;
    }
}
