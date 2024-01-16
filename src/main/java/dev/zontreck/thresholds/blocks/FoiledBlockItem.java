package dev.zontreck.thresholds.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FoiledBlockItem extends BlockItem {

    public FoiledBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

}
