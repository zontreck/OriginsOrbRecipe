package dev.zontreck.otemod.antigrief;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;

public class HealerManager implements Runnable
{
    private List<HealerWorker> Workers = new ArrayList<>();
    public HealerManager(){

    }
    public void registerWorker(HealerWorker worker)
    {
        Workers.add(worker);
    }

    public void deregisterWorker(HealerWorker worker)
    {
        Workers.remove(worker);
    }
    @Override
    public void run(){
        boolean skipWait=false;
        int skipCount=0;
        long lastSave = 0;
        final long saveInterval = (2*60); // Every 2 minutes
        boolean lastWait = false;
        // Heal pass 1 is set all positions to bedrock
        // Pass 2 is assert all solid blocks (Not air)
        // Pass 3 is to set the air blocks and remove bedrock


        while(OTEMod.ALIVE)
        {
            try{
                try{
                    if(!skipWait) Thread.sleep(OTEServerConfig.HEALER_TIMER.get());
                }catch(Exception E){}

                if(lastWait != OTEMod.HEALER_WAIT) OTEMod.LOGGER.info("Healer Wait Flag was Toggled");
                lastWait = OTEMod.HEALER_WAIT;
                if(OTEMod.HEALER_WAIT) continue; // Wait to process until import completes

                if(!OTEMod.ALIVE) break; // Begin tear down, server has shut down

                for (HealerWorker healerWorker : Workers) {
                    healerWorker.doTick=true;
                }


            }catch(Exception e){}
        }

        OTEMod.LOGGER.info("Tearing down healer jobs. Saving remaining queue, stand by...");
        try {
            HealerQueue.dump();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        OTEMod.LOGGER.info("Finished tearing down Healer - Good bye");
    }
}
