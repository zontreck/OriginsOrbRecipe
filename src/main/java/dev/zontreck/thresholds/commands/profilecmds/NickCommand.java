package dev.zontreck.thresholds.commands.profilecmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.thresholds.implementation.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class NickCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("nick")
            .executes(c->setchatcolor(c.getSource(), c.getSource().getPlayerOrException().getName().getString()))
            .then(Commands.argument("new_name", StringArgumentType.string())//StringArgumentType.string())
                .executes(c -> setchatcolor(c.getSource(), StringArgumentType.getString(c, "new_name")))// EnumArgument.getS(c, "color")))
            )
        );
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    public static int setchatcolor(CommandSourceStack source, String string) {

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
        p.nickname = string;
        p.commit();
        LibZontreck.PROFILES.put(play.getStringUUID(), p);


        ChatHelpers.broadcastTo(play, ChatHelpers.macro(Messages.NICK_UPDATED), source.getServer());

        return 0;
    }
}
