package dev.zontreck.otemod.implementation.warps;

import java.util.UUID;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.nbt.CompoundTag;

public class Warp {
    public UUID owner;
    public String WarpName;
    public boolean RTP;
    public boolean isPublic;
    public TeleportDestination destination;

    public Warp(UUID owner, String name, boolean rtp, boolean publicWarp, TeleportDestination destination)
    {
        this.owner=owner;
        WarpName=name;
        RTP=rtp;
        isPublic=publicWarp;
        this.destination=destination;
    }

    public static Warp deserialize(CompoundTag tag) throws InvalidDeserialization
    {
        return new Warp(tag.getUUID("owner"), tag.getString("name"), tag.getBoolean("rtp"), tag.getBoolean("public"), new TeleportDestination(tag.getCompound("destination")));
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", owner);
        tag.putString("name", WarpName);
        tag.putBoolean("rtp", RTP);
        tag.putBoolean("public", isPublic);
        tag.put("destination", destination.serialize());

        return tag;
    }
}
