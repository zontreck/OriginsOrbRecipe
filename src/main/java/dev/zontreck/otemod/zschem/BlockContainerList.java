package dev.zontreck.otemod.zschem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

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
            for (StoredBlock storedBlock : containers) {
                storedBlock.tick();
                if(storedBlock.isExpired()){
                    HealRunner.scheduleHeal(storedBlock);
                    containers.remove(storedBlock);
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
