package dev.zontreck.thresholds.zschem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dev.zontreck.thresholds.configs.ThresholdsServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
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
            
            if(ThresholdsServerConfig.DEBUG_HEALER.get())
            {
                // Save as sNBT
                String prettyFormat = NbtUtils.structureToSnbt(primary);
                    
                BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(x));
                    bw.write(prettyFormat);
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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
    public static final String HealerQueueDebugFile = ".dev.nbt";

    public static Path getPath()
    {
        
        Path configDir = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
        //Path configFile = null;
        return configDir.resolve("ote_queue");
    }

    public static String getExtension()
    {
        
        if(ThresholdsServerConfig.DEBUG_HEALER.get())
        {
            return BlockSaver.HealerQueueDebugFile;

        }else {
            return BlockSaver.HealerQueueFile;
        }
    }
}
