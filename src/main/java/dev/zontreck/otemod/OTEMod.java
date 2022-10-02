package dev.zontreck.otemod;

import java.util.Set;
import java.util.Map.Entry;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.slf4j.Logger;

import dev.zontreck.otemod.blocks.ModBlocks;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.events.EventHandler;
import dev.zontreck.otemod.items.ModItems;
import dev.zontreck.otemod.ore.Modifier;
import dev.zontreck.otemod.ore.Modifier.ModifierOfBiomes;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OTEMod.MOD_ID)
public class OTEMod
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String FIRST_JOIN_TAG = "dev.zontreck.otemod.firstjoin";
    public static final String MOD_ID = "otemod";
    public static final String MODIFY_BIOMES = "modify_biomes";
    public static final ResourceLocation MODIFY_BIOMES_RL = new ResourceLocation(OTEMod.MOD_ID, MODIFY_BIOMES);


    public OTEMod()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);


        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, OTEServerConfig.SPEC, "otemod-rss-server.toml");
        
        
        
        // Register ourselves for server and other game events we are interested in
        final DeferredRegister<Codec<? extends BiomeModifier>> serializers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, OTEMod.MOD_ID);
        serializers.register(bus);
        serializers.register(MODIFY_BIOMES, ModifierOfBiomes::makeCodec);


        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        ModBlocks.register(bus);
        ModItems.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }


    @SubscribeEvent
    public void onSpawn(EntityJoinLevelEvent ev){
        Level w = ev.getLevel();
        if(w.isClientSide){
            return;
        }

        Entity e = ev.getEntity();
        if(!(e instanceof Player))return;

        Player p = (Player)e;
        
        if(firstJoin(p)){
            // Do first join actions here

            /*for (Entry<String,Integer> ent : SARServerConfig.INITIAL_ITEMS_TO_GIVE_ON_FIRST_JOIN.get().entrySet()) {
                
                Inventory i = p.getInventory();
                

            }*/
        }

    }

    public boolean firstJoin(Player p){
        
        Set<String> tags = p.getTags();

        if(tags.contains(OTEMod.FIRST_JOIN_TAG)){
            return false;
        }

        //p.addTag(ShapedAionResources.FIRST_JOIN_TAG);
        
        
        return true;
    }
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        //LOGGER.info("HELLO FROM COMMON SETUP");
        //LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            //LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

}
