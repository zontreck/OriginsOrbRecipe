package dev.zontreck.otemod.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.PlayerFlyCache;
import dev.zontreck.otemod.configs.Profile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=OTEMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ChatServerOverride {

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onJoin(final PlayerEvent.PlayerLoggedInEvent ev)
    {
        //Player joined, send custom alert
        
        // Download user data from database
        try{
            Connection c = OTEMod.DB.getConnection();
            String SQL = "SELECT * FROM `profiles` WHERE `uuid`=?;";
            PreparedStatement pst = c.prepareStatement(SQL);
            pst.setString(1,ev.getEntity().getStringUUID());

            ResultSet rs = pst.executeQuery();
            boolean has_profile=false;
            while(rs.next())
            {
                has_profile=true;

                
                OTEMod.PROFILES.put(ev.getEntity().getStringUUID(), new Profile(rs.getString("username"), rs.getString("prefix"), rs.getString("nickname"), rs.getString("name_color"), ev.getEntity().getStringUUID(), rs.getString("prefix_color"), rs.getString("chat_color")));
            }

            if(!has_profile)
            {
                // Create profile!
                ServerPlayer play = (ServerPlayer)ev.getEntity();
                Profile p = Profile.factory(play);
                OTEMod.PROFILES.put(play.getStringUUID(), p);
                p.commit(); // Commits the profile to the server

                ev.getEntity().displayClientMessage(Component.literal(ChatColor.BOLD+ ChatColor.DARK_GRAY + "["+ChatColor.DARK_GREEN + "OTEMOD" + ChatColor.DARK_GRAY + "] "+ChatColor.DARK_GREEN + "First join! Your server profile has been created"), false);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        Profile prof = Profile.get_profile_of(ev.getEntity().getStringUUID());
        if(prof == null){
            OTEMod.LOGGER.error("FATAL: Profile was null for "+ev.getEntity().getName().getString());
            return;
        }
        
        ChatServerOverride.broadcast(Component.literal(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "+" + ChatColor.DARK_GRAY + "] "+ ChatColor.BOLD + ChatColor.DARK_AQUA + prof.nickname), ev.getEntity().getServer());
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onLeave(final PlayerEvent.PlayerLoggedOutEvent ev)
    {
        // Get player profile, send disconnect alert, then commit profile and remove it from memory
        Profile px = Profile.get_profile_of(ev.getEntity().getStringUUID());

        if(px==null)return;

        // Send the alert
        ChatServerOverride.broadcast(Component.literal(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] "+ChatColor.BOLD + ChatColor.DARK_AQUA + px.nickname), ev.getEntity().getServer());

        px.commit();
        OTEMod.PROFILES.remove(ev.getEntity().getStringUUID());
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onClone(final PlayerEvent.Clone ev)
    {
        // Fix for fly ability not copying to new instance on death or other circumstances
        Player old = ev.getOriginal();
        Player n = ev.getEntity();

        PlayerFlyCache c = PlayerFlyCache.cachePlayer((ServerPlayer)old);
        c.Assert((ServerPlayer)n);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onChat(final ServerChatEvent ev){
        // Player has chatted, apply override
        ServerPlayer sp = ev.getPlayer();
        // Get profile
        Profile XD = Profile.get_profile_of(sp.getStringUUID());
        
        // Override the chat!
        String prefixStr = "";
        if(XD.prefix != ""){
            prefixStr = ChatColor.DARK_GRAY + "[" + ChatColor.BOLD + XD.prefix_color + XD.prefix + ChatColor.resetChat() + ChatColor.DARK_GRAY + "] ";
        }

        String msg = ev.getMessage().getString();
        msg= ChatColor.doColors(msg);

        String nameStr = ChatColor.resetChat() + "< "+ XD.name_color + XD.nickname + ChatColor.resetChat() + " >";
        String message = ": "+XD.chat_color + msg;
        Style hover = Style.EMPTY;
        hover=hover.withFont(Style.DEFAULT_FONT).withHoverEvent(HoverTip.get(ChatColor.MINECOIN_GOLD+"User Name: "+XD.username));
        ev.setCanceled(true);

        ChatServerOverride.broadcast(Component.literal(prefixStr+nameStr+message).setStyle(hover), ev.getPlayer().server);
    }


    public static void broadcastAbove(Component message, MinecraftServer s)
    {
        s.execute(new Runnable() {
            public void run(){

                // This will broadcast to all players
                for(ServerPlayer play : s.getPlayerList().getPlayers())
                {
                    play.displayClientMessage(message, true); // Send the message!
                }
                LogToConsole(Component.literal("[all] ").append(message));
            }
        });
    }
    public static void LogToConsole(Component Message)
    {
        OTEMod.LOGGER.info(Message.getString());
    }
    public static void broadcast(Component message, MinecraftServer s)
    {
        s.execute(new Runnable() {
            public void run(){

                // This will broadcast to all players
                for(ServerPlayer play : s.getPlayerList().getPlayers())
                {
                    play.displayClientMessage(message, false); // Send the message!
                }
                LogToConsole(Component.literal("[all] ").append(message));
            }
        });
    }
    public static void broadcastTo(UUID ID, Component message, MinecraftServer s)
    {
        s.execute(new Runnable() {
            public void run(){

                ServerPlayer play = s.getPlayerList().getPlayer(ID);
                play.displayClientMessage(message, false); // Send the message!
                
                LogToConsole(Component.literal("[server] -> ["+play.getName().toString()+"] ").append(message));
            }
        });
    }
    public static void broadcastToAbove(UUID ID, Component message, MinecraftServer s)
    {
        s.execute(new Runnable() {
            public void run(){

                ServerPlayer play = s.getPlayerList().getPlayer(ID);
                play.displayClientMessage(message, true); // Send the message!
                
                LogToConsole(Component.literal("[server] -> ["+play.getName().toString()+"] ").append(message));
            }
        });
    }
}
