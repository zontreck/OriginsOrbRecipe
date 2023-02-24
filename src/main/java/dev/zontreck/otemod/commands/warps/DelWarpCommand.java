package dev.zontreck.otemod.commands.warps;

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
import dev.zontreck.otemod.implementation.warps.NoSuchWarpException;
import dev.zontreck.otemod.implementation.warps.Warp;
import dev.zontreck.otemod.implementation.warps.WarpsProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class DelWarpCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("delwarp").then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> setWarp(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setWarp(CommandSourceStack source, String string) {
        
        ServerPlayer p = (ServerPlayer)source.getEntity();

        Warp warp;
        try {
            warp = WarpsProvider.WARPS_INSTANCE.getNamedWarp(string);
        } catch (NoSuchWarpException e) {
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!That warp does not exist")), p.server);
            return 0;
        }
        if(p.getUUID().equals(warp.owner) || p.hasPermissions(5))
        {
            try {
                WarpsProvider.WARPS_INSTANCE.delete(WarpsProvider.WARPS_INSTANCE.getNamedWarp(string));
            } catch (NoSuchWarpException e) {
                ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!That warp does not exist")), p.server);
                return 0;
            }
        
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Green!Warp deleted successfully")), p.server);
    
        }else {
            
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!Warp could not be deleted, because you do not own the warp.")), p.server);
        }
    
        return 0;
    }
}
