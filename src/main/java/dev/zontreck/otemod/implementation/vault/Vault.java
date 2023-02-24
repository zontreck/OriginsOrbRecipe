package dev.zontreck.otemod.implementation.vault;

import java.io.File;
import java.io.IOException;

import dev.zontreck.otemod.implementation.profiles.Profile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

public class Vault {
    public int vaultNum;
    public File file_location;
    public Profile user;
    public final boolean isNew;
    private CompoundTag tag;

    protected Vault(int num, File loc, Profile owner)
    {
        user=owner;
        file_location=loc;
        vaultNum=num;
        isNew=!file_location.exists();

        if(!isNew){
            try {
                tag = NbtIo.read(loc);
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
}
