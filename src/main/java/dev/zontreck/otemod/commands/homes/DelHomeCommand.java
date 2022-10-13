package dev.zontreck.otemod.commands.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class DelHomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("rmhome").executes(c->rmHome(c.getSource(), "default")).then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> rmHome(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int rmHome(CommandSourceStack ctx, String homeName)
    {
        // Request homes
//        String homeName = "";
//        CommandSourceStack ctx = ctx2.getSource();
//        homeName = StringArgumentType.getString(ctx2, "nickname");
//        if(homeName==null)return 0;
        
        if(! ctx.isPlayer())
        {
            Style s = Style.EMPTY.withColor(TextColor.parseColor(ChatColor.DARK_RED)).withFont(Style.DEFAULT_FONT);
            
            
            ChatServerOverride.broadcastTo(ctx.getPlayer().getUUID(), Component.translatable("dev.zontreck.otemod.msgs.only_player").withStyle(s), ctx.getServer());
            return 1;
        }
        ServerPlayer p = ctx.getPlayer();
        Connection con = OTEMod.DB.getConnection();
        try {
            con.beginRequest();
            //Statement stat = con.createStatement();


            String SQL = "DELETE FROM `homes` WHERE `user`=? AND `home_name`=?;";

            PreparedStatement pstat = con.prepareStatement(SQL);
            pstat.setString(1, p.getStringUUID());
            pstat.setString(2, homeName);

            pstat.execute();
            
            
            ctx.sendSuccess(MutableComponent.create(new TranslatableContents("dev.zontreck.otemod.msgs.homes.del.success")), true);
            con.endRequest();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ctx.sendFailure(Component.translatable("dev.zontreck.otemod.msgs.homes.del.fail"));
        }

        return 0;
    }
    
}
