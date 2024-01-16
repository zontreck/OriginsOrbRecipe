package dev.zontreck.thresholds.events;

import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.integrations.KeyBindings;
import dev.zontreck.thresholds.networking.ModMessages;
import dev.zontreck.thresholds.networking.packets.OpenVaultC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.Instant;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ThresholdsMod.MOD_ID, value = Dist.CLIENT)
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
    }

    @Mod.EventBusSubscriber(modid =  ThresholdsMod.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBus
    {

    }
}
