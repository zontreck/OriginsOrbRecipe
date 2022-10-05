package dev.zontreck.otemod.commands.profilecmds;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatColor;
import dev.zontreck.otemod.chat.ChatColor.ColorOptions;
import dev.zontreck.otemod.configs.Profile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

public class PrefixColorCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("pcolor")
            .executes(c->setchatcolor(c.getSource(), ColorOptions.White))
            .then(Commands.argument("color", EnumArgument.enumArgument(ChatColor.ColorOptions.class))//StringArgumentType.string())
                .executes(c -> setchatcolor(c.getSource(), c.getArgument("color", ChatColor.ColorOptions.class)))// EnumArgument.getS(c, "color")))
            )
        );
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    public static int setchatcolor(CommandSourceStack source, ColorOptions string) {

        // Chat Color has a registry of colors that we can use to map back to our desired color
        String actual_color = string.toString();
        // To code
        String colorcoded = ChatColor.from(actual_color);

        // Get profile
        if(source.getPlayer()==null){
            source.sendFailure(Component.literal(ChatColor.DARK_RED+"Only a player can use this command"));
            return 1;
        }
        Profile p = Profile.get_profile_of(source.getPlayer().getStringUUID());
        p.name_color = colorcoded;
        p.commit();
        OTEMod.PROFILES.put(source.getPlayer().getStringUUID(), p);

        source.sendSuccess(Component.literal(ChatColor.DARK_GRAY+ "["+ChatColor.DARK_GREEN+ "OTEMOD" + ChatColor.DARK_GRAY + "] "+ChatColor.DARK_PURPLE + "Your prefix color has been updated"), false);

        return 0;
    }
}
