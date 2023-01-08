package dev.zontreck.otemod.zschem;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    
    @SubscribeEvent
    public void onDetonate(ExplosionEvent.Detonate ev)
    {
        if(ev.getLevel().isClientSide)return;

        //Entity explodes = ev.getExplosion().getSourceMob();
        // Register blocks to be healed
        WorldProp wp = WorldProp.acquire((ServerLevel)ev.getLevel());
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
    public void onLoadLevel(LevelEvent.Load ev)
    {
        if(!ev.getLevel().isClientSide() && ev.getLevel() instanceof ServerLevel)
        {
            healers.put((ServerLevel)ev.getLevel(), WorldProp.acquire((ServerLevel)ev.getLevel()));
            
        }
    }

    @SubscribeEvent
    public void onUnload(LevelEvent.Unload ev)
    {
        if(!ev.getLevel().isClientSide())
        {
            healers.remove(ev.getLevel());
        }
    }

    @SubscribeEvent
    public void onShutdown(ServerStoppingEvent ev)
    {
        WorldProp.SaveAll();
    }

    @SubscribeEvent
    public void onSaving(LevelEvent.Save ev)
    {
        WorldProp.SaveAll();
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent ev)
    {
        if(!ev.level.isClientSide){
            WorldProp wp = WorldProp.acquire((ServerLevel)ev.level);
            if(wp!=null){
                wp.onTick();
            }
        }
    }
}
