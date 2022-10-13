package dev.zontreck.otemod.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.configs.Profile;
import dev.zontreck.otemod.containers.Vector2;
import dev.zontreck.otemod.containers.Vector3;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

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
        if(!source.isPlayer())
        {
            return 1;
        }

        ServerPlayer p = source.getPlayer();
        Connection con = OTEMod.DB.getConnection();
        try{
            // Begin
            con.beginRequest();
            PreparedStatement pstat = con.prepareStatement("SELECT * FROM `warps`;"); // We want the warp owner, and the warp type, and name. We don't really care about the teleport properties right now, but a future version will show lore on the tooltip to indicate where it goes

            ResultSet rs = pstat.executeQuery();
            int count=0;
            while(rs.next())
            {
                // Lets do it!
                count++;
            }
            rs=pstat.executeQuery();// Reset the query
            ChatServerOverride.broadcastTo(p.getUUID(), Component.literal(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"OTEMOD" + ChatColor.DARK_GRAY+"] "+ChatColor.resetChat() + "There are "+count+" warps available"), source.getServer());

            while(rs.next())
            {
                // This is a warp!
                // Pull the owner profile
                Profile prof = Profile.get_profile_of(rs.getString("owner"));
                String warpName = rs.getString("warpname");
                int warpType = rs.getInt("warptype");
                String appendType = (warpType == 0) ? "standard warp." : "RTP Warp. This has a position randomizer.";

                HoverEvent hover = HoverTip.get(ChatColor.BOLD + ChatColor.DARK_PURPLE + "This warp is a "+appendType);
                ClickEvent click = Clickable.command("/warp "+warpName);
                Style S = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(hover).withClickEvent(click);

                Component warpMsg = Component.literal(ChatColor.GREEN + warpName + ChatColor.resetChat()).withStyle(S);
                
                // Now, display the warp name, along with the warp's owner information
                HoverEvent h2 = HoverTip.get(prof.name_color+prof.nickname+ChatColor.resetChat()+ChatColor.AQUA+" is the owner of this warp");
                S = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(h2);
                Component ownerInfo = Component.literal(ChatColor.GOLD+ "  [Hover to see the warp's info]").withStyle(S);

                // Combine the two
                warpMsg = Component.literal("").append(warpMsg).append(ownerInfo);
                ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), warpMsg, source.getServer());
            }
        }catch (Exception E)
        {
            E.printStackTrace();
        }
        return 0;
    }

}
