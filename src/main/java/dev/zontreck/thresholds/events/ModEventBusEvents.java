package dev.zontreck.thresholds.events;

import dev.zontreck.thresholds.ThresholdsMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ThresholdsMod.MOD_ID, bus = Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void onMobAttributeCreation(EntityAttributeCreationEvent ev)
    {
        //ev.put((EntityType<? extends LivingEntity>) ModEntityTypes.POSSUM.get(), PossumEntity.createAttributes());
        ThresholdsMod.LOGGER.info("/!\\ REGISTERING ATTRIBUTES   /!\\");
    }
}
