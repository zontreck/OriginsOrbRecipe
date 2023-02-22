package dev.zontreck.otemod.commands.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.exceptions.InvalidSideException;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.commands.teleport.TeleportActioner;
import dev.zontreck.otemod.commands.teleport.TeleportContainer;
import dev.zontreck.otemod.database.TeleportDestination;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

public class HomeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("home").executes(c-> home(c.getSource(), "default")).then(Commands.argument("nickname", StringArgumentType.string()).executes(c -> home(c.getSource(), StringArgumentType.getString(c, "nickname")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int home(CommandSourceStack ctx, String homeName)
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
        String SQL="";
        try {
            con.beginRequest();
            Statement stat = con.createStatement();
            Vec3 position = p.position();
            
            Vec2 rot = p.getRotationVector();
            
            //stat.execute("REPLACE INTO `homes` (user, home_name, x, y, z, rot_x, rot_y, dimension) values (\"" + p.getStringUUID() + "\", \""+ homeName + "\", "+String.valueOf(position.x)+", "+String.valueOf(position.y)+", "+String.valueOf(position.z)+", "+String.valueOf(rot.x)+", "+String.valueOf(rot.y)+", \"" + p.getLevel().dimension().location().getNamespace() + ":" + p.getLevel().dimension().location().getPath() + "\");");
            // Query database now
            SQL = "SELECT * FROM `homes` WHERE `user`=? AND `home_name`=?;";
            //ResultSet rs = stat.executeQuery(SQL);
            
            PreparedStatement pstat = con.prepareStatement(SQL);
            pstat.setString(1, p.getStringUUID());
            pstat.setString(2, homeName);

            ResultSet rs = pstat.executeQuery();

            boolean has_home = false;
            while(rs.next()){
                has_home=true;
                // Now, begin to extract the home data
                TeleportDestination dest = new TeleportDestination(NbtUtils.snbtToStructure(rs.getString("teleporter")));
                


                position = dest.Position.asMinecraftVector();
                rot = dest.Rotation.asMinecraftVector();

                ServerLevel dimL=null;
                try {
                    dimL = (ServerLevel)dest.getActualDimension();
                } catch (InvalidSideException e) {
                    e.printStackTrace();
                    return 1;
                }

                TeleportActioner.ApplyTeleportEffect(p);
                // Instantiate a Teleport Runner

                final ServerPlayer f_p = p;
                final Vec3 f_pos = position;
                final Vec2 f_rot = rot;
                final ServerLevel f_dim = dimL;
                TeleportContainer cont = new TeleportContainer(f_p, f_pos, f_rot, f_dim);
                TeleportActioner.PerformTeleport(cont);
            }

            if(!has_home)throw new SQLException("NO HOME");
            
            Style sxx = Style.EMPTY.withColor(TextColor.parseColor(ChatColor.DARK_GREEN)).withFont(Style.DEFAULT_FONT);
            

            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_green!Home found! Wormhole opening now...")), ctx.getServer());
            con.endRequest();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(!e.getMessage().equals("%%"))
                ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Red! Could not go to the home")), ctx.getServer());
            else
                ctx.sendFailure(new TextComponent("FAILED SQL: "+ ChatColor.GOLD+ SQL));
        } catch (InvalidDeserialization e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CommandSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }
}
