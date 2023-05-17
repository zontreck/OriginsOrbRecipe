package dev.zontreck.otemod.commands.zschem;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.integrations.LuckPermsHelper;
import dev.zontreck.otemod.permissions.Permissions;
import dev.zontreck.otemod.zschem.MemoryHolder;
import dev.zontreck.otemod.zschem.MemoryHolder.Container;
import dev.zontreck.otemod.zschem.StoredBlock;
import dev.zontreck.otemod.zschem.WorldProp;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// This command will place the loaded schematic in world. The schematic will originate from position 1. The positions are relative and are added onto position 1.
public class PlaceAsAir {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("zsetair").executes(c-> place(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int place(CommandSourceStack source) {

        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(play==null)return 1;

        if(!LuckPermsHelper.hasGroupOrPerm(play, Permissions.zschem, Permissions.zschem_air)){
            LuckPermsHelper.sendNoPermissionsMessage(play, Permissions.zschem_air, Permissions.zschem);
            return 1;
        }

        if(!MemoryHolder.hasPlayerCached(play)){

            ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Red!You must first set the positions!"), OTEMod.THE_SERVER);

            return 1;
        }

        Container cont = MemoryHolder.getContainer(play);

        if(cont.Pos1 != OTEMod.ZERO_VECTOR && cont.Pos2 != OTEMod.ZERO_VECTOR)
        {
            WorldProp system = WorldProp.acquire(cont.lvl);
            // Begin the process
            List<Vector3> positions = cont.Pos1.makeCube(cont.Pos2);
            Collections.shuffle(positions);
            Iterator<Vector3> v3 = positions.iterator();
            
            while(v3.hasNext())
            {
                Vector3 pos = v3.next();
                StoredBlock sb = new StoredBlock(pos.asBlockPos(), Blocks.AIR.defaultBlockState(), source.getLevel());
                system.customEnqueue(sb);
            }

            
        }else {
            
            ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Red!You must first set the positions!"), OTEMod.THE_SERVER);

            return 1;
        }


        ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Green!Enqueued!"), OTEMod.THE_SERVER);

        return 0;
    }
}
