package dev.zontreck.otemod.commands;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
import dev.zontreck.otemod.configs.OTEServerConfig;
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

    public static boolean canUse(String cmd)
    {
        if(!CommandCooldownRegistry.containsKey(cmd)) return true;
        long lastUse = CommandCooldownRegistry.get(cmd);
        switch(cmd)
        {
            case "rtp":
            {
                if(Instant.now().getEpochSecond() > lastUse+Long.parseLong(String.valueOf(OTEServerConfig.RTP_COOLDOWN))){
                    CommandCooldownRegistry.remove(cmd);
                    return true;
                }else return false;
            }
            default:
            {
                CommandCooldownRegistry.remove(cmd);
                return true; // cooldown not yet made
            }
        }

    }
    public static String getRemaining(String string) {
        long now = Instant.now().getEpochSecond();
        if(!CommandCooldownRegistry.containsKey(string))return "0";
        long used = CommandCooldownRegistry.get(string);
        long cmd_time = 0L;
        switch(string)
        {
            case "rtp":
            {
                cmd_time = Long.parseLong(String.valueOf(OTEServerConfig.RTP_COOLDOWN));
                break;
            }
            default:
            {
                cmd_time = 0L;
                break;
            }
        }

        used+=cmd_time;
        long diff = used-now;
        if(diff<0)diff=0L;
        return String.valueOf(diff);
    }
    
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
