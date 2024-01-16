package dev.zontreck.thresholds.commands.zschem;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.integrations.LuckPermsHelper;
import dev.zontreck.thresholds.permissions.Permissions;
import dev.zontreck.thresholds.zschem.MemoryHolder;
import dev.zontreck.thresholds.zschem.MemoryHolder.Container;
import dev.zontreck.thresholds.zschem.StoredBlock;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(play==null)return 1;
        
        if(!LuckPermsHelper.hasGroupOrPerm(play, Permissions.zschem, Permissions.zschem_save)){
            LuckPermsHelper.sendNoPermissionsMessage(play, Permissions.zschem_save, Permissions.zschem);
            return 1;
        }

        if(MemoryHolder.hasPlayerCached(play))
        {
            Container cont = MemoryHolder.getContainer(play);
            if(cont == null)
            {

                saveSchematicUsage(source);
            }else {
                if(cont.Pos1 != ThresholdsMod.ZERO_VECTOR && cont.Pos2 != ThresholdsMod.ZERO_VECTOR)
                {
                    // Lets go!
                    List<StoredBlock> blocks = new ArrayList<StoredBlock>();
                    // First we calculate every vector between pos1 and pos2.
                    // Then we subtract pos1 from the vector to acquire a relative position.
                    // Then we save the block with this relative position to the blocks list
                    // Once serialized, it is then possible to add the position. Note that this makes it impossible to rotate a zschem like with worldedit, but thats usually fine for our usecases. once in-game worldedit can be used to rotate.
                    // TODO: Also- It is possible that a rotational implementation can be added in the future

                    List<Vector3> positions = cont.Pos1.makeCube(cont.Pos2);
                    
                    for (Vector3 vector3 : positions) {
                        Vector3 v3 = vector3.subtract(cont.Pos1);

                        BlockPos current = vector3.asBlockPos();

                        StoredBlock sb = new StoredBlock(current, cont.lvl.getBlockState(current), cont.lvl);
                        sb.setPosition(v3);

                        blocks.add(sb);
                    }

                    CompoundTag savedSchem = new CompoundTag();
                    ListTag lst = new ListTag();
                    for (StoredBlock sBlock : blocks) {
                        lst.add(sBlock.serialize());
                    }
                    savedSchem.put("blocks", lst);

                    
                    Path configDir = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
                    configDir = configDir.resolve("ZSchems");
                    File X = configDir.toFile();
                    if(!X.exists())
                    {
                        X.mkdir();
                    }
                    configDir = configDir.resolve(name+"-zschem.nbt");

                    // Save file!
                    try {
                        NbtIo.writeCompressed(savedSchem, configDir.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro(ThresholdsMod.ThresholdsPrefix +" !Dark_Green!ZSchem saved to disk!"), ThresholdsMod.THE_SERVER);
                    return 0;
                }
            }
        }
        ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro("!Dark_Red! You must first set the positions"), ThresholdsMod.THE_SERVER);

        return 0;
    }

    private static int saveSchematicUsage(CommandSourceStack source)
    {
        String usage = ThresholdsMod.ThresholdsPrefix;
        usage += "!gold! /savezschem [string:name]";
        ServerPlayer play=(ServerPlayer)source.getEntity();
        if(play==null)return 1;
        ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro(usage), ThresholdsMod.THE_SERVER);
        return 0;
    }
}
