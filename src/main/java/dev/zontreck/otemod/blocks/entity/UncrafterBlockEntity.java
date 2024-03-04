package dev.zontreck.otemod.blocks.entity;

import dev.zontreck.otemod.implementation.OutputItemStackHandler;
import dev.zontreck.otemod.implementation.energy.OTEEnergy;
import dev.zontreck.otemod.implementation.uncrafting.UncrafterMenu;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.EnergySyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UncrafterBlockEntity extends BlockEntity implements MenuProvider
{
    public UncrafterBlockEntity(BlockPos position, BlockState state) {
        super(ModEntities.UNCRAFTER.get(), position, state);

        outputSlots = new OutputItemStackHandler(outputItems);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                switch (i)
                {
                    case 0: {
                        return UncrafterBlockEntity.this.progress;
                    }
                    default: return 0;
                }
            }

            @Override
            public void set(int i, int i1) {
                switch (i)
                {
                    case 0: {
                        UncrafterBlockEntity.this.progress = i1;
                    }
                }

            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    protected final ContainerData data;
    protected int progress = 0;

    private static final int ENERGY_REQUIREMENT = 250;

    public static int PROCESSING_TICKS = (3 * 20); // 3 seconds to uncraft

    protected final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ItemStackHandler outputItems = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private ItemStackHandler outputSlots;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItems = LazyOptional.empty();

    private final OTEEnergy ENERGY_STORAGE = new OTEEnergy(ENERGY_REQUIREMENT * 10, ENERGY_REQUIREMENT*2) {
        @Override
        public void onChanged() {

            setChanged();

            ModMessages.sendToAll(new EnergySyncS2CPacket(energy, getBlockPos()));
        }
    };

    @Override
    public Component getDisplayName() {
        return Component.literal("Uncrafting Factory");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new UncrafterMenu(i, inventory, this, data);
    }


    @Override
    public void onLoad()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()->itemHandler);
        lazyOutputItems = LazyOptional.of(()->outputSlots);
        lazyEnergyHandler = LazyOptional.of(()->ENERGY_STORAGE);
    }


    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyOutputItems.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.put("output", outputItems.serializeNBT());
        nbt.putInt("prog", progress);
        nbt.putInt("energy", ENERGY_STORAGE.getEnergyStored());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);

        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        outputItems.deserializeNBT(nbt.getCompound("output"));
        progress = nbt.getInt("prog");
        ENERGY_STORAGE.setEnergy(nbt.getInt("energy"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) // all sides accept power
        {
            return lazyEnergyHandler.cast();
        }
        if(cap == ForgeCapabilities.FLUID_HANDLER)
        {
            //return lazyFluidHandler.cast(); // TODO: Implement a fluid storage, and add a spot for it on the GUI
        }
        if(cap == ForgeCapabilities.ITEM_HANDLER && side == Direction.DOWN)
        {
            return lazyOutputItems.cast();
        }else return lazyItemHandler.cast(); // all sides except bottom of block
    }
    public void doDrop()
    {
        SimpleContainer cont = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            cont.setItem(i, itemHandler.getStackInSlot(i));
        }
        cont = new SimpleContainer(outputItems.getSlots());
        for (int i = 0; i < outputItems.getSlots(); i++) {
            cont.setItem(i, outputItems.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, cont);
    }

    public static void tick(Level lvl, BlockPos pos, BlockState state, UncrafterBlockEntity entity)
    {
        if(lvl.isClientSide())return;


        if(hasRecipe(entity))
        {
            if(!hasEnergy(entity))return; // Halt until sufficient energy has been received
            entity.progress++;
            setChanged(lvl, pos, state);
            drain(entity);

            if(entity.progress >= UncrafterBlockEntity.PROCESSING_TICKS)
            {
                uncraftItem(entity);
            }
        }else {
            if(entity.progress>0){
                entity.resetProgress();
                setChanged(lvl, pos, state);
            }
        }
    }

    private static void drain(UncrafterBlockEntity entity) {
        entity.ENERGY_STORAGE.extractEnergy(ENERGY_REQUIREMENT, false);
    }

    private static boolean hasEnergy(UncrafterBlockEntity entity) {
        return (entity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQUIREMENT);
    }

    private static void uncraftItem(UncrafterBlockEntity entity) {
        if(hasRecipe(entity))
        {
            ItemStack existing = entity.outputItems.getStackInSlot(0);
            existing.setCount(existing.getCount()+1);
            if(existing.is(Items.AIR))
            {
                existing = makeOutputItems(entity.itemHandler.getStackInSlot(0));
            }
            entity.itemHandler.extractItem(0, 1, false);
            entity.outputItems.setStackInSlot(0, existing);

            entity.resetProgress();
        }
    }

    protected static ItemStack makeOutputItems(ItemStack original)
    {
        ItemStack newItem = new ItemStack(original.getItem(),1);
        return newItem;
    }

    private void resetProgress() {
        progress=0;

    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergy(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }

    private static boolean hasRecipe(UncrafterBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for(int i=0;i<entity.itemHandler.getSlots();i++)
        {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        SimpleContainer output = new SimpleContainer(entity.outputItems.getSlots());
        for(int i=0;i<entity.outputItems.getSlots();i++)
        {
            output.setItem(i, entity.outputItems.getStackInSlot(i));
        }


        boolean hasAnItem = !entity.itemHandler.getStackInSlot(0).isEmpty();

        ItemStack result = null;
        if(hasAnItem)
        {
            result = makeOutputItems(entity.itemHandler.getStackInSlot(0));

        }
        return hasAnItem && canInsertIntoOutput(output, result);
    }

    private static boolean canInsertIntoOutput(SimpleContainer inventory, ItemStack result) {
        ItemStack existing = inventory.getItem(0);
        boolean stackCompat = (existing.getMaxStackSize() > existing.getCount());
        boolean sameType = (existing.getItem() == result.getItem());
        boolean outputEmpty = existing.isEmpty();

        if(outputEmpty)return true;
        return (stackCompat && sameType);
    }
}
