package dev.zontreck.otemod.database;

import dev.zontreck.otemod.containers.Vector2;
import dev.zontreck.otemod.containers.Vector3;
import dev.zontreck.otemod.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

/**
 *  This defines the data structure, and methods for deserializing and serializing teleport destinations, for easier storage in the database
 **/
public class TeleportDestination {
    public Vector3 Position;
    public Vector2 Rotation;
    public String Dimension;

    public TeleportDestination(CompoundTag tag) throws InvalidDeserialization
    {
        Position = new Vector3(tag.getString("Position"));
        Rotation = new Vector2(tag.getString("Rotation"));
        Dimension = tag.getString("Dimension");
    }
    public TeleportDestination(Vector3 pos, Vector2 rot, String dim)
    {
        Position = pos;
        Rotation = rot;
        Dimension = dim;
    }

    @Override
    public String toString()
    {
        CompoundTag tag = new CompoundTag();
        tag.putString("Position", Position.toString());
        tag.putString("Rotation", Rotation.toString());
        tag.putString("Dimension", Dimension);

        return NbtUtils.structureToSnbt(tag);

    }
    
}
