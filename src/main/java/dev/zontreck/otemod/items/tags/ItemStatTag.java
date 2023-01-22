package dev.zontreck.otemod.items.tags;

import net.minecraft.nbt.CompoundTag;

public class ItemStatTag {
    public static final String STATS_TAG = "stat";
    public ItemStatType type;
    public int value;

    public ItemStatTag(ItemStatType t, int tag)
    {
        type = t;
        value = tag;
    }

    public void increment(){
        value++;
    }
    public void decrement()
    {
        value--;
    }

    public void save(CompoundTag tag)
    {
        tag.putInt(STATS_TAG+"_"+type.name().toLowerCase(), value);
    }
}
