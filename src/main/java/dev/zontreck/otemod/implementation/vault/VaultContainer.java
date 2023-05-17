package dev.zontreck.otemod.implementation.vault;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.events.VaultModifiedEvent;
import dev.zontreck.otemod.implementation.vault.VaultProvider.VaultAccessStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        ChatHelpers.broadcastToAbove(owner, Component.literal(ChatColor.BOLD+ChatColor.DARK_GREEN+"Saving the vault's contents..."), server);

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
