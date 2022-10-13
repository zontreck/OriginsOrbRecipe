package dev.zontreck.otemod.commands.homes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class HomesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("homes").executes(HomesCommand::getHomes));
    }

    private static int getHomes(CommandContext<CommandSourceStack> ctx)
    {
        // Request homes
        if(! ctx.getSource().isPlayer())
        {
            
            ChatServerOverride.broadcastTo(ctx.getSource().getPlayer().getUUID(), Component.literal(ChatColor.DARK_RED).append(Component.translatable("dev.zontreck.otemod.msgs.only_player")), ctx.getSource().getServer());
            return 1;
        }
        ServerPlayer p = ctx.getSource().getPlayer();
        Connection con = OTEMod.DB.getConnection();
        try {
            con.beginRequest();
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT `home_name` FROM `homes` WHERE `user`=\"" + p.getStringUUID()+"\"");
            List<String> homes = new ArrayList<String>();
            while(rs.next()){
                homes.add(rs.getString("home_name"));
            }
            
            ctx.getSource().sendSuccess(MutableComponent.create(new TranslatableContents("dev.zontreck.otemod.msgs.homes.total")).append(""+String.valueOf(homes.size())), true);
            con.endRequest();

            for (String string : homes) {
                Style st = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(HoverTip.get(ChatColor.BOLD+ChatColor.DARK_GREEN+"Click here to go to this home")).withClickEvent(Clickable.command("/home "+string));

                ChatServerOverride.broadcastTo(ctx.getSource().getPlayer().getUUID(), Component.literal(ChatColor.BOLD + ChatColor.MINECOIN_GOLD+"["+ChatColor.resetChat()+ChatColor.UNDERLINE+ChatColor.BOLD+ChatColor.DARK_GREEN+"HOME"+ChatColor.resetChat()+ChatColor.BOLD+ChatColor.MINECOIN_GOLD+"] "+ChatColor.resetChat()+ChatColor.YELLOW+string).setStyle(st), ctx.getSource().getServer());
                
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }
    
}
