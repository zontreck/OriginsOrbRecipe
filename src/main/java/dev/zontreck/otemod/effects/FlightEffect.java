package dev.zontreck.otemod.effects;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.Messages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;

import java.util.UUID;

public class FlightEffect extends MobEffect {
    int lastDuration = -1;
    protected FlightEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        lastDuration=pDuration;
        //OTEMod.LOGGER.info("Effect duration: "  + lastDuration);
        return pDuration > 0;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        super.addAttributeModifiers(entity, map, i);

        if(entity instanceof ServerPlayer player)
        {
            if(player.getAbilities().mayfly==false)
            {
                player.getAbilities().mayfly=true;
                player.onUpdateAbilities();

                ChatHelpers.broadcastTo(player, ChatHelpers.macro(Messages.FLIGHT_GIVEN), player.server);
            }
        }
    }

    private void removeFlightModifier(LivingEntity entity)
    {
        if(lastDuration == -1)
        {
            return;
        }
        if ( entity instanceof Player player )
        {
            if(ServerUtilities.isServer() && lastDuration < (5*20))
            {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                serverPlayer.getAbilities().mayfly = false;
                serverPlayer.getAbilities().flying = false;

                serverPlayer.onUpdateAbilities();

                ChatHelpers.broadcastTo(serverPlayer, ChatHelpers.macro(Messages.FLIGHT_REMOVED), serverPlayer.server);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap p_19470_, int p_19471_) {
        super.removeAttributeModifiers(entity, p_19470_, p_19471_);
        removeFlightModifier(entity);
    }
}
