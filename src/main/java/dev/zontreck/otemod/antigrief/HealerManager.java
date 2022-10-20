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

                if(HealerQueue.ToHeal.size()==0 && HealerQueue.ToValidate.size()==0)
                {
                    HealerQueue.Pass=0;
                    if(HealerQueue.dirty())
                        HealerQueue.dump();
                    continue;
                }

                // Get the first block in the list
                final StoredBlock sb = HealerQueue.locateLowestBlock(HealerQueue.ToHeal);
                ServerLevel level = null;
                StoredBlock below = null;

                if(sb != null)
                {
                    level = sb.getWorldPosition().getActualDimension();
                    below = HealerQueue.getExact(new WorldPosition(sb.getWorldPosition().Position.moveDown(), level));
                }


                switch(HealerQueue.Pass)
                {
                    case 0:
                    {
                        // Pass 1. Set all positions to bedrock
                        // The code will check is the block is solid. If the block below it is not solid, it will set it to air, regardless of if it is a falling block of not

                        if(HealerQueue.ToHeal.size()==0)
                        {
                            // Move the validate list back into healer queue, and increment pass
                            OTEMod.LOGGER.info("Pass 1 completed, moving to pass 2");
                            HealerQueue.Pass=1;
                            HealerQueue.ToHeal = HealerQueue.ToValidate;
                            HealerQueue.ToValidate = new ArrayList<>();
                            HealerQueue.dump();
                            break; // Exit this loop
                        }

                        if(below == null){
                            // This line will prevent the block below from getting set to Sculk
                            below = StoredBlock.getSculk(new WorldPosition(sb.getWorldPosition().Position.moveDown(), level)); // below is null so it is a unknown, accept a loss if its a falling block
                        }
                        

                        if(!sb.getState().isAir() && below.getState().isAir())
                        {
                            HealRunner.scheduleHeal(StoredBlock.getSculk(below.getWorldPosition()));
                            skipWait=false;
                        }else {
                            if(!sb.getState().isAir())
                            {
                                HealRunner.scheduleHeal(StoredBlock.getSculk(sb.getWorldPosition()));
                                skipWait=false;
                            } else {
                                skipWait=true;
                            }
                        }
                        HealerQueue.ToValidate.add(sb);
                        HealerQueue.ToHeal.remove(sb);
                        break;
                    }
                    case 1:
                    {
                        // Pass 2 only sets the solid blocks
                        if(HealerQueue.ToHeal.size()==0)
                        {
                            OTEMod.LOGGER.info("Pass 2 completed, moving to pass 3");
                            HealerQueue.Pass++;
                            HealerQueue.ToHeal = HealerQueue.ToValidate;
                            HealerQueue.ToValidate = new ArrayList<>();
                            HealerQueue.dump();
                            break;
                        }

                        if(!sb.getState().isAir())
                        {
                            skipWait=false;
                            HealRunner.scheduleHeal(sb);
                        }else{
                            skipWait=true;

                            HealerQueue.ToValidate.add(sb);
                        }
                        HealerQueue.ToHeal.remove(sb);
                        break;
                    }

                    case 2:
                    {
                        // Pass 3 removes bedrock by setting blocks that are air
                        if(HealerQueue.ToHeal.size()==0)
                        {
                            OTEMod.LOGGER.info("Pass 3 has been completed. Ending restore");
                            HealerQueue.Pass=0;
                            HealerQueue.ToHeal.clear();
                            HealerQueue.ToValidate.clear();
                            HealerQueue.dump();
                            break;
                        }
                        HealerQueue.ToHeal.remove(sb);

                        if(sb.getState().isAir())
                        {
                            BlockState bs = sb.getWorldPosition().getActualDimension().getBlockState(sb.getPos());
                            if(!bs.isAir() && !bs.is(Blocks.SCULK))
                            {
                                skipWait=true;
                                continue;
                            }
                            if(!bs.isAir()){
                                skipWait=false;
                                HealRunner.scheduleHeal(sb);
                            }else skipWait=true;
                        }else skipWait=true;
                        break;
                    }
                    default:
                    {
                        HealerQueue.Pass=0;
                        OTEMod.LOGGER.info("/!\\ ALERT /!\\\n\nWARNING: Unknown pass operation was added to the HealQueue");
                        break;
                    }
                }

            }catch(Exception e){}
        }
        OTEMod.ALIVE=false;
        /*while(OTEMod.ALIVE) // do nothing, code is disabled here.
        {
            try{

                // Loop back to start if no items in queue
                if(HealerQueue.ToHeal.size()==0){

                    if(HealerQueue.ToValidate.size()==0)continue;

                    boolean redo = false;
                    // Validate success
                    for(StoredBlock sb : HealerQueue.ToValidate)
                    {
                        try{

                            final ServerLevel level = sb.getWorldPosition().getActualDimension();
                            if(!level.getBlockState(sb.getPos()).is(sb.getState().getBlock()))
                            {
                                redo=true;
                                // Redo restore
                                HealerQueue.ToHeal.add(sb);
                            }
                        }catch(Exception e){}
                    }

                    HealerQueue.ToValidate.clear();
                    OTEMod.LOGGER.info("Validation of restore completed");
                    HealerQueue.dump();

                    if(redo)
                    {
                        HealerQueue.Shuffle();
                        OTEMod.LOGGER.info("Validation was not successful, and the repair will be repeated");
                    }
                    continue;
                }
                
                // Get the first block in the list
                final StoredBlock sb = HealerQueue.locateHighestBlock(HealerQueue.ToHeal);
                final ServerLevel level = sb.getWorldPosition().getActualDimension();

                // Remove the block from the queue now to prevent further issues
                if( !HealerQueue.ToValidate.add(sb) )
                {
                    OTEMod.LOGGER.info("Failed to add Block to Validation queue!!! Verification of restore will not work");
                }else 
                    HealerQueue.ToHeal.remove(sb);

                // Healer object should have been added to the validation list


                // Check if the block to restore, and the block at position are air. 
                if(level.getBlockState(sb.getPos()).isAir() && sb.getState().isAir()){
                    skipWait=true;
                    continue; // Skip the wait, and this block
                } else skipWait=false;


                if(skipWait)
                {
                    if(skipCount > 5){
                        skipCount=0;
                        skipWait=false;// Give the server a chance to breathe
                    }
                    else {
                        skipCount++;
                    }
                }else skipCount=0;
                
                

                if(!skipWait) // Only save the queue when sleeping appropriately
                {

                    if(lastSave+saveInterval < Instant.now().getEpochSecond()){

                        try {
                            HealerQueue.dump();
                            lastSave = Instant.now().getEpochSecond();
                            OTEMod.LOGGER.info("Flushing current healer queue to disk...");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }*/

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
