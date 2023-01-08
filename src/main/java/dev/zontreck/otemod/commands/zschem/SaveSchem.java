package dev.zontreck.otemod.commands.zschem;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.zschem.MemoryHolder;
import dev.zontreck.otemod.zschem.MemoryHolder.Container;
import dev.zontreck.otemod.zschem.StoredBlock;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class SaveSchem {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("savezschem").executes(c-> saveSchematicUsage(c.getSource())).then(Commands.argument("name", StringArgumentType.string()).executes(z->saveSchematic(z.getSource(), StringArgumentType.getString(z, "name")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int saveSchematic(CommandSourceStack source, String name) {
        // Perform sanity checks
        if(!source.isPlayer())return 1;

        ServerPlayer play = source.getPlayer();
        if(play==null)return 1;
        if(MemoryHolder.hasPlayerCached(play))
        {
            Container cont = MemoryHolder.get(play);
            if(cont == null)
            {

                saveSchematicUsage(source);
            }else {
                if(cont.Pos1 != null && cont.Pos2 != null)
                {
                    // Lets go!
                    List<StoredBlock> blocks = new ArrayList<StoredBlock>();
                    // First we calculate every vector between pos1 and pos2.
                    // Then we subtract pos1 from the vector to acquire a relative position.
                    // Then we save the block with this relative position to the blocks list
                    // Once serialized, it is then possible to add the position. Note that this makes it impossible to rotate a zschem like with worldedit, but thats usually fine for our usecases. once in-game worldedit can be used to rotate.
                    // TODO: Also- It is possible that a rotational implementation can be added in the future
                }
            }
        }
        ChatServerOverride.broadcastTo(play.getUUID(), Component.literal(ChatColor.doColors("!Dark_Red! You must first set the positions")), OTEMod.THE_SERVER);

        return 0;
    }

    private static int saveSchematicUsage(CommandSourceStack source)
    {
        String usage = OTEMod.OTEPrefix;
        usage += ChatColor.doColors("!gold! /savezschem [string:name]");
        ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(usage), OTEMod.THE_SERVER);
        return 0;
    }
}
