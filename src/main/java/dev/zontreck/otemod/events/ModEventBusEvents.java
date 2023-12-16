package dev.zontreck.otemod.events;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.entities.ModEntityTypes;
import dev.zontreck.otemod.entities.monsters.PossumEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, bus = Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void onMobAttributeCreation(EntityAttributeCreationEvent ev)
    {
        //ev.put((EntityType<? extends LivingEntity>) ModEntityTypes.POSSUM.get(), PossumEntity.createAttributes());
        OTEMod.LOGGER.info("/!\\ REGISTERING ATTRIBUTES   /!\\");
    }
}
