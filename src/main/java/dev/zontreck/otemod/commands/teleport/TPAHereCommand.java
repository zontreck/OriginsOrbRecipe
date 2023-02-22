package dev.zontreck.otemod.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.Clickable;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.configs.Profile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class TPAHereCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("tpahere").executes(c->usage(c.getSource())).then(Commands.argument("player", EntityArgument.player()).executes(c -> tpa(c.getSource(), EntityArgument.getPlayer(c, "player")))));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int tpa(CommandSourceStack source, ServerPlayer serverPlayer) {
        // Send the request to player

        ServerPlayer play = (ServerPlayer)source.getEntity();
        if(serverPlayer == null){
            source.sendFailure(new TextComponent(ChatColor.DARK_RED+"Error: Player not found"));
            return 1;
        }
        if(!OTEMod.DEVELOPER){
            if(play.getUUID() == serverPlayer.getUUID()){
                source.sendFailure(new TextComponent(ChatColor.DARK_RED+"You cannot teleport to yourself!"));
                return 1;
            }
        }
        TeleportContainer cont = new TeleportContainer(serverPlayer.getUUID(), play.getUUID());

        for(TeleportContainer cont2 : OTEMod.TeleportRegistry){
            if(cont2.compareTo(cont)==0){
                ChatServerOverride.broadcastTo(cont.ToPlayer, new TextComponent(ChatColor.DARK_RED+ "You already have a TPA Request active, wait for it to expire, or use the cancel button/command"), source.getServer());
                return 0;
            }else {
                if(cont2.ToPlayer.equals(cont.ToPlayer)){
                    ChatServerOverride.broadcastTo(cont.FromPlayer, new TextComponent(ChatColor.DARK_RED+ "You already have a TPA Request active, wait for it to expire, or use the cancel button/command"), source.getServer());
                    return 0;
                }
            }
        }


        ClickEvent ce = Clickable.command("/tpcancel "+cont.TeleportID.toString());
        HoverEvent he = HoverTip.get(ChatColor.DARK_GREEN+"Cancel this teleport request (Not yet implemented)");

        Style s = Style.EMPTY.withFont(Style.DEFAULT_FONT).withHoverEvent(he).withClickEvent(ce);

        // Send the alerts
        ChatServerOverride.broadcastTo(cont.ToPlayer, new TextComponent(ChatColor.BOLD + ChatColor.DARK_GREEN +"TPA Request Sent! ").append(new TextComponent(ChatColor.BOLD+ChatColor.DARK_GRAY+"["+ChatColor.DARK_RED+"X"+ChatColor.DARK_GRAY+"]").setStyle(s)), serverPlayer.server);


        ce = Clickable.command("/tpaccept "+cont.TeleportID.toString());
        he = HoverTip.get(ChatColor.DARK_GREEN + "Accept tp request");
        ClickEvent ce2 = Clickable.command("/tpdeny "+cont.TeleportID.toString());
        HoverEvent he2 = HoverTip.get(ChatColor.DARK_RED+"Deny this request");
        s = Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(ce).withHoverEvent(he);

        Style s2 = Style.EMPTY.withFont(Style.DEFAULT_FONT).withClickEvent(ce2).withHoverEvent(he2);

        Profile p = Profile.get_profile_of(cont.ToPlayer.toString());
        ChatServerOverride.broadcastTo(cont.FromPlayer, new TextComponent(p.name_color+p.nickname + ChatColor.BOLD + ChatColor.DARK_PURPLE+" is requesting you to teleport to them\n \n").
            append(new TextComponent(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"ACCEPT" + ChatColor.DARK_GRAY+"] ").setStyle(s)).
            append(new TextComponent(ChatColor.DARK_GRAY + "["+ChatColor.DARK_RED+"DENY"+ChatColor.DARK_GRAY+"]").setStyle(s2)), serverPlayer.server);
        
        OTEMod.TeleportRegistry.add(cont);
        return 0;
    }

    private static int usage(CommandSourceStack source) {
        source.sendSuccess(new TextComponent("/tpa USAGE\n\n      "+ChatColor.BOLD + ChatColor.DARK_GRAY+"/tpa "+ChatColor.DARK_RED+"target_player\n"), false);
        return 0;
    }
}
