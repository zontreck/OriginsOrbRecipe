package dev.zontreck.otemod.implementation.events;

import dev.zontreck.otemod.implementation.homes.Home;
import net.minecraftforge.eventbus.api.Event;

public class HomeCreatedEvent extends Event
{
    public Home home;
    public HomeCreatedEvent(Home home)
    {
        this.home=home;
    }
}
