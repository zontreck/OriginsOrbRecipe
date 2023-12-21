package dev.zontreck.otemod.commands.vaults;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.implementation.Messages;
import dev.zontreck.otemod.implementation.PlayerFirstJoinTag;
import dev.zontreck.otemod.implementation.vault.NoMoreVaultException;
import dev.zontreck.otemod.implementation.vault.StarterContainer;
import dev.zontreck.otemod.implementation.vault.VaultContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

import java.time.Instant;

public class StarterCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("starter").executes(x->openStarterMenu(x.getSource())).then(Commands.argument("player", EntityArgument.player()).executes(x->starterCommand(x.getSource(), EntityArgument.getPlayer(x, "player")))));
    }

    public static int starterCommand(CommandSourceStack ctx, ServerPlayer player)
    {
        if(ctx.hasPermission(ctx.getServer().getOperatorUserPermissionLevel()))
        {
            try {
                vendStarterKit(player);
            } catch (UserProfileNotYetExistsException e) {
                throw new RuntimeException(e);
            }
            return 0;
        }
        return 1;
    }

    public static void vendStarterKit(ServerPlayer player) throws UserProfileNotYetExistsException {
        Profile prof = Profile.get_profile_of(player.getStringUUID());
        PlayerFirstJoinTag PFJT = PlayerFirstJoinTag.now();
        PFJT.LastGiven=0L;

        PFJT.save(prof.NBT);

        OTEMod.checkFirstJoin(player);
    }

    public static int openStarterMenu(CommandSourceStack ctx)
    {
        ServerPlayer player = ctx.getPlayer();
        if(player != null)
        {
            if(player.hasPermissions(ctx.getServer().getOperatorUserPermissionLevel()))
            {
                try {
                    StarterContainer container = new StarterContainer(player);
                    NetworkHooks.openScreen(player, new SimpleMenuProvider(container.serverMenu, Component.literal("Starter Gear")));


                    // Add to the master vault registry
                    if(StarterContainer.VAULT_REGISTRY.containsKey(player.getUUID()))StarterContainer.VAULT_REGISTRY.remove(player.getUUID());
                    StarterContainer.VAULT_REGISTRY.put(player.getUUID(), container);
                } catch (NoMoreVaultException e) {
                    throw new RuntimeException(e);
                }

            }else {
                ChatHelpers.broadcastTo(player, ChatHelpers.macro(Messages.STARTER_FAILURE_PERMISSIONS), ctx.getServer());
            }

            return 0;
        }
        ctx.sendFailure(ChatHelpers.macro(Messages.STARTER_FAILURE_CONSOLE));
        return 1;
    }


    public static void doOpen(ServerPlayer p){

        StarterContainer container;
        try {
            container = new StarterContainer(p);
        } catch (NoMoreVaultException e) {
            ChatHelpers.broadcastTo(p.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+"!Dark_Red!You cannot open anymore vaults. Craft a new vault!"), p.server);
            return;
        }

        NetworkHooks.openScreen(p, new SimpleMenuProvider(container.serverMenu, Component.literal("Starter Gear")));

        // Add to the master vault registry
        if(StarterContainer.VAULT_REGISTRY.containsKey(p.getUUID()))VaultContainer.VAULT_REGISTRY.remove(p.getUUID());
        StarterContainer.VAULT_REGISTRY.put(p.getUUID(), container);
    }
}
