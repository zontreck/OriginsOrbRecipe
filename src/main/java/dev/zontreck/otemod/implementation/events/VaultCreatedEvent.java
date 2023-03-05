package dev.zontreck.otemod.implementation.events;

import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraftforge.eventbus.api.Event;

public class VaultCreatedEvent extends Event
{
    public int vault_num;
    public Profile user;
    public int in_use;
    public int max;
    public int playerMax;
    public int remaining;
    public boolean at_max;
    
    public VaultCreatedEvent(int num, Profile user, int vaultsInUse)
    {
        max=OTEServerConfig.MAX_VAULTS.get();
        vault_num = num;
        in_use = vaultsInUse;
        playerMax=user.available_vaults;
        remaining = playerMax-in_use;
        if(remaining<=0)at_max=true;
        this.user=user;

    }
}
