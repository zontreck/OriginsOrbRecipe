package dev.zontreck.otemod.commands.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.implementation.profiles.Profile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TextComponent;

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
        try{
            ServerPlayer p = ctx.getPlayerOrException();
            Profile prof = Profile.get_profile_of(p.getStringUUID());
            prof.player_homes.delete(homeName);

            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors("!dark_green! Home was deleted successfully")), ctx.getServer());
        }catch(Exception e)
        {
            e.printStackTrace();

            ChatServerOverride.broadcastTo(ctx.getEntity().getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors("!dark_red! Home could not be deleted due to an unknown error")), ctx.getServer());
        }
        return 0;
    }
    
}
