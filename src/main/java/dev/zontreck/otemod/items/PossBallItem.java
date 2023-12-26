package dev.zontreck.otemod.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PossBallItem extends Item
{
    public PossBallItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean isFoil(ItemStack pStack) {
        if(!pStack.hasTag())
        {
            pStack.setTag(new CompoundTag());
        }
        if(pStack.getTag().contains("entity"))
        {
            return true;
        } else return false;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 2;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (pLevel.random.nextFloat() * 0.4f + 0.8f));
        if(!pLevel.isClientSide)
        {
            ThrownPossBall TPB = new ThrownPossBall(pLevel, pPlayer);
            if(pPlayer.getAbilities().instabuild) TPB.setItem(stack.copy());
            else
                TPB.setItem(stack);
            TPB.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(TPB);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if(!pPlayer.getAbilities().instabuild)
        {
            stack.shrink(1);
        }else {

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
