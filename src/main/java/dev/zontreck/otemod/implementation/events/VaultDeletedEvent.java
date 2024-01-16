package dev.zontreck.otemod.implementation.events;

import dev.zontreck.libzontreck.profiles.Profile;

public class VaultDeletedEvent extends VaultCreatedEvent{

    public VaultDeletedEvent(int num, Profile user, int vaultsInUse) {
        super(num, user, vaultsInUse);
    }
    
}
