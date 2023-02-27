package dev.zontreck.otemod.commands.teleport;

import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.events.RTPEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * This class aims to serve as the Random Position Locate system
 * It aims to be as non-thread blocking as possible to avoid server lag
 * 
 * To utilize, initialize a RTPContainer from the RandomPositionFactory and execute from there.
 */
public class RandomPositionLocator implements Runnable
{
    private final RTPContainer contain;

    public RandomPositionLocator(RTPContainer rtp)
    {
        contain=rtp;
    }

    @Override
    public void run() {
        if(!OTEMod.ALIVE)return;
        ChatServerOverride.broadcastTo(contain.container.PlayerInst.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Purple!Searching... Attempt "+String.valueOf(contain.tries)+"/30")), OTEMod.THE_SERVER);

        ServerLevel levl = contain.container.Dimension;
        ChunkAccess chunk  = levl.getChunk(contain.container.world_pos.Position.asBlockPos());
        ChunkPos cpos = chunk.getPos();
        boolean needsLoading = false;
        needsLoading = !(levl.getForcedChunks().contains(cpos.toLong()));


        if(needsLoading)
            levl.setChunkForced(cpos.x, cpos.z, true);

        int curChecks=0;
        while(curChecks<30)
        {
            if(contain.isSafe(contain.container.world_pos.Position.asBlockPos()))
            {
                contain.complete=true;
                if(needsLoading)    
                    levl.setChunkForced(cpos.x, cpos.z, false);
                
                if(MinecraftForge.EVENT_BUS.post(new RTPEvent(contain.container.PlayerInst, contain.container.world_pos)))
                {
                    contain.complete=false;
                    ChatServerOverride.broadcastTo(contain.container.PlayerInst.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Red!Last position checked was probably claimed. Another mod has asked us not to send you to that location, continuing the search")), OTEMod.THE_SERVER);

                    break;
                }
                return;
            }else {
                curChecks++;
                contain.move();
                OTEMod.LOGGER.info("[DEBUG] "+ChatColor.doColors("!Dark_Red!Checking position: "+contain.container.world_pos.Position.toString()+"; "+contain.container.Dimension.getBlockState(contain.container.world_pos.Position.asBlockPos()).getBlock().toString()+"; "+contain.container.Dimension.getBlockState(contain.container.world_pos.Position.asBlockPos().below()).getBlock().toString()));
            }
        }
        if(needsLoading)
            levl.setChunkForced(cpos.x, cpos.z, false);
            
        contain.newPosition();

        if(contain.tries > 30)
        {
            // Abort
            ChatServerOverride.broadcastTo(contain.container.PlayerInst.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Red!Could not find a suitable location in 30 attempts")), OTEMod.THE_SERVER);
            contain.aborted=true;
            return;
        }else {
            // Schedule the task to execute
            //run();
            RandomPositionLocator next = new RandomPositionLocator(contain);
            OTEMod.delayedExecutor.schedule(next, 2);
        }
    }
    
}
