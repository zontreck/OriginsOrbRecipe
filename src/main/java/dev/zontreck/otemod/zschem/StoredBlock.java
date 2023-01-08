package dev.zontreck.otemod.zschem;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StoredBlock
{
    

    public CompoundTag blockData;

    private WorldPosition position;
    private BlockState state;
    private CompoundTag blockEntity;
    private int tick;
    private int tries;


    public void setPosition(Vector3 pos)
    {
        position.Position=pos;
    }

    public void updateWorld(ServerLevel lv)
    {
        position = new WorldPosition(position.Position, lv);
    }


    public void tick(){
        this.tick--;
    }

    public void setTick(int tick){
        this.tick=tick;
    }


    public boolean isExpired() {
        return tick <= 0;
    }

    public void replaceBlockState(BlockState state)
    {
        this.state=state;
    }


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
        return position.Position.asBlockPos();
    }

    public final WorldPosition getWorldPosition()
    {
        return position;
    }

    public final BlockState getState()
    {
        return state;
    }

    public final long getChunkX()
    {
        Vector3 pos = position.Position;
        return pos.rounded().x >> 4;
    }

    public final long getChunkZ()
    {
        Vector3 pos = position.Position;
        return pos.rounded().z >> 4;
    }

    public void setBlockEntity(BlockEntity entity)
    {
        CompoundTag tag = entity.saveWithoutMetadata();
        this.blockEntity=tag;
    }

    public final CompoundTag getBlockEntity(){
        return blockEntity;
    }

    public static boolean hasBlockEntity(final CompoundTag tag){
        return tag.contains("entity", Tag.TAG_COMPOUND);
    }


    public CompoundTag serialize()
    {
        final CompoundTag tag = new CompoundTag();

        tag.put("pos", position.serialize());
        tag.put("state", NbtUtils.writeBlockState(state));
        tag.putInt("tick", tick);
        tag.putInt("tries", tries);

        if(blockEntity != null) tag.put("entity", blockEntity);

        return tag;
    }
    public int getTries(){
        return tries;
    }
    public int getTickValue()
    {
        return tick;
    }

    public void tickTries(){
        tries++;
    }

    public void deserialize(final CompoundTag tag)
    {
        try {
            position = new WorldPosition(tag.getCompound("pos"), false);
        } catch (InvalidDeserialization e) {
            e.printStackTrace();
        }
        

        state = NbtUtils.readBlockState(tag.getCompound("state"));

        final CompoundTag tmp = tag.getCompound("entity");
        blockEntity = tmp.isEmpty() ? null : tmp;

        tick = tag.getInt("tick");
        tries=tag.getInt("tries");
    }
    


}
