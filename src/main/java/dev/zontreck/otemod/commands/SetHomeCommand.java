package dev.zontreck.otemod.commands;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.math.Vector3d;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.command.TextComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

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
        
        if(! ctx.isPlayer())
        {
            
            ctx.sendFailure(MutableComponent.create( new TranslatableContents("dev.zontreck.otemod.msgs.homes.only_player")));
            return 1;
        }
        ServerPlayer p = ctx.getPlayer();
        Connection con = OTEMod.DB.getConnection();
        try {
            con.beginRequest();
            Statement stat = con.createStatement();
            Vec3 position = p.position();
            Vec2 rot = p.getRotationVector();

            stat.execute("REPLACE INTO `homes` (user, home_name, x, y, z, rot_x, rot_y, dimension) values (\"" + p.getStringUUID() + "\", \""+ homeName + "\", "+String.valueOf(position.x)+", "+String.valueOf(position.y)+", "+String.valueOf(position.z)+", "+String.valueOf(rot.x)+", "+String.valueOf(rot.y)+", \"" + p.getLevel().dimension().location().getNamespace() + ":" + p.getLevel().dimension().location().getPath() + "\");");
            
            
            ctx.sendSuccess(MutableComponent.create(new TranslatableContents("dev.zontreck.otemod.msgs.homes.set.success")), true);
            con.endRequest();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ctx.sendFailure(Component.translatable("dev.zontreck.otemod.msgs.homes.set.fail"));
        }

        return 0;
    }
    
}
