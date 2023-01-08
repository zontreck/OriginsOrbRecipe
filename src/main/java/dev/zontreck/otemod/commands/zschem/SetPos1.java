package dev.zontreck.otemod.commands.zschem;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.zschem.MemoryHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetPos1 {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("zpos1").executes(c-> setzPos1(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setzPos1(CommandSourceStack source) {

        ServerPlayer play = source.getPlayer();
        if(play==null)return 1;

        MemoryHolder.setPos1(play, new Vector3(source.getPosition()));
        MemoryHolder.setLevel(play, source.getLevel());

        ChatServerOverride.broadcastTo(play.getUUID(), Component.literal(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Green!Position 1 set!")), OTEMod.THE_SERVER);

        return 0;
    }
}
