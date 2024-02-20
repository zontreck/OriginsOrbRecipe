package dev.zontreck.otemod.blocks;

import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DeprecatedBlockItem extends BlockItem
{
    public DeprecatedBlockItem(Block a)
    {
        super(a, new Item.Properties().fireResistant());
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    /**
     * This is to give a use to an otherwise useless item. The piglins will exchange the item and it gets removed in that way.
     * @param stack
     * @return
     */
    @Override
    public boolean isPiglinCurrency(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(ChatHelpers.macro("!Dark_Red!This block is deprecated"));
        p_41423_.add(ChatHelpers.macro("!Dark_Green!It would appear this block smells faintly of gold. Maybe piglins will accept it?"));
        p_41423_.add(ChatHelpers.macro("!Dark_Red!This block is scheduled for removal in a future version. You should use it before it is too late."));
    }
}
