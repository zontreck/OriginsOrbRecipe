package dev.zontreck.otemod.commands;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.commands.profilecmds.ChatColorCommand;
import dev.zontreck.otemod.commands.profilecmds.NameColorCommand;
import dev.zontreck.otemod.commands.profilecmds.NickCommand;
import dev.zontreck.otemod.commands.profilecmds.PrefixColorCommand;
import dev.zontreck.otemod.commands.profilecmds.PrefixCommand;
import dev.zontreck.otemod.commands.teleport.RTPCommand;
import dev.zontreck.otemod.commands.teleport.TPACommand;
import dev.zontreck.otemod.commands.teleport.TPAHereCommand;
import dev.zontreck.otemod.commands.teleport.TPAcceptCommand;
import dev.zontreck.otemod.commands.teleport.TPCancelCommand;
import dev.zontreck.otemod.commands.teleport.TPDenyCommand;
import dev.zontreck.otemod.commands.vaults.VaultCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=OTEMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {
    
    @SubscribeEvent
    public void onRegisterCommands(final RegisterCommandsEvent ev)
    {
        HomesCommand.register(ev.getDispatcher());
        SetHomeCommand.register(ev.getDispatcher());
        HomeCommand.register(ev.getDispatcher());
        DelHomeCommand.register(ev.getDispatcher());

        FlyCommand.register(ev.getDispatcher());

        ChatColorCommand.register(ev.getDispatcher());
        NameColorCommand.register(ev.getDispatcher());
        PrefixColorCommand.register(ev.getDispatcher());
        PrefixCommand.register(ev.getDispatcher());
        NickCommand.register(ev.getDispatcher());

        TPACommand.register(ev.getDispatcher());
        TPCancelCommand.register(ev.getDispatcher());
        TPDenyCommand.register(ev.getDispatcher());
        TPAcceptCommand.register(ev.getDispatcher());
        TPAHereCommand.register(ev.getDispatcher());
        RTPCommand.register(ev.getDispatcher());


        VaultCommand.register(ev.getDispatcher());
    }
    
}
