package dev.zontreck.otemod.antigrief2;

import java.util.Collection;
import java.util.function.Supplier;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.antigrief.HealRunner;
import dev.zontreck.otemod.antigrief.StoredBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.level.ExplosionEvent;

public class Healer extends SavedData implements Supplier<Object>
{
    private Level world;
    private TickingHealerTask task;
    static final String DATAKEY = OTEMod.MOD_ID+":"+Healer.class.getSimpleName();

    public Healer()
    {
        task = new TickingHealerTask();
    }

    public void onTick()
    {
        Collection<StoredBlock> blocks = task.tick();
        if(blocks != null)
        {
            for(StoredBlock bdata : blocks)
            {
                HealRunner.scheduleHeal(bdata);
            }
        }
    }

    public void onDetonate(ExplosionEvent.Detonate event)
    {
        Level world = event.getLevel();
        int maxTicks = 0;
        for(BlockPos posExplode : event.getAffectedBlocks())
        {
            BlockState stateExplode = world.getBlockState(posExplode);
            if(!isValid(stateExplode))
                continue;
            
            if(!stateExplode.isAir())
            {
                
            }
        }
    }

    @Override
    public Object get() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompoundTag save(CompoundTag p_77763_) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
