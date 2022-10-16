package dev.zontreck.otemod.antigrief;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.commands.teleport.TeleportContainer;
import dev.zontreck.otemod.containers.Vector3;
import dev.zontreck.otemod.containers.WorldPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;

public class StoredBlock {
    public CompoundTag blockData;

    private WorldPosition position;
    private BlockState state;

    public StoredBlock(final BlockPos pos, final BlockState toSave, final ServerLevel lvl)
    {
        position = new WorldPosition(new Vector3(pos), lvl);

        this.state=toSave;
    }

    public StoredBlock(final CompoundTag tag)
    {
        this.deserialize(tag);
    }


    public final BlockPos getPos()
    {
        return pos;
    }

    public final BlockState getState()
    {
        return state;
    }

    public final int getChunkX()
    {
        return pos.getX() >> 4;
    }

    public final int getChunkZ()
    {
        return pos.getZ() >> 4;
    }


    public CompoundTag serialize()
    {
        final CompoundTag tag = new CompoundTag();
        

        tag.put("pos", )
    }


}
