package dev.zontreck.otemod.configs.snbt;

import dev.zontreck.libzontreck.util.SNbtIo;
import dev.zontreck.otemod.configs.snbt.sections.*;
import dev.zontreck.otemod.database.OTEDatastore;
import net.minecraft.nbt.CompoundTag;

import java.nio.file.Path;

public class ServerConfig
{
    public static final String TAG_VERSION = "revision";
    public static final Path FILE = OTEDatastore.of("server.snbt");

    public static General general = new General();
    public static Drops drops = new Drops();
    public static Cooldowns cooldowns = new Cooldowns();
    public static AntiGrief antigrief = new AntiGrief();
    public static ChatOverrides chatOverrides = new ChatOverrides();



    public static int revision = 0;


    private static final int CURRENT_VERSION = 1;

    public static void load()
    {
        CompoundTag tag = SNbtIo.loadSnbt(FILE);

        deserialize(tag);

    }

    public static void commit()
    {


        SNbtIo.writeSnbt(FILE, serialize());
    }

    private static void migrate(int from, int to)
    {
        if(from == to)
        {
            return;
        } else if(from > to)
        {
            throw new ArithmeticException("Cannot downgrade");
        } else {
            migration(from+1);
            migrate(from+1,to);
        }
    }

    private static void migration(int newVer)
    {
        revision = newVer;

        switch (newVer)
        {
            case 1:
            {
                general = new General();
                drops = new Drops();
                cooldowns = new Cooldowns();
                antigrief = new AntiGrief();
                chatOverrides = new ChatOverrides();
                break;
            }
        }

        commit();
    }

    public static CompoundTag serialize()
    {

        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_VERSION, revision);
        tag.put(General.TAG_NAME, general.save());
        tag.put(Cooldowns.TAG_NAME, cooldowns.save());
        tag.put(AntiGrief.TAG_NAME, antigrief.save());
        tag.put(ChatOverrides.TAG_NAME, chatOverrides.save());


        return tag;
    }

    public static void deserialize(CompoundTag tag)
    {

        revision = tag.getInt(TAG_VERSION);

        migrate(revision, CURRENT_VERSION);

        if(tag.contains(General.TAG_NAME))
            general = General.load(tag.getCompound(General.TAG_NAME));

        if(tag.contains(Drops.TAG_NAME))
            drops = Drops.load(tag.getCompound(Drops.TAG_NAME));

        if(tag.contains(Cooldowns.TAG_NAME))
            cooldowns = Cooldowns.load(tag.getCompound(Cooldowns.TAG_NAME));

        if(tag.contains(AntiGrief.TAG_NAME))
            antigrief = AntiGrief.load(tag.getCompound(AntiGrief.TAG_NAME));

        if(tag.contains(ChatOverrides.TAG_NAME))
            chatOverrides = ChatOverrides.load(tag.getCompound(ChatOverrides.TAG_NAME));

    }

}
