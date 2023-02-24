package dev.zontreck.otemod.commands.homes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.implementation.homes.Home;
import dev.zontreck.otemod.implementation.homes.Homes;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.profiles.UserProfileNotYetExistsException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

public class HomesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("homes").executes(HomesCommand::getHomes));
    }

    private static int getHomes(CommandContext<CommandSourceStack> ctx)
    {
        // Request homes
        try{
            ServerPlayer player = ctx.getSource().getPlayerOrException();

            Profile p = Profile.get_profile_of(player.getStringUUID());
            Homes homes = p.player_homes;

            ChatServerOverride.broadcastTo(player.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Purple!There are !gold!"+String.valueOf(homes.count())+" !dark_purple!total homes.")), player.server);

            
            for (Home string : homes.getList()) {
                Style st = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(HoverTip.get(ChatColor.BOLD+ChatColor.DARK_GREEN+"Click here to go to this home")).withClickEvent(Clickable.command("/home "+string.homeName));

                ChatServerOverride.broadcastTo(player.getUUID(), new TextComponent(ChatColor.BOLD + ChatColor.MINECOIN_GOLD+"["+ChatColor.resetChat()+ChatColor.UNDERLINE+ChatColor.BOLD+ChatColor.DARK_GREEN+"HOME"+ChatColor.resetChat()+ChatColor.BOLD+ChatColor.MINECOIN_GOLD+"] "+ChatColor.resetChat()+ChatColor.YELLOW+string).setStyle(st), ctx.getSource().getServer());
                
            }
        }catch(CommandSyntaxException ex)
        {
            ex.printStackTrace();

        } catch (UserProfileNotYetExistsException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
}
