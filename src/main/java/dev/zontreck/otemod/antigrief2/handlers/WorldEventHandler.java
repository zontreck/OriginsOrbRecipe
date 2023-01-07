package dev.zontreck.otemod.antigrief2.handlers;

import java.util.HashMap;
import java.util.Map;

import dev.zontreck.otemod.antigrief2.Healer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldEventHandler {
    private Map<ServerLevel, Healer> healers = new HashMap<ServerLevel, Healer>();

    public Map<ServerLevel, Healer> getHealers()
    {
        return healers;
    }

    @SubscribeEvent
    public void onLoadLevel(LevelEvent.Load ev)
    {
        if(!ev.getLevel().isClientSide() && ev.getLevel() instanceof ServerLevel)
        {
            healers.put((ServerLevel)ev.getLevel(), Healer.acquire((ServerLevel)ev.getLevel()));
            
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
}
