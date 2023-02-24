package dev.zontreck.otemod.implementation.events;

import dev.zontreck.otemod.implementation.profiles.Profile;
import net.minecraftforge.eventbus.api.Event;

public class ProfileCreatedEvent extends Event
{
    public String playerID;
    public ProfileCreatedEvent(Profile newProfile)
    {
        playerID = newProfile.user_id;
    }    
}
