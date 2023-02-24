package dev.zontreck.otemod.implementation.vault;

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
import dev.zontreck.otemod.implementation.events.VaultModifiedEvent;
import dev.zontreck.otemod.implementation.profiles.Profile;
import dev.zontreck.otemod.implementation.profiles.UserProfileNotYetExistsException;
import dev.zontreck.otemod.implementation.vault.VaultProvider.VaultAccessStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.ItemStackHandler;

public class VaultContainer
{
    public static Map<UUID, VaultContainer> VAULT_REGISTRY = new HashMap<>();
    public VaultMenu theContainer;
    public ItemStackHandler myInventory;
    public ItemStackHandler startingInventory;
    public MenuConstructor serverMenu;
    public UUID owner;
    private MinecraftServer server;
    public final int VAULT_NUMBER;
    public final UUID VaultID;
    public Vault main_accessor;
    public VaultContainer(ServerPlayer player, int vaultNum) throws NoMoreVaultException {
        myInventory = new ItemStackHandler(54); // Vaults have a fixed size at the same as a double chest
        startingInventory = new ItemStackHandler(64);
        theContainer = new VaultMenu(player.containerCounter+1, player.getInventory(), myInventory, BlockPos.ZERO, player, vaultNum);
        VaultID = theContainer.VaultMenuID;
        owner = player.getUUID();
        server=player.server;
        serverMenu = theContainer.getServerMenu(myInventory, vaultNum);
        VAULT_NUMBER=vaultNum;
        if(VAULT_NUMBER == -1)return; // Trash ID

        // Check database for vault
        VaultAccessStrategy strategy;
        try {
            strategy = VaultProvider.check(Profile.get_profile_of(player.getStringUUID()), vaultNum);
            if(strategy == VaultAccessStrategy.CREATE || strategy == VaultAccessStrategy.OPEN)
            {
                Vault accessor = VaultProvider.get(Profile.get_profile_of(player.getStringUUID()), vaultNum);
                if(accessor.isNew)
                {
                    main_accessor=accessor;
                    return;
                }else {
                    myInventory.deserializeNBT(accessor.getContents());
                    startingInventory.deserializeNBT(accessor.getContents());
                }
                main_accessor=accessor;
            }else {
                // DENY
                throw new NoMoreVaultException("No more vaults can be created", vaultNum);
            }
        } catch (UserProfileNotYetExistsException e) {
            throw new NoMoreVaultException("User profile not exists. No vault can be opened or created", -9999);
        }

        // Container is now ready to be sent to the client!
    }

    public void commit()
    {
        if(VAULT_NUMBER == -1)return; // We have no need to save the trash
        boolean isEmpty=true;
        CompoundTag saved = myInventory.serializeNBT();
        ChatServerOverride.broadcastToAbove(owner, new TextComponent(ChatColor.BOLD+ChatColor.DARK_GREEN+"Saving the vault's contents..."), server);

        Profile profile=null;
        try {
            profile = Profile.get_profile_of(owner.toString());
        } catch (UserProfileNotYetExistsException e) {
            e.printStackTrace();
            return;
        }

        for(int i = 0;i<myInventory.getSlots();i++)
        {
            ItemStack is = myInventory.getStackInSlot(i);
            if(!is.is(Items.AIR))
            {
                isEmpty=false;
            }
        }


        if(!isEmpty)
            main_accessor.setContents(saved);
        else
            main_accessor.delete();

        
        VaultModifiedEvent vme = new VaultModifiedEvent(VAULT_NUMBER, profile, VaultProvider.getInUse(profile), myInventory, startingInventory);
        OTEMod.bus.post(vme);
    }

}
