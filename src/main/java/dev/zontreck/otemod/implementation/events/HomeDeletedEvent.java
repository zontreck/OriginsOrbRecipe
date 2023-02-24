package dev.zontreck.otemod.implementation.events;

import dev.zontreck.otemod.implementation.homes.Home;

public class HomeDeletedEvent extends HomeCreatedEvent
{

    public HomeDeletedEvent(Home home) {
        super(home);
    }
    
}
