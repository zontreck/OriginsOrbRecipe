package dev.zontreck.otemod.configs.snbt.sections;

import dev.zontreck.ariaslib.util.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cooldowns
{
    public static final String TAG_NAME = "cooldowns";
    public static final String TAG_LIST = "commands";

    public Map<String, Integer> commands = Maps.of();


    public CompoundTag save()
    {
        CompoundTag tag = new CompoundTag();
        ListTag lst = new ListTag();
        for(Map.Entry<String, Integer> entry : commands.entrySet())
        {
            Command cmd = new Command(entry.getKey(), entry.getValue());
            lst.add(cmd.save());
        }

        tag.put(TAG_LIST, lst);

        return tag;
    }


    public static Cooldowns load(CompoundTag tag)
    {
        Cooldowns cd = new Cooldowns();
        ListTag lst = tag.getList(TAG_LIST, ListTag.TAG_COMPOUND);
        for(Tag entry : lst)
        {
            CompoundTag compoundTag = (CompoundTag) entry;
            Command cmd = Command.load(compoundTag);
            cd.commands.put(cmd.command, cmd.seconds);
        }


        return cd;
    }


    public static class Command
    {
        public static final String TAG_COMMAND = "command";
        public static final String TAG_SECONDS = "seconds";


        public String command;
        public int seconds;

        private Command()
        {

        }

        public Command(String cmd, int sec)
        {
            command=cmd;
            seconds=sec;
        }

        public CompoundTag save()
        {
            CompoundTag tag = new CompoundTag();
            tag.putString(TAG_COMMAND, command);
            tag.putInt(TAG_SECONDS, seconds);

            return tag;
        }

        public static Command load(CompoundTag tag)
        {
            Command cmd = new Command();

            if(tag.contains(TAG_COMMAND))
                cmd.command = tag.getString(TAG_COMMAND);

            if(tag.contains(TAG_SECONDS))
                cmd.seconds = tag.getInt(TAG_SECONDS);


            return cmd;
        }
    }
}
