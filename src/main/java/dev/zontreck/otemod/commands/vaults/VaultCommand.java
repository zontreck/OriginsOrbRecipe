package dev.zontreck.otemod.commands.vaults;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.zontreck.otemod.chat.ChatColor;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.implementation.VaultContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

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
