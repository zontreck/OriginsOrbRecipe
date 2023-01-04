package dev.zontreck.otemod.antigrief;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HealerWorker implements Runnable
{
    private List<StoredBlock> proc;
    public boolean alive=false; // These are the 
    public boolean doTick = false;
    public Thread MyThread;

    private boolean skipWait;

    public int Pass; // A local pass for this worker


    public HealerWorker(List<StoredBlock> toProcess)
    {
        // TODO: Make this a individualized heal worker that does the task of the current HealerManager, but with a central list of positions for de-duplication. But that a second worker would not process the same positions
        // The goal is to have a worker so a second explosion elsewhere does not reset the entire thing
        for (StoredBlock sBlock : toProcess) {
            if(!sBlock.claimed()) // Checks if a thread has been set on this storage block yet or not. This data does not get serialized.
                sBlock.setClaimed(); // We are not yet in a new thread but we know we will be soon.
        }
        proc = toProcess;
    }

    @Override
    public void run()
    {
        alive=true;
        MyThread=Thread.currentThread();
        HealerQueue.ManagerInstance.registerWorker(this);
        OTEMod.LOGGER.info("Hello from Healer Worker "+Thread.currentThread().getName());
        
        for(int i=0;i<proc.size();i++){
            StoredBlock sb = proc.get(i);
            sb.setClaimedBy(MyThread);
        }

        while(alive && OTEMod.ALIVE){
            // Stay alive
            // The tick event will be fired when appropriate
            if(doTick)tick();
            if(skipWait)tick();


            doTick=false;
        }

        OTEMod.LOGGER.info(Thread.currentThread().getName()+" has completed healing an area. Worker is now dismantling");

        HealerQueue.ManagerInstance.deregisterWorker(this);
    }

    public void tick()
    {

        try{

            // A tick in the healer worker tells it to repair a block
            // The healer manager is responsible for dispatching ticks

            
            if(proc.size()==0 && HealerQueue.getBlocksByWorker(this).size()==0)
            {
                alive=false;
                if(HealerQueue.dirty())
                    HealerQueue.dump();
                
                return;
            }

            // Get the first block in the list
            final StoredBlock sb = HealerQueue.locateLowestBlock(proc);
            ServerLevel level = null;
            StoredBlock below = null;

            if(sb != null)
            {
                level = sb.getWorldPosition().getActualDimension();
                below = HealerQueue.getExact(new WorldPosition(sb.getWorldPosition().Position.moveDown(), level));
            }


            switch(Pass)
            {
                case 0:
                {
                    // Pass 1. Set all positions to bedrock
                    // The code will check is the block is solid. If the block below it is not solid, it will set it to air, regardless of if it is a falling block of not

                    if(proc.size()==0)
                    {
                        // Move the validate list back into healer queue, and increment pass
                        OTEMod.LOGGER.info("Pass 1 completed, moving to pass 2");
                        Pass=1;
                        proc = HealerQueue.getBlocksByWorker(this);
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
                    proc.remove(sb);
                    break;
                }
                case 1:
                {
                    // Pass 2 only sets the solid blocks
                    if(proc.size()==0)
                    {
                        OTEMod.LOGGER.info("Pass 2 completed, moving to pass 3");
                        Pass++;
                        proc = HealerQueue.getBlocksByWorker(this);
                        HealerQueue.dump();
                        break;
                    }

                    if(!sb.getState().isAir())
                    {
                        skipWait=false;
                        HealRunner.scheduleHeal(sb);
                        HealerQueue.ToHeal.remove(sb);
                    }else{
                        skipWait=true;

                    }
                    proc.remove(sb);
                    break;
                }

                case 2:
                {
                    // Pass 3 removes bedrock by setting blocks that are air
                    if(proc.size()==0)
                    {
                        OTEMod.LOGGER.info("Pass 3 has been completed. Ending restore");
                        Pass=0;
                        proc.clear();
                        HealerQueue.dump();
                        break;
                    }
                    proc.remove(sb);
                    HealerQueue.ToHeal.remove(sb);

                    if(sb.getState().isAir())
                    {
                        BlockState bs = sb.getWorldPosition().getActualDimension().getBlockState(sb.getPos());
                        if(!bs.isAir() && !bs.is(Blocks.SCULK))
                        {
                            skipWait=true;
                            return;
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
                    Pass=0;
                    OTEMod.LOGGER.info("/!\\ ALERT /!\\\n\nWARNING: Unknown pass operation was added to the HealQueue");
                    break;
                }
            }
        }catch(Exception e){}
        
    }
    
}
