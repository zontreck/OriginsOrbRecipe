package dev.zontreck.otemod.implementation.warps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.zontreck.otemod.database.FileTreeDatastore;
import net.minecraft.nbt.NbtIo;

public class WarpsProvider extends FileTreeDatastore
{
    public static final Path BASE = of("warps");
    public static final Path WARPS_DATA = BASE.resolve("warps.nbt");

    public static final Warps WARPS_INSTANCE;
    static{
        if(!BASE.toFile().exists()){
            try {
                Files.createDirectory(BASE);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        WARPS_INSTANCE = getOrCreate();
    }

    /**
     * Creates a new warps instance, or returns a fully deserialized instance
     * @return
     */
    private static Warps getOrCreate()
    {
        Warps instance = null;
        if(WARPS_DATA.toFile().exists())
        {
            try{
                instance= Warps.deserialize(NbtIo.read(WARPS_DATA.toFile()));

            }catch(Exception e){
                instance=Warps.getNew();
            }
        }else {
            instance=Warps.getNew();
        }

        return instance;
    }

    public static void updateFile()
    {
        try {
            NbtIo.write(WARPS_INSTANCE.serialize(), WARPS_DATA.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
