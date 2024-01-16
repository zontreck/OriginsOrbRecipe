package dev.zontreck.otemod.commands.zschem;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.integrations.LuckPermsHelper;
import dev.zontreck.otemod.permissions.Permissions;
import dev.zontreck.otemod.zschem.MemoryHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetPos2 {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("zpos2").executes(c-> setzPos2(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int setzPos2(CommandSourceStack source) {
        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(play==null)return 1;

        if(!LuckPermsHelper.hasGroupOrPerm(play, Permissions.zschem, Permissions.zschem_pos2)){
            LuckPermsHelper.sendNoPermissionsMessage(play, Permissions.zschem_pos2, Permissions.zschem);
            return 1;
        }
        
        MemoryHolder.setPos2(play, new Vector3(source.getPosition()));
        MemoryHolder.setLevel(play, source.getLevel());

        ChatHelpers.broadcastTo(play.getUUID(), Component.literal(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Green!Position 2 set!")), OTEMod.THE_SERVER);

        return 0;
    }
}
