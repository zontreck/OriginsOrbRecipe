package dev.zontreck.otemod.blocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AuroraDoorBlock extends DoorBlock
{
    public static final BlockBehaviour.Properties DOOR_PROPS;
    static
    {
        DOOR_PROPS = BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).requiresCorrectToolForDrops().strength(10, 100000f).sound(SoundType.NETHERITE_BLOCK);
    }

    public AuroraDoorBlock() {
        super(DOOR_PROPS);
        
        
    }
    
}
