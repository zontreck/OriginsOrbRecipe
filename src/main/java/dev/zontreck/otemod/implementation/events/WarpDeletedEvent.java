package dev.zontreck.otemod.implementation.events;

import dev.zontreck.otemod.implementation.warps.Warp;

public class WarpDeletedEvent extends WarpCreatedEvent
{

    public WarpDeletedEvent(Warp w) {
        super(w);
    }
    
}
