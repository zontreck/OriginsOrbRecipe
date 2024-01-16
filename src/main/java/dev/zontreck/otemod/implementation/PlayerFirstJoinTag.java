package dev.zontreck.otemod.implementation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;

import java.time.Instant;

public class PlayerFirstJoinTag
{
    private PlayerFirstJoinTag(long time)
    {
        LastGiven = time;
    }

    public long LastGiven;
    public static final String ID = "firstjoin";
    public static final String LAST_GIVEN_TAG = "last";

    public static PlayerFirstJoinTag now()
    {
        return new PlayerFirstJoinTag(Instant.now().getEpochSecond());
    }

    public void save(CompoundTag parent)
    {
        CompoundTag tag = new CompoundTag();
        tag.put(LAST_GIVEN_TAG, LongTag.valueOf(LastGiven));

        parent.put(ID, tag);
    }

    public static PlayerFirstJoinTag load(CompoundTag tag)
    {
        if(!tag.contains(ID)) return null;
        CompoundTag me = tag.getCompound(ID);
        return new PlayerFirstJoinTag(me.getLong(LAST_GIVEN_TAG));
    }
}
