package dev.zontreck.otemod.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PossBallItem extends Item
{
    public PossBallItem(Properties pProperties) {
        super(pProperties);
    }

    public boolean contents=false;
    @Override
    public boolean isFoil(ItemStack pStack) {
        return contents;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        contents = !contents;
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
