package dev.zontreck.otemod.implementation.uncrafting;

import dev.zontreck.otemod.blocks.ModBlocks;
import dev.zontreck.otemod.blocks.entity.CompressionChamberBlockEntity;
import dev.zontreck.otemod.blocks.entity.UncrafterBlockEntity;
import dev.zontreck.otemod.implementation.inits.ModMenuTypes;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class UncrafterMenu extends AbstractContainerMenu
{

    public final UncrafterBlockEntity entity;
    public final Level level;
    public final ContainerData data;

    public UncrafterMenu(int id, Inventory inv, FriendlyByteBuf buf)
    {
        this(id, inv, inv.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(1));
    }

    public UncrafterMenu(int id, Inventory inv, BlockEntity entity, ContainerData data)
    {
        super(ModMenuTypes.UNCRAFTER.get(), id);

        checkContainerSize(inv, 1);

        this.data=data;
        this.level = entity.getLevel();
        this.entity = (UncrafterBlockEntity) entity;


        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).ifPresent(handler->{
            addSlot(new SlotItemHandler(handler, 0, 40,40));
        });

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(handler->{
            addSlot(new SlotItemHandler(handler, 0, 152, 40));
        });


        addDataSlots(data);
    }

    public boolean isCrafting()
    {
        return data.get(0) > 0;
    }

    public int getScaledProgress()
    {
        if(!isCrafting())return 0;
        int progress = this.data.get(0);
        int max = UncrafterBlockEntity.PROCESSING_TICKS;

        int progressArrow = 69;


        if(progress != 0 && max != 0)
        {
            int percent = progress * progressArrow / max;
            return percent;
        }

        return 0;
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
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!

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
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, ModBlocks.UNCRAFTER.get());
    }

    private static final int PLAYER_INVENTORY_FIRST_SLOT_HEIGHT = 80;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_LEFT = 22;
    private static final int PLAYER_HOTBAR_FIRST_SLOT = 139;

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
