package dev.zontreck.otemod.items.tags;

import net.minecraft.nbt.CompoundTag;

public class ItemStatTag {
    public static final String STATS_TAG = "stat";
    public ItemStatType type;
    public int tagPosition;
    public int value;

    public ItemStatTag(ItemStatType t, int tagPos, int tag)
    {
        type = t;
        value = tag;
        tagPosition = tagPos;
    }

    public void increment(){
        value++;
    }
    public void decrement()
    {
        value--;
    }

    public void setValue(int value)
    {
        this.value=value;
    }

    public void save(CompoundTag tag)
    {
        tag.putInt(STATS_TAG+"_"+type.name().toLowerCase(), value);
    }
}
