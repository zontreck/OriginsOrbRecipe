package dev.zontreck.otemod.antigrief;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import dev.zontreck.libzontreck.vectors.Vector3;
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
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;

public class HealerManager implements Runnable
{
    public HealerManager(){

    }
    @Override
    public void run(){
        boolean skipWait=false;
        int skipCount=0;
        long lastSave = 0;
        final long saveInterval = (2*60); // Every 2 minutes
        boolean lastWait = false;

        while(OTEMod.ALIVE)
        {
            try{
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
                if(lastWait != OTEMod.HEALER_WAIT){
                    OTEMod.LOGGER.info("Healer wait flag was toggled");
                }
                lastWait = OTEMod.HEALER_WAIT;
                if(OTEMod.HEALER_WAIT)
                    continue; // Wait until the saved queue has been fully imported

                if(!OTEMod.ALIVE)
                {
                    // Server has begun to shut down while we were sleeping
                    // Begin tear down
                    break;
                }

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
                
                // Play a popping sound at the block position
                final SoundEvent pop = SoundEvents.ITEM_PICKUP;
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

                
                level.getServer().execute(new Runnable(){
                    public void run()
                    {

                        //BlockSnapshot bs = BlockSnapshot.create(level.dimension(), level, sb.getPos());
                        
                        //BlockState current = level.getBlockState(sb.getPos());
                        BlockState nState = Block.updateFromNeighbourShapes(sb.getState(), level, sb.getPos());
                        level.setBlock(sb.getPos(), nState, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE); // no update?
                        
                        
                        //level.setBlocksDirty(sb.getPos(), sb.getState(), level.getBlockState(sb.getPos()));
                        //level.markAndNotifyBlock(sb.getPos(), level.getChunkAt(sb.getPos()), sb.getState(), level.getBlockState(sb.getPos()), 2, 0);

                        //level.getChunkAt(sb.getPos()).setBlockState(sb.getPos(), sb.getState(), false);
                        
                        BlockEntity be = level.getBlockEntity(sb.getPos());
                        
                        if(be!=null){
                            //be.deserializeNBT(sb.getBlockEntity());
                            be.load(sb.getBlockEntity());
                            be.setChanged();
                            
                        }
            
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
