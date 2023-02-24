package dev.zontreck.otemod.commands.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.commands.teleport.TeleportActioner;
import dev.zontreck.otemod.commands.teleport.TeleportContainer;
import dev.zontreck.otemod.database.TeleportDestination;
import dev.zontreck.otemod.implementation.homes.Home;
import dev.zontreck.otemod.implementation.homes.NoSuchHomeException;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.profiles.UserProfileNotYetExistsException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
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
        try{
            ServerPlayer p = ctx.getPlayerOrException();
            Profile prof = Profile.get_profile_of(p.getStringUUID());
            Home home = prof.player_homes.get(homeName);

            TeleportDestination dest = home.destination;
            TeleportActioner.ApplyTeleportEffect(p);
            TeleportContainer cont = new TeleportContainer(p, dest.Position.asMinecraftVector(), dest.Rotation.asMinecraftVector(), dest.getActualDimension());
            TeleportActioner.PerformTeleport(cont);

            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_green!Home found! Wormhole opening now...")), ctx.getServer());
        }catch(CommandSyntaxException e)
        {
            e.printStackTrace();
            return 1;
        }catch(NoSuchHomeException e)
        {
            
            ChatServerOverride.broadcastTo(ctx.getEntity().getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !dark_red!Home not found. Maybe it does not exist?")), ctx.getServer());
            return 0;
        } catch (UserProfileNotYetExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        return 0;
    }
}
