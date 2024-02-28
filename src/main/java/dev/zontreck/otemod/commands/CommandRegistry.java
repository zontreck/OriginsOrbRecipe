package dev.zontreck.otemod.commands;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.commands.dims.BuildCommand;
import dev.zontreck.otemod.commands.items.ShareItemInChatCommand;
import dev.zontreck.otemod.commands.profilecmds.ChatColorCommand;
import dev.zontreck.otemod.commands.profilecmds.NameColorCommand;
import dev.zontreck.otemod.commands.profilecmds.NickCommand;
import dev.zontreck.otemod.commands.profilecmds.PrefixColorCommand;
import dev.zontreck.otemod.commands.profilecmds.PrefixCommand;
import dev.zontreck.otemod.commands.vaults.StarterCommand;
import dev.zontreck.otemod.commands.vaults.TrashCommand;
import dev.zontreck.otemod.commands.vaults.VaultCommand;
import dev.zontreck.otemod.commands.zschem.LoadSchem;
import dev.zontreck.otemod.commands.zschem.Place;
import dev.zontreck.otemod.commands.zschem.PlaceAsAir;
import dev.zontreck.otemod.commands.zschem.SaveSchem;
import dev.zontreck.otemod.commands.zschem.SetPos1;
import dev.zontreck.otemod.commands.zschem.SetPos2;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=OTEMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {
    public static Map<String,Long> CommandCooldownRegistry = new HashMap<>();

    public static void markUsed(String cmd)
    {
        // Command was used, mark the current time
        CommandCooldownRegistry.put(cmd, Instant.now().getEpochSecond());
    }
    
    @SubscribeEvent
    public void onRegisterCommands(final RegisterCommandsEvent ev)
    {

        ChatColorCommand.register(ev.getDispatcher());
        NameColorCommand.register(ev.getDispatcher());
        PrefixColorCommand.register(ev.getDispatcher());
        PrefixCommand.register(ev.getDispatcher());
        NickCommand.register(ev.getDispatcher());
        


        VaultCommand.register(ev.getDispatcher());
        TrashCommand.register(ev.getDispatcher());

        SetPos1.register(ev.getDispatcher());
        SetPos2.register(ev.getDispatcher());
        SaveSchem.register(ev.getDispatcher());
        LoadSchem.register(ev.getDispatcher());
        Place.register(ev.getDispatcher());
        PlaceAsAir.register(ev.getDispatcher());

        ShareItemInChatCommand.register(ev.getDispatcher());
        StarterCommand.register(ev.getDispatcher());

        BuildCommand.register(ev.getDispatcher());
    }
}
