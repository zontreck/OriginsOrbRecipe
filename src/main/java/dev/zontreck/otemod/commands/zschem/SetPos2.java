package dev.zontreck.otemod.commands.zschem;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.zschem.MemoryHolder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

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
        MemoryHolder.setPos2(source.getPlayer(), new Vector3(source.getPosition()));

        return 0;
    }
}
