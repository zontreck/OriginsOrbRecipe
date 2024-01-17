package dev.zontreck.otemod.zschem;

import java.util.HashMap;
import java.util.Map;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    
    @SubscribeEvent
    public void onDetonate(ExplosionEvent ev)
    {
        if(ev.getWorld().isClientSide)return;

        //Entity explodes = ev.getExplosion().getSourceMob();
        // Register blocks to be healed
        WorldPosition wpos = new WorldPosition(new Vector3(ev.getExplosion().getPosition()), (ServerLevel) ev.getWorld());

        if(OTEServerConfig.EXCLUDE_DIMS.get().contains(wpos.Dimension)){
            // Dimension is on the exclusion list. Do not process.
            return;
        }
        WorldProp wp = WorldProp.acquire((ServerLevel)ev.getWorld());

        if(wp!=null){
            wp.onDetonate(ev);
        }
    }

    
    private Map<ServerLevel, WorldProp> healers = new HashMap<ServerLevel, WorldProp>();

    public Map<ServerLevel, WorldProp> getHealers()
    {
        return healers;
    }

    @SubscribeEvent
    public void onLoadLevel(WorldEvent.Load ev)
    {
        if(!ev.getWorld().isClientSide() && ev.getWorld() instanceof ServerLevel)
        {
            healers.put((ServerLevel)ev.getWorld(), WorldProp.acquire((ServerLevel)ev.getWorld()));
            
        }
    }

    @SubscribeEvent
    public void onUnload(WorldEvent.Unload ev)
    {
        if(!ev.getWorld().isClientSide())
        {
            healers.remove(ev.getWorld());
        }
    }

    @SubscribeEvent
    public void onShutdown(ServerStoppingEvent ev)
    {
        WorldProp.SaveAll();
    }

    @SubscribeEvent
    public void onSaving(WorldEvent.Save ev)
    {
        WorldProp.SaveAll();
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.WorldTickEvent ev)
    {
        if(!ev.world.isClientSide){
            WorldProp wp = WorldProp.acquire((ServerLevel)ev.world);
            if(wp!=null){
                wp.onTick();
            }

            MemoryHolder.tick();
        }
    }
}
