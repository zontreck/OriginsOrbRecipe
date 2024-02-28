package dev.zontreck.otemod.commands.dims;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.events.TeleportEvent;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.configs.snbt.ServerConfig;
import dev.zontreck.otemod.implementation.Messages;
import dev.zontreck.otemod.registry.ModDimensions;
import dev.zontreck.otemod.registry.PerPlayerDataRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class BuildCommand
{
    private enum Options {
        enter,
        leave
    }
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        var cmd = Commands.literal("builder");
        cmd.then(Commands.literal("enter").executes(c->run(c.getSource(), Options.enter)));
        cmd.then(Commands.literal("leave").executes(c->run(c.getSource(), Options.leave)));
        dispatcher.register(cmd);
    }

    public static int run(CommandSourceStack stack, Options direction)
    {
        if(stack.isPlayer())
        {
            ServerPlayer sp = stack.getPlayer();
            boolean playerIsOp = sp.hasPermissions(stack.getServer().getOperatorUserPermissionLevel());

            if(playerIsOp || ServerConfig.general.allowBuilder)
            {

                if(direction == Options.enter)
                {
                    WorldPosition save = new WorldPosition(sp);
                    PerPlayerDataRegistry.put(sp.getUUID(), "builder_entered_from", save.serialize());

                    WorldPosition pos = new WorldPosition(new Vector3(0, -55, 0), ModDimensions.BUILDER_DIM());

                    if(!MinecraftForge.EVENT_BUS.post(new TeleportEvent(pos, sp)))
                        stack.getPlayer().teleportTo(pos.getActualDimension(), pos.Position.x, pos.Position.y, pos.Position.z, 0, 0);

                    return 0;
                } else if(direction == Options.leave) {

                    CompoundTag tag = (CompoundTag) PerPlayerDataRegistry.get(sp.getUUID(), "builder_entered_from");
                    if(tag != null)
                    {
                        try {
                            WorldPosition pos = new WorldPosition(tag, false);

                            if(!MinecraftForge.EVENT_BUS.post(new TeleportEvent(pos, sp)))
                                sp.teleportTo(pos.getActualDimension(), pos.Position.x, pos.Position.y, pos.Position.z, 0,0);
                        } catch (InvalidDeserialization e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        ChatHelpers.broadcastTo(stack.getPlayer(), ChatHelpers.macro("!Dark_Red!There is not a cached position. did you logout, or the server restart? Use a home or warp command instead."), stack.getServer());
                    }

                    return 0;
                } else return 0;
            }else {
                ChatHelpers.broadcastTo(sp, ChatHelpers.macro(Messages.BUILDER_DIMENSION_DISALLOWED), stack.getServer());

                return 0;
            }
        }else {
            stack.sendFailure(ChatHelpers.macro(Messages.CONSOLE_ERROR));
            return -1;
        }
    }
}
