package dev.zontreck.otemod.zschem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;

public class BlockContainerList {
    private static final BlockContainerList INSTANCE =new BlockContainerList();
    private final Lock lock;
    private final List<StoredBlock> containers;

    public BlockContainerList()
    {
        this.lock = new ReentrantLock();
        this.containers = new ArrayList<>();
    }

    public static BlockContainerList getInstance()
    {
        return INSTANCE;
    }

    public int getNewLongestTick()
    {
        //Random rng = new Random();
        int newLonger = OTEServerConfig.TIME_BETWEEN_BLOCKS.get();
        int cur = 0;

        for (StoredBlock storedBlock : containers) {
            if(cur < storedBlock.getTickValue()){
                cur = storedBlock.getTickValue();
            }
        }
        if(cur == 0)cur = OTEServerConfig.HEALER_TIMER.get();

        return cur + newLonger;
    }

    public void add(StoredBlock item)
    {
        lock.lock();
        try{
            for(StoredBlock sb : containers)
            {
                if(sb.getWorldPosition().same(item.getWorldPosition()))
                {
                    return;
                }
            }

            containers.add(item);
        }finally{
            lock.unlock();
        }
    }

    public void tick()
    {
        lock.lock();
        try{
            Iterator<StoredBlock> isb = containers.iterator();
            while(isb.hasNext())
            {
                StoredBlock storedBlock = isb.next();
                storedBlock.tick();
                if(storedBlock.isExpired()){
                    WorldPosition wp = storedBlock.getWorldPosition();
                    BlockState bs = wp.getActualDimension().getBlockState(wp.Position.asBlockPos());
                    if(bs.is(storedBlock.getState().getBlock()) || storedBlock.getTries() >= OTEServerConfig.MAX_TRIES_HEAL.get())
                    {

                        HealRunner.scheduleHeal(storedBlock);
                        isb.remove();
                    }else {
                        HealRunner.scheduleHeal(storedBlock);
                        storedBlock.setTick(getNewLongestTick());
                        storedBlock.tickTries();
                    }
                }
            }
        }finally{
            lock.unlock();
        }
    }

    public CompoundTag save(CompoundTag tag){
        lock.lock();
        try{
            
            ListTag lst = new ListTag();
            for (StoredBlock block : containers) {
                lst.add(block.serialize());
            }
            tag.put("blocks", lst);
            return tag;
        }finally{
            lock.unlock();
        }
    }

    public static BlockContainerList load(CompoundTag tag){
        BlockContainerList lst = new BlockContainerList();
        
        ListTag xlst = tag.getList("blocks", CompoundTag.TAG_BYTE);
        ListIterator<Tag> it = xlst.listIterator();
        while(it.hasNext()){
            Tag tg = it.next();
            CompoundTag blk = (CompoundTag)tg;
            StoredBlock sb = new StoredBlock(blk);

            lst.add(sb);
        }

        return lst;
    }
}
