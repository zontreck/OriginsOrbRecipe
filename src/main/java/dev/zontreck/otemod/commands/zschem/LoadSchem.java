package dev.zontreck.otemod.commands.zschem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.integrations.LuckPermsHelper;
import dev.zontreck.otemod.permissions.Permissions;
import dev.zontreck.otemod.zschem.MemoryHolder;
import dev.zontreck.otemod.zschem.MemoryHolder.Container;
import dev.zontreck.otemod.zschem.StoredBlock;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public class LoadSchem {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("loadzschem").executes(c-> loadSchematicUsage(c.getSource())).then(Commands.argument("name", StringArgumentType.string()).executes(z->loadSchematic(z.getSource(), StringArgumentType.getString(z, "name")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int loadSchematic(CommandSourceStack source, String name) {
        // Perform sanity checks
        

        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(play==null)return 1;

        if(!LuckPermsHelper.hasGroupOrPerm(play, Permissions.zschem, Permissions.zschem_load)){
            LuckPermsHelper.sendNoPermissionsMessage(play, Permissions.zschem_load, Permissions.zschem);
            return 1;
        }

        if(MemoryHolder.hasPlayerCached(play))
        {
            Container cont = MemoryHolder.getContainer(play);
            if(cont == null)
            {

                loadSchematicUsage(source);
            }else {
                if(cont.Pos1 != OTEMod.ZERO_VECTOR)
                {
                    // Lets go!
                    List<StoredBlock> blocks = new ArrayList<StoredBlock>();
                    // First we calculate every vector between pos1 and pos2.
                    // Then we subtract pos1 from the vector to acquire a relative position.
                    // Then we save the block with this relative position to the blocks list
                    // Once serialized, it is then possible to add the position. Note that this makes it impossible to rotate a zschem like with worldedit, but thats usually fine for our usecases. once in-game worldedit can be used to rotate.
                    // TODO: Also- It is possible that a rotational implementation can be added in the future

                    Path configDir = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
                    configDir = configDir.resolve("ZSchems");
                    File X = configDir.toFile();
                    if(!X.exists())
                    {
                        X.mkdir();
                    }
                    configDir = configDir.resolve(name+"-zschem.nbt");

                    if(configDir.toFile().exists()){
                        CompoundTag CT=new CompoundTag();
                        try {
                            CT = NbtIo.readCompressed(configDir.toFile());

                        } catch (IOException e) {
                            e.printStackTrace();
                            return 1;
                        }

                        ListTag blst = CT.getList("blocks", CompoundTag.TAG_COMPOUND);
                        Iterator<Tag> tags = blst.iterator();
                        while(tags.hasNext())
                        {
                            CompoundTag nxt = (CompoundTag)tags.next();
                            StoredBlock sb = new StoredBlock(nxt);
                            ServerLevel pasteLvl = cont.lvl;
                            sb.updateWorld(pasteLvl);

                            WorldPosition wp = sb.getWorldPosition();
                            Vector3 superPos = cont.Pos1;
                            wp.Position = superPos.add(wp.Position);
                            sb.setPosition(wp.Position);
                            

                            blocks.add(sb);

                        }

                        MemoryHolder.setBlocks(play, blocks);
                        
                    }else {
                        ChatHelpers.broadcastTo(play.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Red!No such ZSchem exists!")), source.getServer());
                        return 0;
                    }
                    

                    ChatHelpers.broadcastTo(play.getUUID(), new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Green!ZSchem loaded from disk!")), OTEMod.THE_SERVER);
                    return 0;
                }
            }
        }
        ChatHelpers.broadcastTo(play.getUUID(), new TextComponent(ChatColor.doColors("!Dark_Red! You must set the first position")), OTEMod.THE_SERVER);

        return 0;
    }

    private static int loadSchematicUsage(CommandSourceStack source)
    {
        String usage = OTEMod.OTEPrefix;
        usage += ChatColor.doColors("!gold! /loadzschem [string:name]");
        ServerPlayer play=(ServerPlayer)source.getEntity();
        if(play==null)return 1;
        ChatHelpers.broadcastTo(play.getUUID(), new TextComponent(usage), OTEMod.THE_SERVER);
        return 0;
    }
}
