package dev.zontreck.otemod.events;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.energy.IThresholdsEnergyContainer;
import dev.zontreck.otemod.integrations.KeyBindings;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.EnergyRequestC2SPacket;
import dev.zontreck.otemod.networking.packets.OpenVaultC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.Instant;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents
    {
        // Timeout!
        static long lastPress;

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event)
        {
            //OTEMod.LOGGER.info("KEY PRESS: "+event.getKey());
            if(KeyBindings.OPEN_VAULT.matches(event.getKey(), event.getScanCode()) && Minecraft.getInstance().screen == null && lastPress+10 < Instant.now().getEpochSecond())
            {
                lastPress = Instant.now().getEpochSecond();
                ModMessages.sendToServer(new OpenVaultC2SPacket(0, false, 0));
            }
        }


        private static int TICK_COUNT = 0;
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event)
        {
            TICK_COUNT ++;

            if(TICK_COUNT >= (2*20))
            {
                Screen screen = Minecraft.getInstance().screen;
                BlockPos pos;
                BlockEntity entity;
                Level lvl;
                if(screen instanceof IThresholdsEnergyContainer itc)
                {
                    pos = itc.getPosition();
                    entity = itc.getEntity();
                    lvl = entity.getLevel();
                    ResourceLocation location = lvl.dimension().location();

                    ModMessages.sendToServer(new EnergyRequestC2SPacket(pos, lvl, Minecraft.getInstance().player));
                }
            }
        }
    }


    @Mod.EventBusSubscriber(modid =  OTEMod.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBus
    {

    }
}
