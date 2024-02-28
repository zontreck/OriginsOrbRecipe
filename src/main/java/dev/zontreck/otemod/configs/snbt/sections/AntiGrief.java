package dev.zontreck.otemod.configs.snbt.sections;

import dev.zontreck.ariaslib.util.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class AntiGrief
{
    public static final String TAG_NAME = "antigrief";
    public static final String TAG_HEALER_TIMER = "healerTimer";
    public static final String TAG_DEBUG = "debug";
    public static final String TAG_HEALER_TIME_BETWEEN = "timeBetween";
    public static final String TAG_MAX_TRIES = "maxTries";
    public static final String TAG_EXCLUDE_DIMS = "excludeDimensions";




    public int healerTimer = 250;
    public boolean debug = false;
    public int timeBetween = 20;
    public int maxTries = 10;
    public List<String> blacklistedDimensions = Lists.of("minecraft:the_nether", "minecraft:the_end", "otemod:resource", "otemod:threshold");



    public CompoundTag save()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_HEALER_TIMER, healerTimer);
        tag.putBoolean(TAG_DEBUG, debug);
        tag.putInt(TAG_HEALER_TIME_BETWEEN, timeBetween);
        tag.putInt(TAG_MAX_TRIES, maxTries);
        ListTag blacklist = new ListTag();
        for(String str : blacklistedDimensions)
        {
            blacklist.add(StringTag.valueOf(str));
        }
        tag.put(TAG_EXCLUDE_DIMS, blacklist);


        return tag;
    }

    public static AntiGrief load(CompoundTag tag)
    {
        AntiGrief ag = new AntiGrief();

        if(tag.contains(TAG_HEALER_TIMER))
            ag.healerTimer = tag.getInt(TAG_HEALER_TIMER);

        if(tag.contains(TAG_DEBUG))
            ag.debug = tag.getBoolean(TAG_DEBUG);

        if(tag.contains(TAG_HEALER_TIME_BETWEEN))
            ag.timeBetween = tag.getInt(TAG_HEALER_TIME_BETWEEN);

        if(tag.contains(TAG_MAX_TRIES))
            ag.maxTries = tag.getInt(TAG_MAX_TRIES);

        if(tag.contains(TAG_EXCLUDE_DIMS))
        {
            ag.blacklistedDimensions = new ArrayList<>();
            ListTag blacklist = tag.getList(TAG_EXCLUDE_DIMS, ListTag.TAG_STRING);
            for(Tag entry : blacklist)
            {
                StringTag stringTag = (StringTag) entry;
                ag.blacklistedDimensions.add(stringTag.getAsString());
            }
        }



        return ag;
    }
}
