package dev.zontreck.otemod.commands.vaults;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.zontreck.otemod.implementation.VaultContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

public class TrashCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("trash").executes(c-> vault(c.getSource())));

        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int vault(CommandSourceStack source) {
        //VaultContainer cont = new VaultContainer(i, source.getPlayer().getUUID());
        //cont.startOpen(source.getPlayer());
        
        VaultContainer container = new VaultContainer(source.getPlayer(), -1);
        
        NetworkHooks.openScreen(source.getPlayer(), new SimpleMenuProvider(container.serverMenu, Component.literal("Trash")));
        
        // Add to the master vault registry
        if(VaultContainer.VAULT_REGISTRY.containsKey(source.getPlayer().getUUID()))VaultContainer.VAULT_REGISTRY.remove(source.getPlayer().getUUID());
        VaultContainer.VAULT_REGISTRY.put(source.getPlayer().getUUID(), container);
        
        return 0;
    }
}
