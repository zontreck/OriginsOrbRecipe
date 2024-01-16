package dev.zontreck.thresholds.commands.items;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.chat.HoverTip;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.thresholds.ThresholdsMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
                play.displayClientMessage(ChatHelpers.macro(ThresholdsMod.ThresholdsPrefix +" !Dark_Red!You cannot share air in the chat."), false);
                return 0;
            }

            Profile prof;
            try {
                prof = Profile.get_profile_of(play.getUUID().toString());
            } catch (UserProfileNotYetExistsException e) {
                return 1;
            }

            MutableComponent component = ChatHelpers.macro(ThresholdsMod.ThresholdsPrefix).append(is.getDisplayName()).append(ChatHelpers.macro(" !White!-!Dark_Purple! Hover here to see the item that "+prof.name_color+prof.nickname+"!Dark_Purple! shared"));
            Style style = Style.EMPTY.withFont(Style.DEFAULT_FONT);
            component = component.withStyle(style.withHoverEvent(HoverTip.getItem(is)));

            ChatHelpers.broadcast(component, ThresholdsMod.THE_SERVER);

        }else {
            return 1;
        }

        return 0;
    }
}
