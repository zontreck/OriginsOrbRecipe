package dev.zontreck.otemod.enchantments;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber
public class EnchantmentEvents
{
    private static boolean canTick = false;
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START)
        {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            canTick = server!=null && server.getTickCount()%20==0;
        }
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingUpdateEvent tick)
    {
        if(canTick)
        {
            // Process Enchantments

            if(tick.getEntityLiving() instanceof ServerPlayer sp)
            {
                FlightEnchantment.runEntityTick(sp);
                ConsumptionMending.onEntityTick(sp);
            }
        }
    }
}
