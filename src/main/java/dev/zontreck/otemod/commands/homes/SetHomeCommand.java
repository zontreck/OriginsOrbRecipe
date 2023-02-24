package dev.zontreck.otemod.commands.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector2;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.database.TeleportDestination;
import dev.zontreck.otemod.implementation.homes.Home;
import dev.zontreck.otemod.implementation.homes.Homes;
import dev.zontreck.otemod.implementation.homes.HomesProvider;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.profiles.UserProfileNotYetExistsException;
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
        
        ServerPlayer p;
        try {
            p = ctx.getPlayerOrException();
            Vec3 position = p.position();
            Vec2 rot = p.getRotationVector();
    
            TeleportDestination dest = new TeleportDestination(new Vector3(position), new Vector2(rot), p.getLevel());
    
            Home newhome = new Home(p, homeName, dest);
            Profile profile = Profile.get_profile_of(p.getStringUUID());
            Homes homes = profile.player_homes;
            homes.add(newhome);
            
                
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_green!Home was created or updated successfully!")), ctx.getServer());
        } catch (CommandSyntaxException e) {
            
            ChatServerOverride.broadcastTo(ctx.getEntity().getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_red!Home could not be created or updated!")), ctx.getServer());
            e.printStackTrace();
        } catch (UserProfileNotYetExistsException e) {
            ChatServerOverride.broadcastTo(ctx.getEntity().getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_red!Home could not be created or updated!")), ctx.getServer());
            e.printStackTrace();
        }
        

        return 0;
    }
    
}
