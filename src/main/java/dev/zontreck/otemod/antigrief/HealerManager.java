package dev.zontreck.otemod.antigrief;

import java.io.IOException;
import java.util.Random;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
        boolean skipWait=false;
        while(OTEMod.ALIVE)
        {
            // Run the queue
            // We want to restore one block per run, then halt for number of seconds in config
            try {
                if(!skipWait)
                    Thread.sleep(OTEServerConfig.HEALER_TIMER.get());
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
            if(HealerQueue.ToHeal.size()==0){

                if(HealerQueue.ToValidate.size()==0)continue;

                // Validate success
                for(StoredBlock sb : HealerQueue.ToValidate)
                {
                    final ServerLevel level = sb.getWorldPosition().getActualDimension();
                    if(!level.getBlockState(sb.getPos()).is(sb.getState().getBlock()))
                    {
                        // Redo restore
                        HealerQueue.ToHeal.addAll(HealerQueue.ToValidate);
                        HealerQueue.ToValidate.clear();
                        break;
                    }
                }

                HealerQueue.ToValidate.clear();
                OTEMod.LOGGER.info("Validation of restore completed");

                continue;
            }
            // Play a popping sound at the block position
            final SoundEvent pop = SoundEvents.ITEM_PICKUP;
            // Get the first block in the list
            final StoredBlock sb = HealerQueue.ToHeal.get(0);
            final ServerLevel level = sb.getWorldPosition().getActualDimension();


            // Remove the block from the queue now to prevent further issues
            HealerQueue.ToHeal.remove(sb);
            if( !HealerQueue.ToValidate.add(sb) )
            {
                OTEMod.LOGGER.info("Failed to add Block to Validation queue!!! Verification of restore will not work");
            }

            // Healer object should have been added to the validation list


            // Check if the block to restore, and the block at the location are identical
            if(level.getBlockState(sb.getPos()).is(sb.getState().getBlock())){
                skipWait=true;
                continue; // Skip the wait, and this block
            } else skipWait=false;

            
            level.getServer().execute(new Runnable(){
                public void run()
                {

                    level.setBlockAndUpdate(sb.getPos(), sb.getState());
                    BlockEntity be = level.getBlockEntity(sb.getPos());
                    
                    if(be!=null)
                        be.deserializeNBT(sb.getBlockEntity());
        
                    // Everything is restored, play sound
                    SoundSource ss = SoundSource.NEUTRAL;
                    Vector3 v3 = sb.getWorldPosition().Position;
                    Random rng = new Random();
                    
                    level.playSound(null, v3.asBlockPos(), pop, ss, rng.nextFloat(0.75f,1.0f), rng.nextFloat(1));

                    /*for(ServerPlayer player : level.players())
                    {
                        Vector3 playerPos = new Vector3(player.position());
                        if(sb.getWorldPosition().Position.distance(playerPos) < 15)
                        {
                            // have player's client play sound (Packet?)
                        }
                    }*/

                    

                }
            });
            
            

            try {
                HealerQueue.dump();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
