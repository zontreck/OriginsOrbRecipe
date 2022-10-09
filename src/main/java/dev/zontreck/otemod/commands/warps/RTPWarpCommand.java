package dev.zontreck.otemod.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatColor;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.containers.Vector2;
import dev.zontreck.otemod.containers.Vector3;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class RTPWarpCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("rtpwarp").then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> setWarp(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setWarp(CommandSourceStack source, String string) {
        
        if(! source.isPlayer())
        {
            
            ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.DARK_RED).append(Component.translatable("dev.zontreck.otemod.msgs.only_player")), source.getServer());
            return 1;
        }
        ServerPlayer p = source.getPlayer();
        Connection con = OTEMod.DB.getConnection();
        try {
            con.beginRequest();
            PreparedStatement pstat;
            Vec3 position = p.position();
            Vec2 rot = p.getRotationVector();

            TeleportDestination dest = new TeleportDestination(new Vector3(position), new Vector2(rot), p.getLevel().dimension().location().getNamespace() + ":" + p.getLevel().dimension().location().getPath());

            String SQL = "REPLACE INTO `warps` (warpname, owner, warptype, teleporter) values (?, ?, ?, ?);";
            pstat = con.prepareStatement(SQL);
            pstat.setString(1, string);
            pstat.setString(2, p.getStringUUID());
            pstat.setInt(3, 1);
            pstat.setString(4, dest.toString());
            pstat.execute();
            

            ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.GREEN).append(Component.translatable("dev.zontreck.otemod.msgs.warps.set.success")), source.getServer());

            con.endRequest();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.DARK_RED).append(Component.translatable("dev.zontreck.otemod.msgs.warps.set.fail")), source.getServer());
        }

        return 0;
    }
}
