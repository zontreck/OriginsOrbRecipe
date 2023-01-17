package dev.zontreck.otemod.implementation.vault;

import java.util.Map;
import java.util.UUID;

import dev.zontreck.otemod.implementation.inits.ModMenuTypes;
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
        super(ModMenuTypes.VAULT.get(), id);
        VaultMenuID=UUID.randomUUID();
        //this.containerAccess = ContainerLevelAccess.create(player.player.level, pos);

        final int slotSize = 18; 
        final int startX = 24;
        final int inventoryY = 38;

        addPlayerInventory(player);
        addPlayerHotbar(player);


        for (int row = 0; row < 6; row++)
        {
            for (int column = 0; column < 9; column++)
            {
                addSlot(new SlotItemHandler(slots, row*9 + column, startX+column * slotSize , inventoryY + row * slotSize));
            }
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 54;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
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

    
    private static final int PLAYER_INVENTORY_FIRST_SLOT_HEIGHT = 156;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_LEFT = 24;
    private static final int PLAYER_HOTBAR_FIRST_SLOT = 212;

    private void addPlayerInventory(Inventory inv)
    {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j+i*9+9, PLAYER_INVENTORY_FIRST_SLOT_LEFT+j*18, PLAYER_INVENTORY_FIRST_SLOT_HEIGHT+i*18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv)
    {
        for (int index = 0; index < 9; index++) {
            this.addSlot(new Slot(inv, index, PLAYER_INVENTORY_FIRST_SLOT_LEFT+index*18, PLAYER_HOTBAR_FIRST_SLOT));
        }
    }

}
