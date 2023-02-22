package dev.zontreck.otemod.integrations;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class LuckPermsHelper {
    /*public static LuckPerms getLuckPerms()
    {
        return LuckPermsProvider.get();
    }

    private static User getUserOf(Player p){
        return getLuckPerms().getPlayerAdapter(Player.class).getUser(p);
    }

    private static boolean hasPermission(User u, String perm)
    {
        return u.getCachedData().getPermissionData().checkPermission(perm).asBoolean();
    }*/
    
    public static boolean hasPermission(Player p, String perm)
    {
        //User u = getUserOf(p);
        
        return true;
    }

    public static boolean hasGroupOrPerm(Player p, String group, String perm){
        return hasPermission(p, group) || hasPermission(p, perm);
    }

    public static void sendNoPermissionsMessage(ServerPlayer play, String perm, String group) {
        ChatServerOverride.broadcastTo(play.getUUID(), new TextComponent(OTEMod.OTEPrefix + ChatColor.doColors(" !Dark_Red!You do not have permission to use that command. You need permission !Gold!"+perm+" !White! or !Gold!"+group)), play.server);
    }
}
