package dev.zontreck.otemod;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mojang.logging.LogUtils;
import dev.zontreck.otemod.integrations.KeyBindings;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import org.slf4j.Logger;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.blocks.ModBlocks;
import dev.zontreck.otemod.blocks.entity.ModEntities;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.commands.CommandRegistry;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.enchantments.ModEnchantments;
import dev.zontreck.otemod.entities.ModEntityTypes;
import dev.zontreck.otemod.entities.monsters.client.PossumRenderer;
import dev.zontreck.otemod.events.LoreHandlers;
import dev.zontreck.otemod.implementation.inits.ModMenuTypes;
import dev.zontreck.otemod.implementation.scrubber.ItemScrubberScreen;
import dev.zontreck.otemod.implementation.scrubber.MagicalScrubberScreen;
import dev.zontreck.otemod.implementation.vault.VaultScreen;
import dev.zontreck.otemod.implementation.vault.VaultWatcher;
import dev.zontreck.otemod.items.ModItems;
//import dev.zontreck.otemod.ore.Modifier.ModifierOfBiomes;
import dev.zontreck.otemod.networking.ModMessages;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OTEMod.MOD_ID)
public class OTEMod
{
    public static final Vector3 ZERO_VECTOR = new Vector3(0,0,0);
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String FIRST_JOIN_TAG = "dev.zontreck.otemod.firstjoin";
    public static final String MOD_ID = "otemod";
    public static final String MODIFY_BIOMES = "modify_biomes";
    public static final ResourceLocation MODIFY_BIOMES_RL = new ResourceLocation(OTEMod.MOD_ID, MODIFY_BIOMES);
    
    //public static List<TeleportContainer> TeleportRegistry = new ArrayList<>();
    public static MinecraftServer THE_SERVER;
    public static boolean ALIVE=false;
    public static boolean HEALER_WAIT=true; // Only on loading finish should this unlock
    public static Thread HEALER_THREAD;
    
    public static boolean DEVELOPER=false;
    private static Thread MasterThread;

    public static String OTEPrefix = "";
    public static String ONLY_PLAYER = "";
    public static IEventBus bus;
    

    public OTEMod()
    {

        OTEMod.OTEPrefix = ChatColor.doColors("!dark_gray![!dark_green!!bold!OTEMod!reset!!dark_gray!]!reset!");
        OTEMod.ONLY_PLAYER = ChatColor.doColors("!dark_red!Only a player can execute this command");

        bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::setup);


        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, OTEServerConfig.SPEC, "otemod-rss-server.toml");
        
        
        
        // Register ourselves for server and other game events we are interested in
        //final DeferredRegister<Codec<? extends BiomeModifier>> serializers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, OTEMod.MOD_ID);
        //serializers.register(bus);
        //serializers.register(MODIFY_BIOMES, ModifierOfBiomes::makeCodec);


        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LoreHandlers());
        MinecraftForge.EVENT_BUS.register(new ChatServerOverride());
        MinecraftForge.EVENT_BUS.register(new CommandRegistry());
        MinecraftForge.EVENT_BUS.register(new VaultWatcher());
        MinecraftForge.EVENT_BUS.register(new dev.zontreck.otemod.zschem.EventHandler());
        ModMenuTypes.CONTAINERS.register(bus);

        ModBlocks.register(bus);
        ModItems.register(bus);
        ModEntities.register(bus);
        ModEnchantments.register(bus);
        ModEntityTypes.register(bus);

        MinecraftForge.EVENT_BUS.register(new KeyBindings());

        //MenuInitializer.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }


    public boolean firstJoin(Player p){
        
        Set<String> tags = p.getTags();

        if(tags.contains(OTEMod.FIRST_JOIN_TAG)){
            return false;
        }

        //p.addTag(ShapedAionResources.FIRST_JOIN_TAG);
        
        
        return true;
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartedEvent event)
    {
        // Changed away from Starting event due to multiple calls
        if(OTEMod.ALIVE){
            // We were called again?
            // Wtf... return
            OTEMod.LOGGER.info("/!\\ ALERT /!\\ ServerStartedEvent was called multiple times. This is a bug in MinecraftForge");
            return;
        }
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");

        OTEMod.ALIVE=true;
        //HealerQueue.Initialize(); // Set up the queue

        // Set up the repeating task to expire a TeleportContainer
        OTEMod.THE_SERVER = event.getServer();
        OTEMod.MasterThread = new Thread(new Runnable(){
            public void run()
            {
                while(OTEMod.ALIVE){
                    // Check if the teleports have expired
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }

                    /*Iterator<TeleportContainer> containers = OTEMod.TeleportRegistry.iterator();
                    while(containers.hasNext())
                    {
                        TeleportContainer cont = containers.next();
                        Component expire = new TextComponent(OTEMod.OTEPrefix + ChatColor.DARK_PURPLE+" Teleport request has expired");
                        ChatServerOverride.broadcastTo(cont.FromPlayer, expire, OTEMod.THE_SERVER);
                        ChatServerOverride.broadcastTo(cont.ToPlayer, expire, OTEMod.THE_SERVER);
                        
                        containers.remove();
                    }*/
                }

                OTEMod.LOGGER.info("Tearing down OTEMod teleport queue - The server is going down");
            }
        });
        OTEMod.MasterThread.start();
        
    }
    

    @SubscribeEvent
    public void onItemExpire(final ItemExpireEvent ev)
    {
        if(ev.getEntity().level().isClientSide)return;

        if(OTEServerConfig.ITEM_DESPAWN_TIMER.get()<=0)return;

        ItemEntity ite = (ItemEntity)ev.getEntity();
        if(ite.getAge() != (1200  *  5)) {
            
            //OTEMod.LOGGER.info("Extra life has already been given to item : "+ev.getEntity().getName().getString());
            return; // We already gave it extra life, the default is 6000, or 5 minutes
        }
        
        //OTEMod.LOGGER.info("Giving extra life to item : "+ev.getEntity().getName().getString() + "; item age [ "+ev.getEntity().getAge()+ " ]");
        // 1200 ticks per minute
        // OTEMod item despawn amplifier is set in 5 minute intervals
        ev.setExtraLife((1200 * 5)+ ((1200 * 5) * OTEServerConfig.ITEM_DESPAWN_TIMER.get())); // reset the life count
        //OTEMod.LOGGER.info("Item ["+ev.getEntity().getItem().getDisplayName().getString()+"] was given extra life");
        // Hopefully this works?
        ev.setCanceled(true);
        
    }

    
    @SubscribeEvent
    public void onStop(final ServerStoppingEvent ev)
    {
        OTEMod.ALIVE=false; // Tear down all looping threads that will watch this
        OTEMod.MasterThread.interrupt();
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

            MenuScreens.register(ModMenuTypes.VAULT.get(), VaultScreen::new);
            MenuScreens.register(ModMenuTypes.SCRUBBER.get(), ItemScrubberScreen::new);
            MenuScreens.register(ModMenuTypes.MAGIC_SCRUBBER.get(), MagicalScrubberScreen::new);

            //ItemBlockRenderTypes.setRenderLayer(ModBlocks.AURORA_DOOR.get(), RenderType.translucent());

            EntityRenderers.register(ModEntityTypes.POSSUM.get(), PossumRenderer::new);
        }

    }

}
