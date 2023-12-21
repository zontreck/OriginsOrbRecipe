package dev.zontreck.otemod.implementation.vault;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.implementation.events.VaultModifiedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StarterContainer
{
    public static Map<UUID, StarterContainer> VAULT_REGISTRY = new HashMap<>();
    public StarterMenu theContainer;
    public ItemStackHandler myInventory;
    public ItemStackHandler startingInventory;
    public MenuConstructor serverMenu;
    public UUID owner;
    private MinecraftServer server;
    public final UUID VaultID;
    public Starter main_accessor;
    public StarterContainer(ServerPlayer player) throws NoMoreVaultException {
        myInventory = new ItemStackHandler(54); // Vaults have a fixed size at the same as a double chest
        startingInventory = new ItemStackHandler(64);
        theContainer = new StarterMenu(player.containerCounter+1, player.getInventory(), myInventory, BlockPos.ZERO, player);
        VaultID = theContainer.VaultMenuID;
        owner = player.getUUID();
        server=player.server;
        serverMenu = theContainer.getServerMenu(myInventory);

        // Check database for vault
        StarterProvider.VaultAccessStrategy strategy;

        strategy = StarterProvider.check();
        if(strategy == StarterProvider.VaultAccessStrategy.CREATE || strategy == StarterProvider.VaultAccessStrategy.OPEN)
        {
            Starter accessor = StarterProvider.getStarter();
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
            throw new NoMoreVaultException("No more vaults can be created", 0);
        }

        // Container is now ready to be sent to the client!
    }

    public void commit()
    {
        boolean isEmpty=true;
        CompoundTag saved = myInventory.serializeNBT();
        ChatHelpers.broadcastToAbove(owner, Component.literal(ChatColor.BOLD+ChatColor.DARK_GREEN+"Saving the starter kit's contents..."), server);

        main_accessor.setLastChanged(Instant.now().getEpochSecond());

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

        
        VaultModifiedEvent vme = new VaultModifiedEvent(-2, profile, VaultProvider.getInUse(profile), myInventory, startingInventory);
        OTEMod.bus.post(vme);
    }

}
