package dev.zontreck.otemod.commands.dims;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.implementation.Messages;
import dev.zontreck.otemod.registry.ModDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BuildCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("builder").executes(c->run(c.getSource())));
    }

    public static int run(CommandSourceStack stack)
    {
        if(stack.isPlayer())
        {
            boolean playerIsOp = stack.getPlayer().hasPermissions(stack.getServer().getOperatorUserPermissionLevel());

            if(playerIsOp || OTEServerConfig.ALLOW_BUILDER_DIM.get())
            {

                WorldPosition pos = new WorldPosition(new Vector3(0, -55, 0), ModDimensions.BUILDER_DIM());

                stack.getPlayer().teleportTo(pos.getActualDimension(), pos.Position.x, pos.Position.y, pos.Position.z, 0, 0);

                return 0;
            }else {
                ChatHelpers.broadcastTo(stack.getPlayer(), ChatHelpers.macro(Messages.BUILDER_DIMENSION_DISALLOWED), stack.getServer());

                return 0;
            }
        }else {
            stack.sendFailure(ChatHelpers.macro(Messages.CONSOLE_ERROR));
            return -1;
        }
    }
}
