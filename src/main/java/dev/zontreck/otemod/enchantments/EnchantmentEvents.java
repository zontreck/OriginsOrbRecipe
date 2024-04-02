package dev.zontreck.otemod.enchantments;

import dev.zontreck.libzontreck.util.ServerUtilities;
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
    private static int TICKS = 0;
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(server.getPlayerCount() == 0) return;

        if(TICKS >= 20)
        {

            for(ServerPlayer sp : server.getPlayerList().getPlayers())
            {
                FlightEnchantment.runEntityTick(sp);
                ConsumptionMending.onEntityTick(sp);
                NightVisionEnchantment.runEntityTick(sp);
            }

            TICKS=0;

        }

        TICKS++;
    }
}