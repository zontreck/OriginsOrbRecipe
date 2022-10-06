package dev.zontreck.otemod;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.slf4j.Logger;


import dev.zontreck.otemod.blocks.ModBlocks;
import dev.zontreck.otemod.chat.ChatColor;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.commands.CommandRegistry;
import dev.zontreck.otemod.commands.teleport.TeleportContainer;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.configs.Profile;
import dev.zontreck.otemod.database.Database;
import dev.zontreck.otemod.database.Database.DatabaseConnectionException;
import dev.zontreck.otemod.events.EventHandler;
import dev.zontreck.otemod.implementation.VaultScreen;
import dev.zontreck.otemod.implementation.VaultWatcher;
import dev.zontreck.otemod.implementation.inits.MenuInitializer;
import dev.zontreck.otemod.items.ModItems;
import dev.zontreck.otemod.ore.Modifier.ModifierOfBiomes;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OTEMod.MOD_ID)
public class OTEMod
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String FIRST_JOIN_TAG = "dev.zontreck.otemod.firstjoin";
    public static final String ITEM_LIVES_TAG = "dev.zontreck.otemod.entity.extralife";
    public static final String MOD_ID = "otemod";
    public static final String MODIFY_BIOMES = "modify_biomes";
    public static final ResourceLocation MODIFY_BIOMES_RL = new ResourceLocation(OTEMod.MOD_ID, MODIFY_BIOMES);
    public static Database DB=null;
    public static Map<String,Profile> PROFILES = new HashMap<String,Profile>();
    public static List<TeleportContainer> TeleportRegistry = new ArrayList<>();
    public static MinecraftServer THE_SERVER;
    private static boolean ALIVE;
    
    public static boolean DEVELOPER=false;
    private static Thread MasterThread;

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
        MinecraftForge.EVENT_BUS.register(new ChatServerOverride());
        MinecraftForge.EVENT_BUS.register(new CommandRegistry());
        MinecraftForge.EVENT_BUS.register(new VaultWatcher());
        MenuInitializer.CONTAINERS.register(bus);

        ModBlocks.register(bus);
        ModItems.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

/*
 *  @DISABLED DUE TO PlayerEvent.PlayerLoggedInEvent
 * with that event, we just handle this there.  This code is kept as a reference until the new player gear functions have been added.
 * Prereq for new player gear: OTEMod Vault API
 * 
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
                

            }
        }

    }
 * 
 */

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

        try {
            OTEMod.DB = new Database(this);
            OTEMod.ALIVE=true;
            // Validate that the database has been established and that tables exist
            Connection con = OTEMod.DB.getConnection();
            con.setAutoCommit(true);


            con.beginRequest();

            Statement lookup = con.createStatement();
            lookup.execute("CREATE TABLE IF NOT EXISTS `homes` (" +
"                `number` int(11) NOT NULL," +
"                `user` varchar(255) NOT NULL," +
"                `home_name` varchar(255) NOT NULL," +
"                `x` varchar(20) NOT NULL," + 
"                `y` varchar(20) NOT NULL," +
"                `z` varchar(20) NOT NULL," +
"                `rot_x` varchar(20) NOT NULL," + 
"                `rot_y` varchar(20) NOT NULL," + 
"                `dimension` varchar(255) NOT NULL)"); // 10/04/2022 - fix dimension column size due to a bug where mods might have long names!

            lookup.execute("CREATE TABLE IF NOT EXISTS `profiles` ("+
            "`username` varchar (255) not null,"+
            "`uuid` varchar (255) not null,"+
            "`prefix` varchar (255) not null,"+
            "`nickname` varchar (255) not null,"+
            "`name_color` varchar (255) not null,"+
            "`prefix_color` varchar(255) not null,"+
            "`chat_color` varchar(255) not null)");

            lookup.execute("CREATE TABLE IF NOT EXISTS `vaults` (" + 
            "`uuid` varchar (128) NOT NULL, " +
            "`number` int (11) not null," + 
            "`data` text not null);");

            con.endRequest();

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

                        for(TeleportContainer cont : OTEMod.TeleportRegistry){
                            if(cont.has_expired())
                            {
                                try{
                                    Component expire = Component.literal(ChatColor.DARK_PURPLE+"Teleport request has expired");
                                    ChatServerOverride.broadcastTo(cont.FromPlayer, expire, OTEMod.THE_SERVER);
                                    ChatServerOverride.broadcastTo(cont.ToPlayer, expire, OTEMod.THE_SERVER);
                                    OTEMod.TeleportRegistry.remove(cont);
                                }catch(Exception e){
                                    break;
                                }
                            }
                        }
                    }

                    OTEMod.LOGGER.info("Tearing down OTEMod teleport queue - The server is going down");
                }
            });
            OTEMod.MasterThread.start();
        } catch (DatabaseConnectionException | SQLException e) {
            e.printStackTrace();

            LOGGER.error("FATAL ERROR\n \n* DATABASE COULD NOT CONNECT *\n* SEE ABOVE STACK TRACE *");

        }
    }
    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onItemPickup(final ItemPickupEvent ev){
        // Remove the expire tag
        if(ev.getStack().getTagElement(OTEMod.ITEM_LIVES_TAG) != null)
        {
            ev.getStack().removeTagKey(OTEMod.ITEM_LIVES_TAG);
            //OTEMod.LOGGER.info("Removed the item expire tag as the item was picked up");
        }
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onItemExpire(final ItemExpireEvent ev)
    {
        CompoundTag ct = ev.getEntity().getItem().getTagElement(OTEMod.ITEM_LIVES_TAG);
        if(ct == null){
            int life =0;
            ct = new CompoundTag();

            ct.putInt("live", life);
            

        }else {
            int life = ct.getInt("live");
            if(life >= OTEServerConfig.ITEM_DESPAWN_TIMER.get()){
                // Item has expired. we should let it die
                //OTEMod.LOGGER.info("Item ["+ev.getEntity().getItem().getDisplayName().getString()+"] has expired");
                ev.setCanceled(false);
                return;
            }
            life++;
            ct.putInt("live", life);
        }


        ev.getEntity().getItem().removeTagKey(OTEMod.ITEM_LIVES_TAG); // Remove just incase it gets duplicated
        ev.getEntity().getItem().addTagElement(OTEMod.ITEM_LIVES_TAG, ct);
        
        
        //ev.setExtraLife(0); // reset the life count
        //OTEMod.LOGGER.info("Item ["+ev.getEntity().getItem().getDisplayName().getString()+"] was given extra life");
        ev.setCanceled(true);
        
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
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

            MenuScreens.register(MenuInitializer.VAULT.get(), VaultScreen::new);
        }
    }

}
