package dev.zontreck.otemod.effects;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.otemod.enchantments.ModEnchantments;
import dev.zontreck.otemod.implementation.Messages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.Nullable;

public class FlightEffect extends MobEffect {
    protected FlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if(LibZontreck.CURRENT_SIDE == LogicalSide.CLIENT) return;
        if(pLivingEntity instanceof Player)
        {
            if(LibZontreck.CURRENT_SIDE == LogicalSide.SERVER)
            {
                ServerPlayer player = ServerUtilities.getPlayerByID(pLivingEntity.getStringUUID());

                recheck(player);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {

        if(LibZontreck.CURRENT_SIDE == LogicalSide.CLIENT) return;
        if(pLivingEntity instanceof Player)
        {
            ServerPlayer player = ServerUtilities.getPlayerByID(pLivingEntity.getStringUUID());

            recheck(player);
        }
        super.applyInstantenousEffect(pSource, pIndirectSource, pLivingEntity, pAmplifier, pHealth);
    }

    private static void recheck(ServerPlayer sp)
    {
        if(sp.gameMode.isCreative())return; // Don't mess with the creative mode attributes


        ItemStack feet = sp.getItemBySlot(EquipmentSlot.FEET);

        boolean hasFlight = false;

        if(ItemUtils.getEnchantmentLevel(ModEnchantments.FLIGHT_ENCHANTMENT.get(), feet)>0)hasFlight=true;

        if(!hasFlight)
        {
            sp.removeEffect(ModEffects.FLIGHT.get());
        }


        Abilities playerAbilities = sp.getAbilities();
        if(hasFlight)
        {
            if(playerAbilities.mayfly == false)
            {
                playerAbilities.mayfly=true;
                sp.onUpdateAbilities();

                ChatHelpers.broadcastTo(sp, ChatHelpers.macro(Messages.FLIGHT_GIVEN), sp.server);
            }
        }else {
            if(playerAbilities.mayfly)
            {

                playerAbilities.mayfly=false;
                playerAbilities.flying=false;
                sp.onUpdateAbilities();

                ChatHelpers.broadcastTo(sp, ChatHelpers.macro(Messages.FLIGHT_REMOVED), sp.server);
            }
        }
    }
}
