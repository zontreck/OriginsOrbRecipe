package dev.zontreck.otemod.antigrief;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class HealerQueue {
    // Healer Queue's data source is a NBT File in the config folder
    public static final String HealerQueueFile = "OTEHealerLastQueue.nbt";
    public static final String HealerQueueDebugFile = "OTEHealerLastQueue.snbt";

    public static List<StoredBlock> ToHeal = new ArrayList<StoredBlock>();
    public static List<StoredBlock> ToValidate = new ArrayList<StoredBlock>();

    public static Path getPath()
    {

        Path configDir = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
        Path configFile = null;
        if(OTEServerConfig.DEBUG_HEALER.get())
        {
            configFile = configDir.resolve(HealerQueue.HealerQueueDebugFile);

        }else {
            configFile = configDir.resolve(HealerQueue.HealerQueueFile);
        }

        OTEMod.LOGGER.info("OTE HEALER TEMPORARY FILE: "+configFile.toFile().getAbsolutePath());
        return configFile;
    }

    public static void Initialize()
    {
        if(OTEServerConfig.DEBUG_HEALER.get())
        {
            // Load the sNBT file
            Path configFile = getPath();
            File x = configFile.toFile();
            String FinalStr = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader(x));
                while(br.ready())
                {
                    FinalStr += br.readLine();
                    FinalStr += "\n";
                }
                br.close();
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e)
            {
                e.printStackTrace();
            }
            
            try {
                HealerQueue.deserialize(NbtUtils.snbtToStructure(FinalStr));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        } else {
            // Load from normal NBT
            Path configFile = getPath();
            File x = configFile.toFile();
            // Load binary
            try {
                CompoundTag tag = NbtIo.readCompressed(x);
                HealerQueue.deserialize(tag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set up the HealerManager / Runner
        Thread tx = new Thread(new HealerManager());
        tx.start();
    }

    public static CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        // Save entire list
        ListTag lst = new ListTag();
        for(final StoredBlock block : HealerQueue.ToHeal)
        {
            lst.add(block.serialize());
        }
        ListTag lst2 = new ListTag();
        for(final StoredBlock block : HealerQueue.ToValidate)
        {
            lst.add(block.serialize());
        }

        tag.put("queue", lst);
        tag.put("validate", lst2);

        // OK

        return tag;
    }

    public static void deserialize(CompoundTag tag)
    {
        // Begin parsing
        if(tag.contains("queue"))
        {
            HealerQueue.ToHeal.clear();
            // Read the list

            ListTag items = tag.getList("queue", Tag.TAG_COMPOUND);
            
            for(int i=0;i<items.size();i++)
            {
                CompoundTag stored = items.getCompound(i);
                StoredBlock sb = new StoredBlock(stored);
                HealerQueue.ToHeal.add(sb);
            }

            ListTag items2 = tag.getList("validate", Tag.TAG_COMPOUND);
            for(int i=0;i<items2.size();i++)
            {
                CompoundTag stored = items2.getCompound(i);
                StoredBlock sb = new StoredBlock(stored);
                HealerQueue.ToValidate.add(sb);
            }
        }
    }
    public static void dump() throws IOException
    {
        CompoundTag serialized = HealerQueue.serialize();
        if(OTEServerConfig.DEBUG_HEALER.get())
        {
            // Save to sNBT
            String prettyOutput = NbtUtils.structureToSnbt(serialized);
            Path storage = getPath();
            File x = storage.toFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(x));
            bw.write(prettyOutput);
            bw.close();
            
        }else {
            NbtIo.writeCompressed(serialized, getPath().toFile());
        }
    }

    public static void Shuffle()
    {
        Collections.shuffle(ToHeal);

        if(OTEServerConfig.DEBUG_HEALER.get())
            try {
                dump();// Push contents to disk if in debug mode for easier analyzing on change
            } catch (IOException e) {
                e.printStackTrace();
            } 
    }
}
