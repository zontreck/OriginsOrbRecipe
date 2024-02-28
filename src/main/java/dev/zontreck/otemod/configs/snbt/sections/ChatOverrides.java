package dev.zontreck.otemod.configs.snbt.sections;

import dev.zontreck.ariaslib.util.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class ChatOverrides
{
    public static final String TAG_NAME = "chatOverride";
    public static final String TAG_PRETTIFIER = "enablePrettifier";
    public static final String TAG_JOIN_LEAVE = "enableJoinLeaveMessages";


    public boolean enablePrettifier = true;
    public boolean enableJoinLeave = true;



    public CompoundTag save()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_PRETTIFIER, enablePrettifier);
        tag.putBoolean(TAG_JOIN_LEAVE, enableJoinLeave);

        return tag;
    }

    public static ChatOverrides load(CompoundTag tag)
    {
        ChatOverrides ag = new ChatOverrides();

        if(tag.contains(TAG_PRETTIFIER))
            ag.enablePrettifier = tag.getBoolean(TAG_PRETTIFIER);

        if(tag.contains(TAG_JOIN_LEAVE))
            ag.enableJoinLeave = tag.getBoolean(TAG_JOIN_LEAVE);


        return ag;
    }
}
