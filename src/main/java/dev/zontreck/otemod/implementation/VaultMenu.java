package dev.zontreck.otemod.implementation;

import java.util.Map;
import java.util.UUID;

import dev.zontreck.otemod.implementation.inits.MenuInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class VaultMenu extends AbstractContainerMenu
{
    //private final ContainerLevelAccess containerAccess;
    public final UUID VaultMenuID;

    public VaultMenu (int id, Inventory player)
    {
        this(id, player, new ItemStackHandler(54), BlockPos.ZERO);
    }

    public VaultMenu (int id, Inventory player, IItemHandler slots, BlockPos pos)
    {
        super(MenuInitializer.VAULT.get(), id);
        VaultMenuID=UUID.randomUUID();
        //this.containerAccess = ContainerLevelAccess.create(player.player.level, pos);

        final int slotSize = 16; 
        final int startX = 17;
        final int startY = 133;
        final int hotbarY = 184;
        final int inventoryY = 26;

        for (int row = 0; row < 6; row++)
        {
            for (int column = 0; column < 9; column++)
            {
                addSlot(new SlotItemHandler(slots, row*9 + column, startX+column * slotSize , inventoryY + row * slotSize));
            }
        }

        for (int row=0;row<3;row++)
        {
            for(int col = 0; col< 9; col++)
            {
                addSlot(new Slot(player, 9+row * 9 + col, startX + col * slotSize, startY + row * slotSize));
            }
        }

        for(int col = 0; col<9; col++)
        {
            addSlot(new Slot(player, col, startX + col * slotSize, hotbarY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player play, int index) {
        ItemStack ret = ItemStack.EMPTY;
        final Slot slot = getSlot(index);
        boolean moveStack = false;
        if(slot.hasItem()){
            final ItemStack item = slot.getItem();
            ret = item.copy();

            if(index<54)
            {
                moveStack=moveItemStackTo(item, 54, this.slots.size(), true);
                if(!moveStack) return ItemStack.EMPTY;
            }else {
                moveStack = moveItemStackTo(item, 0, 54, false);
                
                if(!moveStack)return ItemStack.EMPTY;
            }


            if(item.isEmpty()){
                slot.set(ItemStack.EMPTY);
            }else slot.setChanged();
        }

        return ret;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true; // We have no block
    }
    
    public static MenuConstructor getServerMenu (ItemStackHandler inventory){
        return (id, player, play) -> new VaultMenu(id, player, inventory, BlockPos.ZERO);
    }


    public void doCommitAction()
    {
        
        // Locate the Vault in the Vault Registry and commit changes.
        // Search for myself!
        for(Map.Entry<UUID,VaultContainer> e : VaultContainer.VAULT_REGISTRY.entrySet())
        {
            if(e.getValue().VaultID.equals(VaultMenuID))
            {
                e.getValue().commit();
            }
        }
    }

}
