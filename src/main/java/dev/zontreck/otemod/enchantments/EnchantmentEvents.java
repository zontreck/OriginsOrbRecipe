package dev.zontreck.otemod.enchantments;

import dev.zontreck.libzontreck.util.ServerUtilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber
public class EnchantmentEvents
{

    @SubscribeEvent
    public static void handleEnchantmentTicks(TickEvent.PlayerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT) return;

        if(event.phase == TickEvent.Phase.END)
        {
            if(event.player instanceof ServerPlayer sp)
            {
                FlightEnchantment.runEntityTick(sp);
                ConsumptionMending.onEntityTick(sp);
                NightVisionEnchantment.runEntityTick(sp);
                WaterBreathingEnchantment.runEntityTick(sp);
            }

        }
    }
}