package dev.zontreck.otemod.events;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.ore.Modifier;
//import dev.zontreck.otemod.ore.OreGenerator;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid=OTEMod.MOD_ID)
public class EventHandler {
    
    /*
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addOresToBiomes(final BiomeLoadingEvent ev){
        //ShapedAionResources.LOGGER.info("Biome loading event called. Registering aion ores");
        OreGenerator.generateOres(ev);
    }*/

    @SubscribeEvent
    public void onGatherData(GatherDataEvent ev){
        Modifier.DoProcess(ev);
    }
}
