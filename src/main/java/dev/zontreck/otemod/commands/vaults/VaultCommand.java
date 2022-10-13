package dev.zontreck.otemod.commands.vaults;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.zontreck.otemod.implementation.VaultContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

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
        //VaultContainer cont = new VaultContainer(i, source.getPlayer().getUUID());
        //cont.startOpen(source.getPlayer());
        
        VaultContainer container = new VaultContainer(source.getPlayer(), i);
        
        NetworkHooks.openScreen(source.getPlayer(), new SimpleMenuProvider(container.serverMenu, Component.literal("Vault "+i)));
        
        // Add to the master vault registry
        if(VaultContainer.VAULT_REGISTRY.containsKey(source.getPlayer().getUUID()))VaultContainer.VAULT_REGISTRY.remove(source.getPlayer().getUUID());
        VaultContainer.VAULT_REGISTRY.put(source.getPlayer().getUUID(), container);
        
        return 0;
    }
}
