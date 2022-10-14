package dev.zontreck.otemod.commands.warps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.commands.teleport.RTPCommand;
import dev.zontreck.otemod.commands.teleport.TeleportActioner;
import dev.zontreck.otemod.commands.teleport.TeleportContainer;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class WarpCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("warp").executes(c-> nowarp(c.getSource())).then(Commands.argument("name", StringArgumentType.string()).executes(c->warp(c.getSource(), StringArgumentType.getString(c, "name")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int warp(CommandSourceStack source, String string) {
        if(!source.isPlayer()){
            
            ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(OTEMod.OTEPrefix + OTEMod.ONLY_PLAYER), source.getServer());
            return 1;
        }

        ServerPlayer p = source.getPlayer();
        Connection con = OTEMod.DB.getConnection();
        String SQL = "";
        try{
            con.beginRequest();

            PreparedStatement pstat;
            SQL = "SELECT * FROM `warps` WHERE `warpname`=?;";
            pstat=con.prepareStatement(SQL);
            pstat.setString(1, string);
            ResultSet rs = pstat.executeQuery();
            // Get the first result
            if(rs.next())
            {
                TeleportDestination dest = new TeleportDestination(NbtUtils.snbtToStructure(rs.getString("teleporter")));

                String dim = dest.Dimension;
                String[] dims = dim.split(":");

                ResourceLocation rl = new ResourceLocation(dims[0], dims[1]);

                ServerLevel dimL  = null;
                for (ServerLevel lServerLevel : p.server.getAllLevels()) {
                    ResourceLocation XL = lServerLevel.dimension().location();

                    if(XL.getNamespace().equals(rl.getNamespace())){
                        if(XL.getPath().equals(rl.getPath())){
                            dimL = lServerLevel;
                        }
                    }
                }

                if(dimL == null)
                {
                    ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.RED+"DIMENSION COULD NOT BE FOUND"), source.getServer());
                    return 1;
                }

                final int type = rs.getInt("warptype");
                final ServerLevel f_dim = dimL;

                Thread tx = new Thread(new Runnable(){
                    public void run(){

                        if(type==1){
                            dest.Position = RTPCommand.findPosition(source.getLevel(), false);
                        }
        
                        TeleportActioner.ApplyTeleportEffect(p);
                        TeleportContainer tc = new TeleportContainer(p, dest.Position.asMinecraftVector(), dest.Rotation.asMinecraftVector(), f_dim);
                        TeleportActioner.PerformTeleport(tc);
                    }
                });
                tx.start();
            }else {
                ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.DARK_RED+"No such warp"), source.getServer());
            }


            con.endRequest();
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private static int nowarp(CommandSourceStack source) {
        ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.DARK_RED + "You must supply the warp name. If you need to know what warps are available, please use the warps command, or click this message.").withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(Clickable.command("/warps"))), source.getServer());
        return 0;
    }

}
