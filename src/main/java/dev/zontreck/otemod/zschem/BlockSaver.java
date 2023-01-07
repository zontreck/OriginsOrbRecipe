package dev.zontreck.otemod.zschem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dev.zontreck.otemod.configs.OTEServerConfig;
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

    public static void InitialLoad()
    {
        
    }

    public void start()
    {
        executor.scheduleAtFixedRate(()->{
            CompoundTag primary = new CompoundTag();
            primary=BlockContainerList.getInstance().save(primary);
            
            File x = getPath().toFile();
            
            if(OTEServerConfig.DEBUG_HEALER.get())
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
    public static final String HealerQueueFile = "OTEHealerLastQueue.nbt";
    public static final String HealerQueueDebugFile = "OTEHealerLastQueue.snbt";

    public static Path getPath()
    {
        
        Path configDir = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
        Path configFile = null;
        if(OTEServerConfig.DEBUG_HEALER.get())
        {
            configFile = configDir.resolve(BlockSaver.HealerQueueDebugFile);

        }else {
            configFile = configDir.resolve(BlockSaver.HealerQueueFile);
        }

        //OTEMod.LOGGER.info("OTE HEALER TEMPORARY FILE: "+configFile.toFile().getAbsolutePath());
        return configFile;
    }
}
