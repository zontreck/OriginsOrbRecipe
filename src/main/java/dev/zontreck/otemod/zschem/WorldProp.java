package dev.zontreck.otemod.zschem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.event.level.ExplosionEvent;

public class WorldProp implements Supplier<Object>
{
    public static Map<ServerLevel, WorldProp> props = new HashMap<ServerLevel,WorldProp>();
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

            if(!bsExplode.isAir() ){
                int ticks = OTEServerConfig.HEALER_TIMER.get() + maxTicks + OTEServerConfig.TIME_BETWEEN_BLOCKS.get();
                if(ticks<0) ticks = maxTicks + OTEServerConfig.TIME_BETWEEN_BLOCKS.get();
                maxTicks += OTEServerConfig.TIME_BETWEEN_BLOCKS.get();
                
                
                addHeal(p, bsExplode, world, ticks);
            }
        }
    }

    private void addHeal(BlockPos p, BlockState s, Level w, int tick)
    {
        StoredBlock sb = new StoredBlock(p, s, (ServerLevel)w);
        sb.setTick(tick);
        task.add(sb);
        world.removeBlockEntity(p);
        world.setBlock(p, Blocks.AIR.defaultBlockState(), 7);
    }

    private boolean isValid(BlockState bs)
    {
        if(bs.is(BlockTags.DOORS) || bs.is(BlockTags.BEDS) || bs.is(BlockTags.TALL_FLOWERS) || bs.is(Blocks.TNT)){
            return false;
        } else return true;
    }

    
    public static void SaveAll()
    {
        Path destBase = BlockSaver.getPath();
        String ext = BlockSaver.getExtension();

        for (Map.Entry<ServerLevel, WorldProp> entry : props.entrySet()) {
            // Perform saving
            String dimsafe = entry.getKey().dimension().location().getNamespace() +"-"+entry.getKey().dimension().location().getPath();
            String pathTemp = destBase.toString()+"_"+dimsafe+ext;

            Path finalPath = Path.of(pathTemp);
            CompoundTag fnl = new CompoundTag();
            
            fnl = entry.getValue().task.save(fnl);
            try {
                NbtIo.writeCompressed(fnl, finalPath.toFile());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void load(CompoundTag tag)
    {
        task = BlockContainerList.load(tag);
    }

    public static WorldProp acquire(ServerLevel w)
    {
        if(props.containsKey(w))
        {
            return props.get(w);
        }
        Path destBase = BlockSaver.getPath();
        String ext = BlockSaver.getExtension();
        String dimsafe = w.dimension().location().getNamespace() +"-"+w.dimension().location().getPath();
        String pathTemp = destBase.toString()+"_"+dimsafe+ext;

        Path finalPath = Path.of(pathTemp);
        WorldProp nProp = new WorldProp();
        nProp.world=w;

        if(finalPath.toFile().exists())
        {

            try {
                nProp.load(NbtIo.read(finalPath.toFile()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        props.put(w,nProp);
        return nProp;
        /*DimensionDataStorage dds = w.getDataStorage();
        WorldProp wp = dds.computeIfAbsent(p->{
            WorldProp swp = new WorldProp();
            swp.load(p);
            return swp;
        }, ()->{
            return new WorldProp();
        }, KEY);
        wp.world = w;
        return wp;
        */
    }

    
    @Override
    public Object get()
    {
        return this;
    }
}
