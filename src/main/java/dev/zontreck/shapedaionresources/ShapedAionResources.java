package dev.zontreck.shapedaionresources;

import java.util.Set;
import java.util.Map.Entry;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import dev.zontreck.shapedaionresources.blocks.ModBlocks;
import dev.zontreck.shapedaionresources.configs.SARServerConfig;
import dev.zontreck.shapedaionresources.events.EventHandler;
import dev.zontreck.shapedaionresources.items.ModItems;
import dev.zontreck.shapedaionresources.ore.OreGenerator;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("shapedaionresources")
public class ShapedAionResources
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String FIRST_JOIN_TAG = "dev.zontreck.shapedaionresources.firstjoin";
    public static final String MOD_ID = "shapedaionresources";


    public ShapedAionResources()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);


        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SARServerConfig.SPEC, "aion-rss-server.toml");
        
        
        
        // Register ourselves for server and other game events we are interested in



        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        ModBlocks.register(bus);
        ModItems.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent ev){
        Level w = ev.getWorld();
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

        if(tags.contains(ShapedAionResources.FIRST_JOIN_TAG)){
            return false;
        }

        //p.addTag(ShapedAionResources.FIRST_JOIN_TAG);
        
        
        return true;
    }


}
