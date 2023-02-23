package dev.zontreck.otemod.commands.items;

import com.mojang.brigadier.CommandDispatcher;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.configs.Profile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;

public class ShareItemInChatCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("shareitem").executes(c->share_item(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int share_item(CommandSourceStack source) {
        
        if(source.getEntity() instanceof Player)
        {
            ServerPlayer play = (ServerPlayer)source.getEntity();
            ItemStack is = play.getItemInHand(InteractionHand.MAIN_HAND);
            if(is.getItem() instanceof AirItem)
            {
                play.displayClientMessage(new TextComponent(OTEMod.OTEPrefix+ChatColor.doColors(" !Dark_Red!You cannot share air in the chat.")), false);
                return 0;
            }

            Profile prof = Profile.get_profile_of(play.getUUID().toString());

            MutableComponent component = new TextComponent(OTEMod.OTEPrefix).append(is.getDisplayName()).append(new TextComponent(ChatColor.doColors(" !White!-!Dark_Purple! Hover here to see the item that "+prof.name_color+prof.nickname+"!Dark_Purple! shared")));
            Style style = Style.EMPTY.withFont(Style.DEFAULT_FONT);
            component = component.withStyle(style.withHoverEvent(HoverTip.getItem(is)));

            ChatServerOverride.broadcast(component, OTEMod.THE_SERVER);

        }else {
            return 1;
        }

        return 0;
    }
}
