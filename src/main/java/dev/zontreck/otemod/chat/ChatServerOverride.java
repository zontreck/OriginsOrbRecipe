package dev.zontreck.otemod.chat;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.events.ProfileLoadedEvent;
import dev.zontreck.libzontreck.events.ProfileUnloadingEvent;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.configs.PlayerFlyCache;
import dev.zontreck.otemod.enchantments.ModEnchantments;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=OTEMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ChatServerOverride {

    @SubscribeEvent
    public void onJoin(final ProfileLoadedEvent ev)
    {
        //Player joined, send custom alert
        ServerPlayer play = ev.player;
        Profile prof = ev.profile;
        

        if(prof.flying)
        {
            play.getAbilities().flying=true;
            play.onUpdateAbilities();
        }

        Abilities playerAbilities = play.getAbilities();
        boolean mayFly = false;
        ItemStack feet = play.getItemBySlot(EquipmentSlot.FEET);
        if(ItemUtils.getEnchantmentLevel(ModEnchantments.FLIGHT_ENCHANTMENT.get(), feet)>0)mayFly = true;

        playerAbilities.mayfly=mayFly;
        PlayerFlyCache c = PlayerFlyCache.cachePlayer(play);
        c.Flying=prof.flying;
        c.FlyEnabled = mayFly;
        c.Assert(play);

        OTEMod.checkFirstJoin(ev.player);

        if(!OTEServerConfig.USE_CUSTOM_JOINLEAVE.get()) return;
        
        ChatHelpers.broadcast(ChatHelpers.macro("!Dark_Gray![!Dark_Green!+!Dark_Gray!] !Bold!!Dark_Aqua![0]",prof.nickname), ev.level.getServer());
        
    }

    @SubscribeEvent
    public void onLeave(ProfileUnloadingEvent ev)
    {
        // Get player profile, send disconnect alert, then commit profile and remove it from memory
        Profile px=ev.profile;


        if(px==null)return;

        if(!OTEServerConfig.USE_CUSTOM_JOINLEAVE.get()) return;

        // Send the alert
        ChatHelpers.broadcast(ChatHelpers.macro("!Dark_Gray![!Dark_Red!-!Dark_Gray!] !Bold!!Dark_Aqua![0]", px.nickname), px.player.server);


        px.flying=px.player.getAbilities().flying;
        ev.setCanceled(true);
    }

    @SubscribeEvent
    public void onClone(final PlayerEvent.Clone ev)
    {
        if(ev.getEntity().level().isClientSide)return;
        // Fix for fly ability not copying to new instance on death or other circumstances
        Player old = ev.getOriginal();
        Player n = ev.getEntity();

        PlayerFlyCache c = PlayerFlyCache.cachePlayer((ServerPlayer)old);
        c.Assert((ServerPlayer)n);
    }

    @SubscribeEvent
    public void onChat(final ServerChatEvent ev){
        if(ev.getPlayer().level().isClientSide)return;
        // Player has chatted, apply override
        if(!OTEServerConfig.USE_CUSTOM_CHATREPLACER.get()) return;

        
        ServerPlayer sp = ev.getPlayer();
        // Get profile
        Profile XD=null;
        try {
            XD = Profile.get_profile_of(sp.getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Override the chat!
        String prefixStr = "";
        if(XD.prefix != ""){
            prefixStr = ChatColor.DARK_GRAY + "[" + ChatColor.BOLD + XD.prefix_color + XD.prefix + ChatColor.resetChat() + ChatColor.DARK_GRAY + "] ";
        }

        String msg = ev.getRawText();
        msg= ChatColor.doColors(msg);

        String nameStr = ChatColor.resetChat() + "< "+ XD.name_color + XD.nickname + ChatColor.resetChat() + " >";
        String message = ": "+XD.chat_color + msg;
        Style hover = Style.EMPTY;
        hover=hover.withFont(Style.DEFAULT_FONT).withHoverEvent(HoverTip.get(ChatColor.MINECOIN_GOLD+"User Name: "+XD.username));
        ev.setCanceled(true);

        ChatHelpers.broadcast(Component.literal(prefixStr + nameStr + message).setStyle(hover), ev.getPlayer().server);
    }


}
