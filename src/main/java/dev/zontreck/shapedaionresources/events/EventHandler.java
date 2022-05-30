package dev.zontreck.shapedaionresources.events;

import dev.zontreck.shapedaionresources.ShapedAionResources;
import dev.zontreck.shapedaionresources.ore.OreGenerator;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=ShapedAionResources.MOD_ID)
public class EventHandler {
    

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addOresToBiomes(final BiomeLoadingEvent ev){
        ShapedAionResources.LOGGER.info("Biome loading event called. Registering aion ores");
        OreGenerator.generateOres(ev);
    }
}
