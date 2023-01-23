package dev.zontreck.otemod.events;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.integrations.KeyBindings;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.OpenVaultPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents
    {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event)
        {
            //OTEMod.LOGGER.info("KEY PRESS: "+event.getKey());
            if(KeyBindings.OPEN_VAULT.consumeClick())
            {
                ModMessages.sendToServer(new OpenVaultPacket(0, false, 0));
            }
        }
    }

    @Mod.EventBusSubscriber(modid =  OTEMod.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBus
    {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event)
        {
            event.register(KeyBindings.OPEN_VAULT);
        }
    }
}
