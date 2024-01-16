package dev.zontreck.otemod.zschem;

import java.util.Random;

import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.libzontreck.vectors.Vector3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HealRunner implements Runnable
{
    public final StoredBlock BlockToSet;
    // Play a popping sound at the block position
    public final SoundEvent pop = SoundEvents.ITEM_PICKUP;

    
    public HealRunner(StoredBlock sb)
    {
        BlockToSet = sb;
    }
    public static void scheduleHeal(StoredBlock sb){
        sb.getWorldPosition().getActualDimension().getServer().execute(new HealRunner(sb));
        
    }
    @Override
    public void run()
    {

        //BlockSnapshot bs = BlockSnapshot.create(level.dimension(), level, sb.getPos());
        
        //BlockState current = level.getBlockState(sb.getPos());
        ServerLevel level = (ServerLevel) BlockToSet.getWorldPosition().getActualDimension();
        

        BlockState nState = Block.updateFromNeighbourShapes(BlockToSet.getState(), level, BlockToSet.getPos());
        level.setBlock(BlockToSet.getPos(), nState, Block.UPDATE_CLIENTS); // no update?
        
        
        //level.setBlocksDirty(sb.getPos(), sb.getState(), level.getBlockState(sb.getPos()));
        //level.markAndNotifyBlock(sb.getPos(), level.getChunkAt(sb.getPos()), sb.getState(), level.getBlockState(sb.getPos()), 2, 0);

        //level.getChunkAt(sb.getPos()).setBlockState(sb.getPos(), sb.getState(), false);
        
        BlockEntity be = level.getBlockEntity(BlockToSet.getPos());
        
        if(be!=null){
            //be.deserializeNBT(sb.getBlockEntity());
            if(BlockToSet.getBlockEntity()!=null){

                be.load(BlockToSet.getBlockEntity());
                be.setChanged();
            }
            
        }

        // Everything is restored, play sound
        SoundSource ss = SoundSource.NEUTRAL;
        Vector3 v3 = BlockToSet.getWorldPosition().Position;
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
}
