package dev.zontreck.otemod.commands.profilecmds;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.ChatColor.ColorOptions;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.configs.Profile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

public class ChatColorCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("ccolor")
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
        // To code
        String colorcoded = ChatColor.from(string);

        // Get profile
        if(source.getPlayer()==null){
            return 1;
        }
        Profile p = Profile.get_profile_of(source.getPlayer().getStringUUID());
        p.chat_color = colorcoded;
        p.commit();
        OTEMod.PROFILES.put(source.getPlayer().getStringUUID(), p);

        source.sendSuccess(Component.literal(OTEMod.OTEPrefix + " "+ChatColor.DARK_PURPLE + "Your chat color has been updated"), false);

        return 0;
    }
}
