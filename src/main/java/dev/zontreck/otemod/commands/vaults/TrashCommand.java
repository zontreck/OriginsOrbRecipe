package dev.zontreck.otemod.commands.vaults;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.vault.NoMoreVaultException;
import dev.zontreck.otemod.implementation.vault.VaultContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
        ServerPlayer play = (ServerPlayer)source.getEntity();

        VaultContainer container;
        try {
            container = new VaultContainer(play, -1);
        } catch (NoMoreVaultException e) {
            ChatHelpers.broadcastTo(play.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Red!You cannot open anymore vaults. Craft a new vault!"), play.server);
            return 0;
        }
        
        NetworkHooks.openGui(play, new SimpleMenuProvider(container.serverMenu, ChatHelpers.macro("Trash")));
        
        // Add to the master vault registry
        if(VaultContainer.VAULT_REGISTRY.containsKey(play.getUUID()))VaultContainer.VAULT_REGISTRY.remove(play.getUUID());
        VaultContainer.VAULT_REGISTRY.put(play.getUUID(), container);
        
        return 0;
    }
}
