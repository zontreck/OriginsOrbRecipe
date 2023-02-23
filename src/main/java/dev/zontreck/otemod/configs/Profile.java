package dev.zontreck.otemod.configs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.server.level.ServerPlayer;

public class Profile {
    public String username;
    public String user_id;
    public String prefix;
    public String nickname;
    public String name_color; // ChatColor.X
    public String prefix_color;
    public String chat_color;
    public Boolean flying;
    public int available_vaults;

    public Profile(String username, String prefix, String nickname, String name_color, String ID, String prefix_color, String chat_color, Boolean isFlying, int vaults) {
        this.username = username;
        this.prefix = prefix;
        this.nickname = nickname;
        this.name_color = name_color;
        this.user_id = ID;
        this.prefix_color = prefix_color;
        this.chat_color = chat_color;
        this.flying=isFlying;
        this.available_vaults=vaults;
    }


    public static Profile get_profile_of(String UUID)
    {
        if(OTEMod.PROFILES.containsKey(UUID)){
            return OTEMod.PROFILES.get(UUID);
        }else {
            // profile does not exist!
            // how can this happen?
            return null;
        }
    }

    public static Profile factory(ServerPlayer play)
    {
        Profile p = new Profile(play.getName().getString(), "Member", play.getDisplayName().getString(), ChatColor.GREEN, play.getStringUUID(), ChatColor.AQUA, ChatColor.WHITE, false, 0);
        return p;
    }

    public void commit()
    {
        // Send player to server!
        Connection con = OTEMod.DB.getConnection();
        String SQL = "REPLACE INTO `profiles` (username, uuid, prefix, nickname, name_color, prefix_color, chat_color, flying, vaults) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement pstat = con.prepareStatement(SQL);
            pstat.setString(1, username);
            pstat.setString(2, user_id);
            pstat.setString(3, prefix);
            pstat.setString(4, nickname);
            pstat.setString(5, name_color);
            pstat.setString(6, prefix_color);
            pstat.setString(7, chat_color);
            pstat.setBoolean(8, flying);
            pstat.setInt(9, available_vaults);

            pstat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
