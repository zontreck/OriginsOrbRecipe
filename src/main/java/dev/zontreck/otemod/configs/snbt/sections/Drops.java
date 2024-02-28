package dev.zontreck.otemod.configs.snbt.sections;

import net.minecraft.nbt.CompoundTag;

public class Drops
{
    public static final String TAG_NAME = "drops";
    public static final String TAG_PLAYER_HEAD_DROPS = "enablePlayerHeadDrops";
    public static final String TAG_SPAWN_EGG_CHANCE = "mobEggingChance";
    public static final String TAG_PLAYER_HEAD_CHANCE = "playerHeadChance";

    public boolean enablePlayerHeadChance = true;
    public float mobEggingChance = 0.25f;
    public float playerHeadChance=0.5f;


    public CompoundTag save()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_PLAYER_HEAD_DROPS, enablePlayerHeadChance);
        tag.putFloat(TAG_SPAWN_EGG_CHANCE, mobEggingChance);
        tag.putFloat(TAG_PLAYER_HEAD_CHANCE, playerHeadChance);

        return tag;
    }

    public static Drops load(CompoundTag tag)
    {
        Drops drops = new Drops();

        if(tag.contains(TAG_PLAYER_HEAD_DROPS))
            drops.enablePlayerHeadChance = tag.getBoolean(TAG_PLAYER_HEAD_DROPS);

        if(tag.contains(TAG_SPAWN_EGG_CHANCE))
            drops.mobEggingChance = tag.getFloat(TAG_SPAWN_EGG_CHANCE);

        if(tag.contains(TAG_PLAYER_HEAD_CHANCE))
            drops.playerHeadChance = tag.getFloat(TAG_PLAYER_HEAD_CHANCE);

        return drops;
    }
}
