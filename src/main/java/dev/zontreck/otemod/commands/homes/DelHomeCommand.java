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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

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
            

            ChatServerOverride.broadcastTo(ctx.getPlayer().getUUID(), Component.literal(OTEMod.OTEPrefix + ChatColor.doColors("!dark_green! Home was deleted successfully")), ctx.getServer());

            con.endRequest();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ChatServerOverride.broadcastTo(ctx.getPlayer().getUUID(), Component.literal(OTEMod.OTEPrefix + ChatColor.doColors("!dark_red! Home was unable to be deleted")), ctx.getServer());
            return 1;
        }

        return 0;
    }
    
}
