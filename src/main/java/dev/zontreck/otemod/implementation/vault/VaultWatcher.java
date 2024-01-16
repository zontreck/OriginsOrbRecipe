package dev.zontreck.otemod.implementation.vault;

import java.util.Map;
import java.util.UUID;

import dev.zontreck.otemod.OTEMod;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=OTEMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class VaultWatcher {

    @SubscribeEvent
    public void onClosedContainer(PlayerContainerEvent.Close ev)
    {
        if(ev.getEntity().level().isClientSide)return;
        //OTEMod.LOGGER.info("Player closed a container");
        // Player closed the container
        // Check if it is a vault Container
        if(ev.getContainer() instanceof VaultMenu)
        {
            // During testing the instance of VaultMenu we get passed back through this method gets a regenerated Vault ID, so our only option is to iterate here and commit a vault based on owner ID
            for(Map.Entry<UUID, VaultContainer> entry : VaultContainer.VAULT_REGISTRY.entrySet()){
                if(entry.getKey() == ev.getEntity().getUUID())
                {
                    entry.getValue().commit();
                    entry.getValue().invalidate();
                }
            }
        } else if(ev.getContainer() instanceof StarterMenu)
        {
            for(Map.Entry<UUID, StarterContainer> entry : StarterContainer.VAULT_REGISTRY.entrySet())
            {
                if(entry.getKey() == ev.getEntity().getUUID())
                {
                    entry.getValue().commit();
                    entry.getValue().invalidate();
                }
            }
        }
    }
}
