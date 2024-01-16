package dev.zontreck.otemod.implementation.events;

import dev.zontreck.libzontreck.profiles.Profile;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.ItemStackHandler;

public class VaultModifiedEvent extends VaultCreatedEvent
{
    public ItemStackHandler inventory;
    public ItemStackHandler oldInventory;

    public VaultModifiedEvent(int num, Profile user, int vaultsInUse, ItemStackHandler inv, ItemStackHandler old) {
        super(num, user, vaultsInUse);
        inventory=inv;
        oldInventory = old;
    }
    
}
