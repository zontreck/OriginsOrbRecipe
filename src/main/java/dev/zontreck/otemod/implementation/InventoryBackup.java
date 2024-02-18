package dev.zontreck.otemod.implementation;

import dev.zontreck.otemod.database.OTEDatastore;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.items.ItemStackHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InventoryBackup extends OTEDatastore
{
    /**
     * This is to introduce custom, virtual gamemodes. To allow for custom dimensions to have their own inventories.
     */
    public enum GameMode {
        Adventure("adventure"),
        Spectator("spectator"),
        Creative("creative"),
        Survival("survival"),
        Thresholds("thresholds"),
        Builder("builder");

        private final String name;
        private GameMode(String name)
        {
            this.name=name;
        }

        public String getName() {
            return name;
        }

        public static GameMode fromGameType(GameType type)
        {
            switch(type)
            {
                case ADVENTURE:
                {
                    return Adventure;
                }
                case SPECTATOR:
                {
                    return Spectator;
                }
                case CREATIVE:
                {
                    return Creative;
                }
                default:
                {
                    return Survival;
                }
            }
        }

        public static GameMode fromNBT(Tag tag)
        {
            String str = tag.getAsString();
            for(GameMode mode : values())
            {
                if(mode.getName().equalsIgnoreCase(str))
                {
                    return mode;
                }
            }

            return Survival;
        }

        public Tag saveToNBT()
        {
            return StringTag.valueOf(name);
        }

        public GameType toMinecraft()
        {
            switch(this)
            {

                case Survival:
                {
                    return GameType.SURVIVAL;
                }

                case Creative:
                {
                    return GameType.CREATIVE;
                }

                case Adventure:
                {
                    return GameType.ADVENTURE;
                }

                case Spectator:
                {
                    return GameType.SPECTATOR;
                }



                default:
                {
                    return GameType.DEFAULT_MODE;
                }
            }
        }
    }
    ServerPlayer player;
    File my_file;

    ListTag list;
    GameMode formerGameMode;

    public static final Path FILE_TREE_PATH = of("gamemode_inventories");

    public InventoryBackup(ServerPlayer player, GameMode mode)
    {
        this.player = player;
        if(!FILE_TREE_PATH.toFile().exists())
        {
            FILE_TREE_PATH.toFile().mkdir();
        }
        var temp = FILE_TREE_PATH.resolve(player.getStringUUID());
        if(!temp.toFile().exists()) temp.toFile().mkdir();

        my_file = temp.resolve(mode.getName() + ".nbt").toFile();
    }

    public void restore()
    {
        try {
            if(!my_file.exists())
            {
                list = new ListTag();
                return;
            }
            CompoundTag tag = NbtIo.read(my_file);
            list = tag.getList("inventory", Tag.TAG_COMPOUND);

            if(tag.contains("gamemode"))
            {
                formerGameMode = GameMode.fromNBT(tag.get("gamemode"));
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try{
            CompoundTag tag = new CompoundTag();
            list = new ListTag();
            list = player.getInventory().save(list);
            tag.put("inventory", list);
            if(formerGameMode != null)
                tag.put("gamemode", formerGameMode.saveToNBT());

            NbtIo.write(tag, my_file);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void apply()
    {
        try {
            player.getInventory().load(list);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setFormerGameMode(GameMode mode)
    {
        formerGameMode=mode;
    }

    public GameMode getFormerGameMode() {
        return formerGameMode;
    }
}
