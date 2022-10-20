package dev.zontreck.otemod.antigrief;

import java.time.Instant;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StoredBlock implements Comparable
{

    public static final StoredBlock getBedrock(WorldPosition pos){
        StoredBlock sb = new StoredBlock(pos.Position.asBlockPos(), Blocks.BEDROCK.defaultBlockState(), pos.getActualDimension());

        return sb;
    }
    public static final StoredBlock getAir(WorldPosition pos){
        StoredBlock sb = new StoredBlock(pos.Position.asBlockPos(), Blocks.AIR.defaultBlockState(), pos.getActualDimension());

        return sb;
    }

    public static final StoredBlock getSculk(WorldPosition pos){
        StoredBlock sb = new StoredBlock(pos.Position.asBlockPos(), Blocks.SCULK.defaultBlockState()   ,pos.getActualDimension());
        return sb;
    }

    

    public static final int UNSET = 0;
    public static final int PHASE1 = 1;
    public static final int PHASE2 = 2;
    public static final int PHSAE3 = 4;

    public CompoundTag blockData;

    private WorldPosition position;
    private BlockState state;
    private CompoundTag blockEntity;

    private boolean claim = false;
    private long claimed_at = 0;
    private Thread claimed_by;

    public void setClaimed()
    {
        claimed_at = Instant.now().getEpochSecond();
        claim=true;
    }

    public boolean claimed()
    {
        if(claimed_by == null)
        {
            if(claim)
            {
                if(Instant.now().getEpochSecond() > claimed_at+30)
                {
                    claim=false;
                    claimed_at = 0; // The claim timed out as no thread was set
                    return false;
                }else return true; // Temporary lock on claim
            }else return false; // Not claimed
        }else return true; // Permanent process lock
    }

    public void setClaimedBy(Thread tx){
        claimed_by=tx;
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
        CompoundTag tag = entity.serializeNBT();
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

        if(blockEntity != null) tag.put("entity", blockEntity);

        return tag;
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
    }
    @Override
    public int compareTo(Object o) {
        if(o instanceof StoredBlock)
        {
            StoredBlock sb = (StoredBlock)o;
            if(sb.position.same(position))
            {
                if(sb.state.equals(state))
                {
                    return 0;
                }return -1;
            }else return -1;

        }return -1;
    }


}
