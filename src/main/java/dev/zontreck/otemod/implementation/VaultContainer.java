package dev.zontreck.otemod.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class VaultContainer
{
    public static Map<UUID, VaultContainer> VAULT_REGISTRY = new HashMap<>();
    public VaultMenu theContainer;
    public ItemStackHandler myInventory;
    public MenuConstructor serverMenu;
    public UUID owner;
    private MinecraftServer server;
    private final int VAULT_NUMBER;
    public final UUID VaultID;
    public VaultContainer(ServerPlayer player, int vaultNum) {
        myInventory = new ItemStackHandler(54); // Vaults have a fixed size at the same as a double chest
        theContainer = new VaultMenu(player.containerCounter+1, player.getInventory(), myInventory, BlockPos.ZERO);
        VaultID = theContainer.VaultMenuID;
        owner = player.getUUID();
        server=player.server;
        serverMenu = theContainer.getServerMenu(myInventory);
        VAULT_NUMBER=vaultNum;
        if(VAULT_NUMBER == -1)return; // Trash ID

        Connection con = OTEMod.DB.getConnection();
        // Check database for vault

        PreparedStatement pstat;
        try {
            con.beginRequest();
            pstat = con.prepareStatement("SELECT * FROM `vaults` WHERE `uuid`=? AND `number`=?;");
            pstat.setString(1, player.getStringUUID());
            pstat.setInt(2, vaultNum);
    
            ResultSet rs = pstat.executeQuery();
            while(rs.next())
            {
                // We have a vault, deserialize the container
                String data = rs.getString("data");
                CompoundTag inv = NbtUtils.snbtToStructure(data);
                myInventory.deserializeNBT(inv);
            }
            con.endRequest();
        } catch (SQLException | CommandSyntaxException e) {
            e.printStackTrace();
        }
        // Container is now ready to be sent to the client!
    }

    public void commit()
    {
        if(VAULT_NUMBER == -1)return; // We have no need to save the trash
        CompoundTag saved = myInventory.serializeNBT();
        ChatServerOverride.broadcastToAbove(owner, Component.literal(ChatColor.BOLD+ChatColor.DARK_GREEN+"Saving the vault's contents..."), server);

        String toSave= NbtUtils.structureToSnbt(saved);

        Connection con = OTEMod.DB.getConnection();
        try{
            con.beginRequest();
            PreparedStatement ps = con.prepareStatement("REPLACE INTO `vaults` (uuid, number, data) VALUES (?,?,?);");
            ps.setString(1, owner.toString());
            ps.setInt(2, VAULT_NUMBER);
            ps.setString(3, toSave);

            boolean has_items = false;
            for (int i = 0; i< myInventory.getSlots(); i++){
                ItemStack IS = myInventory.getStackInSlot(i);
                if(!IS.isEmpty()){
                    has_items=true;
                }
            }

            if(!has_items)
            {
                ps = con.prepareStatement("DELETE FROM `vaults` WHERE uuid=? AND number=?;");
                ps.setString(1, owner.toString());
                ps.setInt(2, VAULT_NUMBER);
            }

            ps.execute();
            con.endRequest();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
