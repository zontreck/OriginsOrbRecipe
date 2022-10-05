package dev.zontreck.otemod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import dev.zontreck.otemod.chat.ChatColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

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
        if(! ctx.isPlayer())
        {
            
            ctx.sendFailure(MutableComponent.create( new TranslatableContents("dev.zontreck.otemod.msgs.homes.only_player")));
            return 1;
        }
        ServerPlayer p = ctx.getPlayer();
        
        if(p.getAbilities().mayfly){
            p.getAbilities().mayfly=false;
            p.getAbilities().flying=false;
            p.onUpdateAbilities();
            
            ctx.sendSuccess(Component.literal(ChatColor.BOLD + "[" + ChatColor.DARK_GREEN + "OTEMOD" + ChatColor.WHITE + "] "+ChatColor.resetChat() + ChatColor.DARK_PURPLE + "Your ability to fly has been disabled"), false);
        }else {
            p.getAbilities().mayfly=true;
            p.onUpdateAbilities();

            ctx.sendSuccess(Component.literal(ChatColor.BOLD + "[" + ChatColor.DARK_GREEN + "OTEMOD" + ChatColor.WHITE + "] "+ChatColor.resetChat() + ChatColor.DARK_PURPLE + "You can now fly"), false);
        }

        return 0;
    }
    
}