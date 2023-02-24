package dev.zontreck.otemod.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.profiles.UserProfileNotYetExistsException;
import dev.zontreck.otemod.implementation.warps.Warp;
import dev.zontreck.otemod.implementation.warps.WarpsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class WarpsCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("warps").executes(c-> warps(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int warps(CommandSourceStack source) {
        

        ServerPlayer p = (ServerPlayer)source.getEntity();
    
        Map<String, Warp> warps = WarpsProvider.WARPS_INSTANCE.get();
        
        ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + " "+ChatColor.resetChat() + "There are "+warps.size()+" warps available"), source.getServer());

        Iterator<Entry<String, Warp>> it = warps.entrySet().iterator();
        while(it.hasNext())
        {
            // TODO: Implement public and private. Private requires an ACL be implemented. New GUI
            Warp warp = it.next().getValue();
            // Pull the owner profile
            Profile prof=null;
            try {
                prof = Profile.get_profile_of(warp.owner.toString());
            } catch (UserProfileNotYetExistsException e) {
                e.printStackTrace();
                return 1;
            }
            String warpName = warp.WarpName;
            int warpType = 0;
            if(warp.RTP) warpType=1;
            
            String appendType = (warpType == 0) ? "standard warp." : "RTP Warp. This has a position randomizer.";

            HoverEvent hover = HoverTip.get(ChatColor.BOLD + ChatColor.DARK_PURPLE + "This warp is a "+appendType);
            ClickEvent click = Clickable.command("/warp "+warpName);
            Style S = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(hover).withClickEvent(click);

            Component warpMsg = new TextComponent(ChatColor.GREEN + warpName + ChatColor.resetChat()).withStyle(S);
            
            // Now, display the warp name, along with the warp's owner information
            HoverEvent h2 = HoverTip.get(prof.name_color+prof.nickname+ChatColor.resetChat()+ChatColor.AQUA+" is the owner of this warp");
            S = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(h2);
            Component ownerInfo = new TextComponent(ChatColor.GOLD+ "  [Hover to see the warp's info]").withStyle(S);

            // Combine the two
            warpMsg = new TextComponent("").append(warpMsg).append(ownerInfo);
            ChatServerOverride.broadcastTo(p.getUUID(), warpMsg, source.getServer());
        }
        
        return 0;
    }

}
