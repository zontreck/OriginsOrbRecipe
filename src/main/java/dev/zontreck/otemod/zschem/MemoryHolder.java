package dev.zontreck.otemod.zschem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class MemoryHolder {
    // This class holds the temporary memory related to zschem data
    // We also store position information here.
    private static final MemoryHolder INSTANCE = new MemoryHolder();

    public static MemoryHolder get(){
        return INSTANCE;
    }

    public class Container{
        // Contains the position and block lists!
        public Vector3 Pos1;
        public Vector3 Pos2;
        public List<StoredBlock> blocks;
        public ServerLevel lvl;

        private int tick = 0;

        public void tick(){
            this.tick--;
        }

        public boolean isExpired(){
            if(tick <= 0){
                return true;
            }else return false;
        }

        public Container(){
            tick = 15000;
            Pos1 = OTEMod.ZERO_VECTOR;
            Pos2 = OTEMod.ZERO_VECTOR;
        }

        public void reset(){
            tick=15000;
        }
    }

    private Map<UUID, Container> playerContainers = new HashMap<UUID,Container>();
    private static final Lock lck = new ReentrantLock();

    public static boolean hasPlayerCached(Player P)
    {
        lck.lock();
        try{

            UUID id = P.getUUID();
            return get().playerContainers.containsKey(id);
        }finally{
            lck.unlock();
        }
    }

    public static void tick()
    {
        lck.lock();
        try{
            Iterator<Map.Entry<UUID,Container>> imeuc = get().playerContainers.entrySet().iterator();
            while(imeuc.hasNext())
            {
                Map.Entry<UUID,Container> entry = imeuc.next();
                Container c = entry.getValue();
                c.tick();

                if(c.isExpired()){
                    imeuc.remove();

                    ChatServerOverride.broadcastTo(entry.getKey(), Component.literal(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!ZSchem Session expired")), c.lvl.getServer());
                }
            }
        }finally{
            lck.unlock();
        }
    }

    private static void makeCachedPlayer(Player p){
        if(!hasPlayerCached(p)){

            Container c = INSTANCE.new Container();
            get().playerContainers.put(p.getUUID(), c);
        }
    }

    public static void setPos1(Player p, Vector3 pos)
    {
        makeCachedPlayer(p);
        if(hasPlayerCached(p))
        {
            Container c = get().playerContainers.get(p.getUUID());
            c.Pos1 = pos;
            c.reset();

            get().playerContainers.put(p.getUUID(), c);
        }
    }

    public static void setLevel(Player p, ServerLevel lvl)
    {
        makeCachedPlayer(p);
        if(hasPlayerCached(p))
        {
            Container c = get().playerContainers.get(p.getUUID());
            c.lvl = lvl;
            c.reset();

            get().playerContainers.put(p.getUUID(), c);
        }
    }

    public static void clear(Player p)
    {
        if(hasPlayerCached(p))
        {
            get().playerContainers.remove(p.getUUID());
        }
    }

    public static void setPos2(Player p, Vector3 pos)
    {
        makeCachedPlayer(p);
        if(hasPlayerCached(p))
        {
            Container c = get().playerContainers.get(p.getUUID());
            c.Pos2 = pos;
            c.reset();

            get().playerContainers.put(p.getUUID(), c);
        }
    }

    public static void setBlocks(Player p, List<StoredBlock> blk)
    {
        makeCachedPlayer(p);
        if(hasPlayerCached(p))
        {
            Container c = get().playerContainers.get(p.getUUID());
            c.blocks=blk;
            c.reset();

            get().playerContainers.put(p.getUUID(), c);
        }
    }

    public static List<StoredBlock> getBlocks(Player p)
    {
        if(hasPlayerCached(p))
        {
            return get().playerContainers.get(p.getUUID()).blocks;
        }else return new ArrayList<StoredBlock>();
    }

    public static Container getContainer(Player p)
    {
        if(hasPlayerCached(p)){
            return get().playerContainers.get(p.getUUID());
        }else return null;
    }

}
