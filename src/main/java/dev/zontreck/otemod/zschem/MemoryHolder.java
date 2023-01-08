package dev.zontreck.otemod.zschem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.world.entity.player.Player;

public class MemoryHolder {
    // This class holds the temporary memory related to zschem data
    // We also store position information here.

    public class Container{
        // Contains the position and block lists!
        public Vector3 Pos1;
        public Vector3 Pos2;
        public List<StoredBlock> blocks;
    }

    private static Map<UUID, Container> playerContainers = new HashMap<UUID,Container>();
    private static final Lock lck = new ReentrantLock();

    public static boolean hasPlayerCached(Player P)
    {
        lck.lock();
        try{

            UUID id = P.getUUID();
            return playerContainers.containsKey(id);
        }finally{
            lck.unlock();
        }
    }

    public static void setPos1(Player p, Vector3 pos)
    {
        if(hasPlayerCached(p))
        {
            Container c = playerContainers.get(p.getUUID());
            c.Pos1 = pos;

            playerContainers.put(p.getUUID(), c);
        }
    }

    public static void clear(Player p)
    {
        if(hasPlayerCached(p))
        {
            playerContainers.remove(p.getUUID());
        }
    }

    public static void setPos2(Player p, Vector3 pos)
    {
        if(hasPlayerCached(p))
        {
            Container c = playerContainers.get(p.getUUID());
            c.Pos2 = pos;

            playerContainers.put(p.getUUID(), c);
        }
    }

    public static void setBlocks(Player p, List<StoredBlock> blk)
    {
        if(hasPlayerCached(p))
        {
            Container c = playerContainers.get(p.getUUID());
            c.blocks=blk;

            playerContainers.put(p.getUUID(), c);
        }
    }

    public static List<StoredBlock> getBlocks(Player p)
    {
        if(hasPlayerCached(p))
        {
            return playerContainers.get(p.getUUID()).blocks;
        }else return new ArrayList<StoredBlock>();
    }

    public static Container get(Player p)
    {
        if(hasPlayerCached(p)){
            return playerContainers.get(p.getUUID());
        }else return null;
    }

}
