package dev.zontreck.otemod.implementation.events;

import dev.zontreck.otemod.implementation.warps.Warp;
import net.minecraftforge.eventbus.api.Event;

public class WarpCreatedEvent extends Event
{
    public Warp warp;
    public WarpCreatedEvent(Warp w)
    {
        warp=w;
    }
}
