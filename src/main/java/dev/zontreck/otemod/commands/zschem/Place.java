package dev.zontreck.otemod.commands.zschem;

import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.integrations.LuckPermsHelper;
import dev.zontreck.otemod.permissions.Permissions;
import dev.zontreck.otemod.zschem.MemoryHolder;
import dev.zontreck.otemod.zschem.StoredBlock;
import dev.zontreck.otemod.zschem.WorldProp;
import dev.zontreck.otemod.zschem.MemoryHolder.Container;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

// This command will place the loaded schematic in world. The schematic will originate from position 1. The positions are relative and are added onto position 1.
public class Place {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("placezschem").executes(c-> place(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int place(CommandSourceStack source) {

        ServerPlayer play = source.getPlayer();
        if(play==null)return 1;

        if(!LuckPermsHelper.hasGroupOrPerm(play, Permissions.zschem, Permissions.zschem_place)){
            LuckPermsHelper.sendNoPermissionsMessage(play, Permissions.zschem_place, Permissions.zschem);
            return 1;
        }
        
        if(!MemoryHolder.hasPlayerCached(play)){

            ChatServerOverride.broadcastTo(play.getUUID(), Component.literal(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!You must first load the zschem!")), OTEMod.THE_SERVER);

            return 1;
        }

        Container cont = MemoryHolder.getContainer(play);
        List<StoredBlock> blocks = cont.blocks;
        Collections.shuffle(blocks);

        if(cont.Pos1 != OTEMod.ZERO_VECTOR)
        {
            WorldProp system = WorldProp.acquire(cont.lvl);
            // Begin the process
            for (StoredBlock storedBlock : blocks) {
                // alter the stored block and send it off to the queue system for the relevant world!
                
                system.customEnqueue(storedBlock);
            }
            
        }else {
            
            ChatServerOverride.broadcastTo(play.getUUID(), Component.literal(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!You must first load the zschem!")), OTEMod.THE_SERVER);

            return 1;
        }


        ChatServerOverride.broadcastTo(play.getUUID(), Component.literal(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Green!Enqueued!")), OTEMod.THE_SERVER);

        return 0;
    }
}
