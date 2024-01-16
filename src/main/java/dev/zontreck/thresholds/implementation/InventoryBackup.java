package dev.zontreck.thresholds.implementation;

import dev.zontreck.thresholds.database.OTEDatastore;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class InventoryBackup extends OTEDatastore
{
    ServerPlayer player;
    File my_file;

    ListTag list;
    public static final Path FILE_TREE_PATH = of("gamemode_inventories");

    public InventoryBackup(ServerPlayer player, GameType mode)
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


}
