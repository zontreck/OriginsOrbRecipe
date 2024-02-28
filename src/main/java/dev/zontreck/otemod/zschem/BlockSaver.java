package dev.zontreck.otemod.zschem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dev.zontreck.libzontreck.util.SNbtIo;
import dev.zontreck.otemod.configs.snbt.ServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class BlockSaver {
    private static final int SAVE_INTERVAL = 1;

    private final ScheduledExecutorService executor;

    public BlockSaver(ScheduledExecutorService service)
    {
        executor=service;
    }

    public void start()
    {
        executor.scheduleAtFixedRate(()->{
            CompoundTag primary = new CompoundTag();
            primary=BlockContainerList.getInstance().save(primary);
            
            File x = getPath().toFile();
            
            if(ServerConfig.antigrief.debug)
            {
                // Save as sNBT
                SNbtIo.writeSnbt(x.toPath(), primary);
            }else{
                try {
                    NbtIo.writeCompressed(primary, x);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, SAVE_INTERVAL, SAVE_INTERVAL, TimeUnit.MINUTES);
    }


    
    // Healer Queue's data source is a NBT File in the config folder
    public static final String HealerQueueFile = ".nbt";
    public static final String HealerQueueDebugFile = ".dev.snbt";

    public static Path getPath()
    {
        
        Path configDir = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
        //Path configFile = null;
        return configDir.resolve("ote_queue");
    }

    public static String getExtension()
    {
        
        if(ServerConfig.antigrief.debug)
        {
            return BlockSaver.HealerQueueDebugFile;

        }else {
            return BlockSaver.HealerQueueFile;
        }
    }
}
