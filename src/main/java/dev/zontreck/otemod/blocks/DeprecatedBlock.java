package dev.zontreck.otemod.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class DeprecatedBlock extends Block
{
    public DeprecatedBlock(){
        super(BlockBehaviour.Properties.of().instabreak());
    }
}
