package dev.zontreck.thresholds.commands;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.commands.items.ShareItemInChatCommand;
import dev.zontreck.thresholds.commands.profilecmds.ChatColorCommand;
import dev.zontreck.thresholds.commands.profilecmds.NameColorCommand;
import dev.zontreck.thresholds.commands.profilecmds.NickCommand;
import dev.zontreck.thresholds.commands.profilecmds.PrefixColorCommand;
import dev.zontreck.thresholds.commands.profilecmds.PrefixCommand;
import dev.zontreck.thresholds.commands.vaults.StarterCommand;
import dev.zontreck.thresholds.commands.vaults.TrashCommand;
import dev.zontreck.thresholds.commands.vaults.VaultCommand;
import dev.zontreck.thresholds.commands.zschem.LoadSchem;
import dev.zontreck.thresholds.commands.zschem.Place;
import dev.zontreck.thresholds.commands.zschem.PlaceAsAir;
import dev.zontreck.thresholds.commands.zschem.SaveSchem;
import dev.zontreck.thresholds.commands.zschem.SetPos1;
import dev.zontreck.thresholds.commands.zschem.SetPos2;
import dev.zontreck.thresholds.configs.ThresholdsServerConfig;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid= ThresholdsMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
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
                if(Instant.now().getEpochSecond() > lastUse+Long.parseLong(String.valueOf(ThresholdsServerConfig.RTP_COOLDOWN))){
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
                cmd_time = Long.parseLong(String.valueOf(ThresholdsServerConfig.RTP_COOLDOWN));
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
    }
}
