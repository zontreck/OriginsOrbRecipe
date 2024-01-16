package dev.zontreck.thresholds.implementation.vault;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.items.ItemStackHandler;

import java.io.File;
import java.io.IOException;

public class Starter {
    public File file_location;
    public final boolean isNew;
    private long LastChanged;
    private CompoundTag tag;

    protected Starter(File loc)
    {
        file_location=loc;
        isNew=!file_location.exists();
        CompoundTag tag;

        if(!isNew){
            try {
                tag = NbtIo.read(loc);
                this.tag = tag.getCompound("inventory");
                this.LastChanged = tag.getLong("changed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This will return the contents of the NBT Vault
     * @return
     */
    public CompoundTag getContents(){
        return tag;
    }

    /**
     * This sets the internal compound tag of the vault provider, but also saves it to the file immediately.
     * @param newTag
     */
    public void setContents(CompoundTag newTag)
    {
        tag=newTag;
        commit();
    }

    public void setLastChanged(long change)
    {
        LastChanged = change;
    }

    public long getLastChanged() {
        return LastChanged;
    }

    private void commit()
    {
        CompoundTag newTag = new CompoundTag();
        newTag.put("inventory", tag);
        newTag.put("changed", LongTag.valueOf(LastChanged));

        try {
            NbtIo.write(newTag, file_location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This is called to dispose of the vault and the vault file
     */
    public void delete()
    {
        if(file_location.exists())
            file_location.delete();
    }

    public ItemStackHandler getItems()
    {
        ItemStackHandler H = new ItemStackHandler(32);
        H.deserializeNBT(getContents());

        return H;
    }
}
