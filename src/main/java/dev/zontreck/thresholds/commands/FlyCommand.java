package dev.zontreck.thresholds.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.thresholds.ThresholdsMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FlyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("fly").executes(FlyCommand::Fly));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int Fly(CommandContext<CommandSourceStack> ctx2)
    {
        // Request homes
//        String homeName = "";
//        CommandSourceStack ctx = ctx2.getSource();
//        homeName = StringArgumentType.getString(ctx2, "nickname");
//        if(homeName==null)return 0;
        CommandSourceStack ctx = ctx2.getSource();
        
        ServerPlayer p = (ServerPlayer)ctx.getEntity();
        if(p==null)return 1;
        
        if(p.getAbilities().mayfly){
            p.getAbilities().mayfly=false;
            p.getAbilities().flying=false;
            p.onUpdateAbilities();


            ctx.sendSystemMessage(Component.literal(ThresholdsMod.ThresholdsPrefix + ChatColor.DARK_PURPLE + " Your ability to fly has been disabled"));
        }else {
            p.getAbilities().mayfly=true;
            p.onUpdateAbilities();

            ctx.sendSystemMessage(Component.literal(ThresholdsMod.ThresholdsPrefix + ChatColor.DARK_PURPLE + " You can now fly"));
        }

        return 0;
    }
    
}
