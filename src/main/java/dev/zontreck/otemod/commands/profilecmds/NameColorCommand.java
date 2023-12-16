package dev.zontreck.otemod.commands.profilecmds;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.ChatColor.ColorOptions;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;

public class NameColorCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("nick_color")
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
        p.name_color = colorcoded;
        p.commit();
        LibZontreck.PROFILES.put(play.getStringUUID(), p);


        ChatHelpers.broadcastTo(play, ChatHelpers.macro(Messages.NAME_COLOR_UPDATED), source.getServer());

        return 0;
    }
}
