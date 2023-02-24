package dev.zontreck.otemod.commands.vaults;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.vault.NoMoreVaultException;
import dev.zontreck.otemod.implementation.vault.Vault;
import dev.zontreck.otemod.implementation.vault.VaultContainer;
import dev.zontreck.otemod.implementation.vault.VaultProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
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
        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(i <0)
        {
            ChatServerOverride.broadcastTo(play.getUUID(), new TextComponent(ChatColor.doColors(OTEMod.OTEPrefix+" !Dark_Red!You can only specify a vault number in the positive range")), source.getServer());
            return 0;
        }
        doOpen(play, i);
        
        
        return 0;
    }

    public static void doOpen(ServerPlayer p, int i){

        VaultContainer container;
        try {
            container = new VaultContainer(p, i);
        } catch (NoMoreVaultException e) {
            ChatServerOverride.broadcastTo(p.getUUID(), new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!You cannot open anymore vaults. Craft a new vault!")), p.server);
            return;
        }
        
        NetworkHooks.openGui(p, new SimpleMenuProvider(container.serverMenu, new TextComponent("Vault "+i)));
        
        // Add to the master vault registry
        if(VaultContainer.VAULT_REGISTRY.containsKey(p.getUUID()))VaultContainer.VAULT_REGISTRY.remove(p.getUUID());
        VaultContainer.VAULT_REGISTRY.put(p.getUUID(), container);
    }
}
