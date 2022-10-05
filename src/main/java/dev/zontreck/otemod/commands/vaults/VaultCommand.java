package dev.zontreck.otemod.commands.vaults;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.zontreck.otemod.chat.ChatColor;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.containers.VaultContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class VaultCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("pv").executes(c-> vault(c.getSource(), 0)).then(Commands.argument("number", IntegerArgumentType.integer()).executes(c -> vault(c.getSource(), IntegerArgumentType.getInteger(c, "number")))));
        dispatcher.register(Commands.literal("vault").executes(c-> vault(c.getSource(), 0)).then(Commands.argument("number", IntegerArgumentType.integer()).executes(c -> vault(c.getSource(), IntegerArgumentType.getInteger(c, "number")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int vault(CommandSourceStack source, int i) {
        VaultContainer cont = new VaultContainer(i, source.getPlayer().getUUID());
        cont.startOpen(source.getPlayer());

        ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.literal(ChatColor.DARK_RED + "Vaults are not yet implemented"), source.getServer());
        return 0;
    }
}
