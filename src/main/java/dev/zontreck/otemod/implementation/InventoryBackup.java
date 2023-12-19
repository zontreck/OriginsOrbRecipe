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
    ServerPlayer player;
    File my_file;

    ListTag list;
    public static final Path FILE_TREE_PATH = of("gamemode_inventories");

    public InventoryBackup(ServerPlayer player, GameType mode)
    {
        this.player = player;
        var temp = FILE_TREE_PATH.resolve(player.getStringUUID());
        if(!temp.toFile().exists()) temp.toFile().mkdir();

        my_file = temp.resolve(mode.getName() + ".nbt").toFile();
    }

    public void restore()
    {
        try {
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
            list.clear();
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
