package dev.zontreck.otemod.antigrief;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import dev.zontreck.otemod.OTEMod;

public class HealerWorker implements Runnable
{
    private final List<StoredBlock> proc;
    public boolean alive=false; // These are the 
    public boolean doTick = false;
    public Thread MyThread;
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
        MyThread=Thread.currentThread();
        HealerQueue.ManagerInstance.registerWorker(this);
        for (StoredBlock storedBlock : proc) {
            storedBlock.setClaimedBy(Thread.currentThread());
        }

        while(alive){
            // Stay alive
            // The tick event will be fired when appropriate
            if(doTick)tick();


            doTick=false;
        }

        OTEMod.LOGGER.info(Thread.currentThread().getName()+" has completed healing an area. Worker is now dismantling");

        HealerQueue.ManagerInstance.deregisterWorker(this);
    }

    public void tick()
    {
        // A tick in the healer worker tells it to repair a block
        // The healer manager is responsible for dispatching ticks
    }
    
}
