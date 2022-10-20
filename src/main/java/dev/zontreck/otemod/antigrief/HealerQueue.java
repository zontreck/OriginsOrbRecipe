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

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class HealerQueue {
    // Healer Queue's data source is a NBT File in the config folder
    public static final String HealerQueueFile = "OTEHealerLastQueue.nbt";
    public static final String HealerQueueDebugFile = "OTEHealerLastQueue.snbt";

    public static List<StoredBlock> ToHeal = new ArrayList<StoredBlock>(); // Air and Solid Blocks get set to bedrock initially
    public static List<StoredBlock> ToValidate = new ArrayList<StoredBlock>(); // This contains all the blocks except air
    
    public static int Pass = 0;

    
    private static List<StoredBlock> LastToHeal = new ArrayList<StoredBlock>();
    private static List<StoredBlock> LastToValidate = new ArrayList<StoredBlock>();
    private static int LastPass = 0;

    public static HealerManager ManagerInstance=null;


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

        //OTEMod.LOGGER.info("OTE HEALER TEMPORARY FILE: "+configFile.toFile().getAbsolutePath());
        return configFile;
    }

    public static StoredBlock locateHighestBlock(List<StoredBlock> list)
    {
        StoredBlock sb = null;
        double currentY = 0;
        for (StoredBlock storedBlock : ToHeal) {
            if(storedBlock.getWorldPosition().Position.y > currentY)
            {
                currentY = storedBlock.getWorldPosition().Position.y;
                sb=storedBlock;
            }
        }

        return sb;
    }

    public static StoredBlock locateLowestBlock(List<StoredBlock> list)
    {
        StoredBlock sb = null;
        double currentY = 300;
        for (StoredBlock storedBlock : ToHeal) {
            if(storedBlock.getWorldPosition().Position.y < currentY)
            {
                currentY = storedBlock.getWorldPosition().Position.y;
                sb=storedBlock;
            }
        }

        return sb;
    }

    public static StoredBlock getExact(WorldPosition wp)
    {
        for (StoredBlock storedBlock : ToHeal) {
            if(storedBlock.getWorldPosition().same(wp))
            {
                return storedBlock;
            }
        }
        for (StoredBlock storedBlock : ToValidate) {
            if(storedBlock.getWorldPosition().same(wp))
            {
                return storedBlock;
            }
        }

        return null;
    }
    

    public static boolean HasValidatePosition(BlockPos pos, ServerLevel lvl)
    {
        Vector3 realPos = new Vector3(pos);
        WorldPosition real = new WorldPosition(realPos, lvl);

        for (StoredBlock storedBlock : ToHeal) {
            if(storedBlock.getWorldPosition().same(real))
            {
                return true;
            }
        }
        for (StoredBlock storedBlock : ToValidate) {
            if(storedBlock.getWorldPosition().same(real))return true;
        }

        return false;
    }

    public static void Initialize()
    {
        Thread tx = new Thread(new Runnable(){
            public void run(){
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
                    } catch (Exception e) {
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
                OTEMod.HEALER_WAIT=false;
            }
        });
        tx.start();

        // Set up the HealerManager / Runner
        ManagerInstance = new HealerManager();
        Thread txx = new Thread(ManagerInstance);
        txx.start();

        OTEMod.HEALER_THREAD = txx;
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
            lst2.add(block.serialize());
        }

        tag.put("queue", lst);
        tag.put("validate", lst2);
        tag.putInt("pass", HealerQueue.Pass);

        //OTEMod.LOGGER.info("HEAL ["+HealerQueue.ToHeal.size()+"] / VALIDATE ["+HealerQueue.ToValidate.size()+"]");

        // OK

        return tag;
    }

    public static List<StoredBlock> removeSame(List<StoredBlock> other)
    {
        other=removeSameFrom(ToHeal, other);
        other=removeSameFrom(ToValidate,other);

        return other;
    }

    public static List<StoredBlock> removeSameFrom(List<StoredBlock> stored, List<StoredBlock> other)
    {
        
        for(int i = 0;i<stored.size();i++)
        {
            for(int x = 0;x<other.size();x++)
            {
                if(other.get(x).getWorldPosition().same(stored.get(i).getWorldPosition()))
                {
                    // Both are same
                    other.remove(x);
                    x=-1; // Reset indexing
                }
            }
        }

        return other;
    }

    public static void removeExact(WorldPosition pos)
    {
        for (int i = 0; i < ToHeal.size(); i++) {
            if(ToHeal.get(i).getWorldPosition().same(pos)){
                ToHeal.remove(i);
            }
        }
        for (int i = 0; i < ToValidate.size(); i++) {
            if(ToValidate.get(i).getWorldPosition().same(pos)){
                ToValidate.remove(i);
            }
        }
    }


    public static void deserialize(CompoundTag tag)
    {
        OTEMod.HEALER_WAIT=true;
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
        }

        OTEMod.LOGGER.info("Finished loading the queue ["+HealerQueue.ToHeal.size()+"] items");

        if(tag.contains("validate"))
        {
            HealerQueue.ToValidate.clear();
            
            ListTag items2 = tag.getList("validate", Tag.TAG_COMPOUND);
            for(int i=0;i<items2.size();i++)
            {
                CompoundTag stored = items2.getCompound(i);
                StoredBlock sb = new StoredBlock(stored);
                HealerQueue.ToValidate.add(sb);
            }
        }

        HealerQueue.Pass = tag.getInt("pass");

        OTEMod.LOGGER.info("Finished loading validation queue for healer ["+HealerQueue.ToValidate.size()+"] items");
        OTEMod.HEALER_WAIT=false;
    }

    public static boolean dirty()
    {
        if(ToHeal!=LastToHeal)return true;
        if(ToValidate!=LastToValidate)return true;
        if(Pass != LastPass)return true;
        return false;
    }

    public static void dump() throws IOException
    {
        LastToHeal = ToHeal;
        LastToValidate = ToValidate;
        LastPass = Pass;
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
