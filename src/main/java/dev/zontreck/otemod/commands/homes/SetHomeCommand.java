package dev.zontreck.otemod.commands.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TextComponent;

public class SetHomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("sethome").executes(c->setHome(c.getSource(), "default")).then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> setHome(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setHome(CommandSourceStack ctx, String homeName)
    {
        // Request homes
//        String homeName = "";
//        CommandSourceStack ctx = ctx2.getSource();
//        homeName = StringArgumentType.getString(ctx2, "nickname");
//        if(homeName==null)return 0;
        
        if(!(ctx.getEntity() instanceof Player))
        {
            
            return 1;
        }
        ServerPlayer p = (ServerPlayer)ctx.getEntity();
        Connection con = OTEMod.DB.getConnection();
        try {
            con.beginRequest();
            //Statement stat = con.createStatement();
            Vec3 position = p.position();
            Vec2 rot = p.getRotationVector();

            TeleportDestination dest = new TeleportDestination(new Vector3(position), new Vector2(rot), p.getLevel());

            String SQL = "REPLACE INTO `homes` (user, home_name, teleporter) VALUES (?, ?, ?);";
            PreparedStatement pstat = con.prepareStatement(SQL);

            pstat.setString(1, p.getStringUUID());
            pstat.setString(2, homeName);
            pstat.setString(3, dest.toString());

            pstat.execute();
            
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_green!Home was created or updated successfully!")), ctx.getServer());
            
            con.endRequest();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_red!Home could not be updated or created for a unknown reason!")), ctx.getServer());
            return 1;
        }

        return 0;
    }
    
}
