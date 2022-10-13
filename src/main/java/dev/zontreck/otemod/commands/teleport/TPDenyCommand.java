package dev.zontreck.otemod.commands.teleport;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class TPDenyCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpdeny").then(Commands.argument("TeleportUUID", StringArgumentType.string()).executes(c->doCancel(c.getSource(), StringArgumentType.getString(c, "TeleportUUID")))));
        
        //executes(c -> doCancel(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int doCancel(CommandSourceStack source, String TPID) {
        UUID teleporter = UUID.fromString(TPID);
        for(TeleportContainer cont : OTEMod.TeleportRegistry){
            if(cont.TeleportID.equals(teleporter)){
                // Canceling!
                Component comp = Component.literal(ChatColor.DARK_GRAY + "["+ ChatColor.DARK_GREEN+ "OTEMOD" + ChatColor.DARK_GRAY+"] " + ChatColor.DARK_PURPLE+"Teleport request was denied");

                ChatServerOverride.broadcastTo(cont.FromPlayer, comp, source.getServer());
                ChatServerOverride.broadcastTo(cont.ToPlayer, comp, source.getServer());

                OTEMod.TeleportRegistry.remove(cont);
                return 0;
            }
        }

        Component comp = Component.literal(ChatColor.DARK_RED+"The teleport was not found, perhaps the request expired or was already cancelled/denied");

        ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), comp, source.getServer());

        return 0;
    }
}
