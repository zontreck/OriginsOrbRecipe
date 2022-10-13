package dev.zontreck.otemod.commands.profilecmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.Profile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class PrefixCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("prefix")
            .executes(c->setchatcolor(c.getSource(), "Member"))
            .then(Commands.argument("new_prefix", StringArgumentType.string())//StringArgumentType.string())
                .executes(c -> setchatcolor(c.getSource(), StringArgumentType.getString(c, "new_prefix")))// EnumArgument.getS(c, "color")))
            )
        );
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    public static int setchatcolor(CommandSourceStack source, String string) {

        // Get profile
        if(source.getPlayer()==null){
            source.sendFailure(Component.literal(ChatColor.DARK_RED+"Only a player can use this command"));
            return 1;
        }
        Profile p = Profile.get_profile_of(source.getPlayer().getStringUUID());
        p.prefix = string;
        p.commit();
        OTEMod.PROFILES.put(source.getPlayer().getStringUUID(), p);

        source.sendSuccess(Component.literal(ChatColor.DARK_GRAY+ "["+ChatColor.DARK_GREEN+ "OTEMOD" + ChatColor.DARK_GRAY + "] "+ChatColor.DARK_PURPLE + "Your prefix has been updated"), false);

        return 0;
    }
}
