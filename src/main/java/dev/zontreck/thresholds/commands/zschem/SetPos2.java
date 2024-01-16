package dev.zontreck.thresholds.commands.zschem;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.integrations.LuckPermsHelper;
import dev.zontreck.thresholds.permissions.Permissions;
import dev.zontreck.thresholds.zschem.MemoryHolder;
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

        ChatHelpers.broadcastTo(play.getUUID(), Component.literal(ThresholdsMod.ThresholdsPrefix +ChatColor.doColors(" !Dark_Green!Position 2 set!")), ThresholdsMod.THE_SERVER);

        return 0;
    }
}
