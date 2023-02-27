package dev.zontreck.otemod.commands.profilecmds;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.ChatColor.ColorOptions;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.profiles.UserProfileNotYetExistsException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;

public class ChatColorCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("chat_color")
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
        if(!(source.getEntity() instanceof Player)){
            return 1;
        }
        ServerPlayer play = (ServerPlayer)source.getEntity();
        Profile p;
        try {
            p = Profile.get_profile_of(play.getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            return 1;
        }
        p.chat_color = colorcoded;
        p.commit();
        OTEMod.PROFILES.put(play.getStringUUID(), p);

        source.sendSuccess(new TextComponent(OTEMod.OTEPrefix + " "+ChatColor.DARK_PURPLE + "Your chat color has been updated"), false);

        return 0;
    }
}
