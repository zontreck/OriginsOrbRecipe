package dev.zontreck.otemod.database;

import dev.zontreck.otemod.containers.Vector2;
import dev.zontreck.otemod.containers.Vector3;
import dev.zontreck.otemod.containers.WorldPosition;
import dev.zontreck.otemod.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;

/**
 *  This defines the data structure, and methods for deserializing and serializing teleport destinations, for easier storage in the database
 **/
public class TeleportDestination extends WorldPosition
{
    public Vector2 Rotation;

    public TeleportDestination(CompoundTag tag) throws InvalidDeserialization
    {
        super(tag, true);
        Rotation = new Vector2(tag.getString("Rotation"));
    }
    public TeleportDestination(Vector3 pos, Vector2 rot, String dim)
    {
        super(pos, dim);
        Rotation = rot;
    }

    public TeleportDestination(Vector3 pos, Vector2 rot, ServerLevel dim)
    {
        super(pos,dim);
        Rotation=rot;
    }

    @Override
    public String toString()
    {

        return NbtUtils.structureToSnbt(serialize());

    }

    public CompoundTag serialize(){

        CompoundTag tag = super.serializePretty();
        tag.putString("Rotation", Rotation.toString());

        return tag;
    }
    
}
