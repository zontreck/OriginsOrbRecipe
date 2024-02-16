package dev.zontreck.otemod.items;

import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class IhanCrystal extends Item{

    public static final String TAG_XP = "xp";

    public IhanCrystal() {
        super(new Properties().stacksTo(1));
    }

    private void assertTag(ItemStack stack)
    {
        if(!stack.hasTag() || stack.getTag()==null)
        {
            stack.setTag(new CompoundTag());
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        assertTag(stack);

        if(stack.getTag().contains(TAG_XP))
        {
            if(stack.getTag().getInt(TAG_XP) > 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        assertTag(stack);

        tooltip.add(ChatHelpers.macro("!Dark_Green!Stored XP: !Dark_Red!" + stack.getTag().getInt(TAG_XP) + "!Dark_Green! level" + ((stack.getTag().getInt(TAG_XP)>1)?"levels" : "level")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level serverWorld, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        assertTag(stack);

        serverWorld.playSound(player, player.getOnPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL);

        if(!serverWorld.isClientSide)
        {

            ServerPlayer serverPlayer = (ServerPlayer) player;
            if(player.isCrouching())
            {
                int lvls=0;
                // Unpack the experience levels - 10 at a time!!
                int xp = stack.getTag().getInt(TAG_XP);
                if(xp>10){
                    lvls = 10;
                }else lvls= xp;
                xp-=lvls;

                serverPlayer.setExperienceLevels(serverPlayer.experienceLevel + lvls);

                stack.getTag().putInt(TAG_XP, xp);
            } else {
                // Store the xp
                int xp = stack.getTag().getInt(TAG_XP);
                xp += player.experienceLevel;

                serverPlayer.setExperienceLevels(0);

                stack.getTag().putInt(TAG_XP, xp);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, serverWorld.isClientSide);

    }

}
