package dev.zontreck.otemod.implementation.homes;

import java.util.UUID;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class Home {
    public UUID owner;
    public String homeName;
    public TeleportDestination destination;


    public Home(ServerPlayer player, String name, TeleportDestination dest)
    {
        owner=player.getUUID();
        homeName=name;
        destination=dest;
    }

    public Home(CompoundTag tag)
    {
        owner = tag.getUUID("owner");
        homeName = tag.getString("name");
        try {
            destination = new TeleportDestination(tag.getCompound("dest"));
        } catch (InvalidDeserialization e) {
            e.printStackTrace();
        }
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", owner);
        tag.putString("name", homeName);
        tag.put("dest", destination.serialize());

        return tag;
    }
}
