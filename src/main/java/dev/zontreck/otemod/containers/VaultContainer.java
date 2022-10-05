package dev.zontreck.otemod.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class VaultContainer implements Container
{
    public int VaultID;
    public UUID VaultOwner;
    public ItemStack[] Contents=new ItemStack[64];

    public VaultContainer(int num, UUID owner)
    {
        VaultID = num;
        VaultOwner = owner;
        Contents = new ItemStack[64];
    }

    @Override
    public void clearContent() {
        for(int i=0; i< Contents.length; i++)
        {
            Contents[i] = null;
        }
    }

    @Override
    public int getContainerSize() {
        return 64; // Double chest
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack is : Contents)
        {
            if(is == null)continue;
            else return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return Contents[p_18941_];
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        // TODO debug this!
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        Contents[p_18944_] = p_18945_;
        setChanged(); // mark the contents to be uploaded to the server
    }

    @Override
    public void setChanged() {
        // Enqueue upload to the database
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true; // The inventory is always valid for this container type. There is no circumstance where the container could be destroyed.
    }

    public void downloadContentsToChest(Vec3 pos)
    {
        // Player is standing on a chest and wants us to download the vault into the chest.
        // TODO
    }
    
}
