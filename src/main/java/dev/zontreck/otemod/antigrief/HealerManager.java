package dev.zontreck.otemod.antigrief;

import java.io.IOException;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.containers.Vector3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HealerManager implements Runnable
{
    public HealerManager(){

    }
    @Override
    public void run(){
        while(OTEMod.ALIVE)
        {
            // Run the queue
            // We want to restore one block per run, then halt for number of seconds in config
            try {
                Thread.sleep(Long.parseLong(String.valueOf(OTEServerConfig.HEALER_TIMER.get()*1000)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!OTEMod.ALIVE)
            {
                // Server has begun to shut down while we were sleeping
                // Begin tear down
                break;
            }

            // Loop back to start if no items in queue
            if(HealerQueue.ToHeal.size()==0)continue;
            // Play a popping sound at the block position
            SoundEvent pop = SoundEvents.ITEM_PICKUP;
            // Get the first block in the list
            StoredBlock sb = HealerQueue.ToHeal.get(0);
            // Remove the block from the queue now to prevent further issues
            HealerQueue.ToHeal.remove(sb);
            ServerLevel level = sb.getWorldPosition().getActualDimension();
            
            level.setBlock(sb.getPos(), sb.getState(), 0);
            BlockEntity be = level.getBlockEntity(sb.getPos());
            be.deserializeNBT(sb.getBlockEntity());

            // Everything is restored, play sound
            SoundSource ss = SoundSource.BLOCKS;
            Vector3 v3 = sb.getWorldPosition().Position;
            level.playSound(null, v3.x, v3.y, v3.z, pop, ss, 0, 0);
            
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
