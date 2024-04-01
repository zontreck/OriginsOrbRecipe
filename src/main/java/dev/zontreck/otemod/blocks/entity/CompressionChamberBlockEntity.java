package dev.zontreck.otemod.blocks.entity;

import dev.zontreck.libzontreck.items.InputItemStackHandler;
import dev.zontreck.libzontreck.items.OutputItemStackHandler;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.implementation.compressor.CompressionChamberMenu;
import dev.zontreck.otemod.implementation.energy.IThresholdsEnergy;
import dev.zontreck.otemod.implementation.energy.OTEEnergy;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.EnergySyncS2CPacket;
import dev.zontreck.otemod.recipe.CompressionChamberRecipe;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

public class CompressionChamberBlockEntity extends BlockEntity implements MenuProvider, IThresholdsEnergy
{

    private boolean EnergyDirty=true;
    private int TickCount=0;

    public CompressionChamberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.COMPRESSION_CHAMBER.get(), pPos, pBlockState);

        outputSlot = new OutputItemStackHandler(outputItems);
        inputSlot = new InputItemStackHandler(itemsHandler);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> CompressionChamberBlockEntity.this.progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch(i)
                {
                    case 0->CompressionChamberBlockEntity.this.progress = i1;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    protected final ContainerData data;
    protected int progress=0;

    public static int PROCESSING_TICKS = 80;

    protected final ItemStackHandler itemsHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
        }
    };
    protected final ItemStackHandler outputItems = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
        }
    };
    private ItemStackHandler outputSlot;
    private ItemStackHandler inputSlot;

    private final OTEEnergy ENERGY_STORAGE = new OTEEnergy(ENERGY_REQUIREMENT*3, ENERGY_REQUIREMENT*512) {

        @Override
        public void onChanged() {
            setChanged();
            EnergyDirty=true;

        }
    };


    private static int ENERGY_REQUIREMENT = 750;

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItems = LazyOptional.empty();


    @Override
    public Component getDisplayName() {
        return ChatHelpers.macro("Compression Chamber");
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CompressionChamberMenu(id, inv, this, this.data);
    }


    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(cap == ForgeCapabilities.ENERGY)
        {
            return lazyEnergyHandler.cast();
        }
        if(side == Direction.DOWN && cap == ForgeCapabilities.ITEM_HANDLER)
        {
            // Return the output slot only
            return lazyOutputItems.cast();
        }
        if(cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap,side);
    }


    @Override
    public void onLoad()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()->inputSlot);
        lazyOutputItems = LazyOptional.of(()->outputSlot);
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
        nbt.put("inventory", itemsHandler.serializeNBT());
        nbt.put("output", outputItems.serializeNBT());
        nbt.putInt("prog", progress);
        nbt.putInt("energy", ENERGY_STORAGE.getEnergyStored());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);

        itemsHandler.deserializeNBT(nbt.getCompound("inventory"));
        outputItems.deserializeNBT(nbt.getCompound("output"));
        progress = nbt.getInt("prog");
        ENERGY_STORAGE.setEnergy(nbt.getInt("energy"));
    }

    public void doDrop()
    {
        SimpleContainer cont = new SimpleContainer(itemsHandler.getSlots());
        for (int i = 0; i < itemsHandler.getSlots(); i++) {
            cont.setItem(i, itemsHandler.getStackInSlot(i));
        }
        cont = new SimpleContainer(outputItems.getSlots());
        for (int i = 0; i < outputItems.getSlots(); i++) {
            cont.setItem(i, outputItems.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, cont);
    }


    public static void tick(Level lvl, BlockPos pos, BlockState state, CompressionChamberBlockEntity entity)
    {
        if(lvl.isClientSide())return;



        if(entity.EnergyDirty)
        {
            if(entity.TickCount >= (2 * 20))
            {
                ModMessages.sendToAll(new EnergySyncS2CPacket(entity.getEnergy(), pos));
                entity.EnergyDirty=false;
            } else entity.TickCount++;
        }


        if(hasRecipe(entity))
        {
            if(!hasEnergy(entity))return; // Halt until sufficient energy has been received
            entity.progress++;
            setChanged(lvl, pos, state);
            drain(entity);

            if(entity.progress >= CompressionChamberBlockEntity.PROCESSING_TICKS)
            {
                craftItem(entity);
            }
        }else {
            if(entity.progress>0){
                entity.resetProgress();
                setChanged(lvl, pos, state);
            }
        }
    }


    private static void drain(CompressionChamberBlockEntity entity) {
        entity.ENERGY_STORAGE.extractEnergy(ENERGY_REQUIREMENT, false);
    }

    private static boolean hasEnergy(CompressionChamberBlockEntity entity) {
        return (entity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQUIREMENT);
    }


    private static void craftItem(CompressionChamberBlockEntity entity) {
        if(hasRecipe(entity))
        {
            SimpleContainer inventory = new SimpleContainer(entity.itemsHandler.getSlots());
            for(int i=0;i<entity.itemsHandler.getSlots();i++)
            {
                inventory.setItem(i, entity.itemsHandler.getStackInSlot(i));
            }
            SimpleContainer output = new SimpleContainer(entity.outputItems.getSlots());
            for(int i=0;i<entity.outputItems.getSlots();i++)
            {
                output.setItem(i, entity.outputItems.getStackInSlot(i));
            }

            Optional<CompressionChamberRecipe> recipe = entity.level.getRecipeManager().getRecipeFor(CompressionChamberRecipe.Type.INSTANCE, inventory, entity.level);

            ItemStack existing = entity.outputItems.getStackInSlot(0);

            if(existing.is(Items.AIR))
            {
                existing = recipe.get().getResultItem(entity.level.registryAccess());
            }else
                existing.setCount(recipe.get().getResultItem(entity.level.registryAccess()).getCount() + existing.getCount());

            entity.outputItems.setStackInSlot(0, existing);
            entity.itemsHandler.extractItem(0,1,false);


            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(CompressionChamberBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemsHandler.getSlots());
        for(int i=0;i<entity.itemsHandler.getSlots();i++)
        {
            inventory.setItem(i, entity.itemsHandler.getStackInSlot(i));
        }
        SimpleContainer output = new SimpleContainer(entity.outputItems.getSlots());
        for(int i=0;i<entity.outputItems.getSlots();i++)
        {
            output.setItem(i, entity.outputItems.getStackInSlot(i));
        }

        Optional<CompressionChamberRecipe> recipe = entity.level.getRecipeManager().getRecipeFor(CompressionChamberRecipe.Type.INSTANCE, inventory, entity.level);

        boolean ret = recipe.isPresent() && canInsertIntoOutput(output, recipe.get().getResultItem(entity.level.registryAccess()));

        if(ret)
        {
            PROCESSING_TICKS = recipe.get().getTime();
        }

        return ret;
    }

    private static boolean canInsertIntoOutput(SimpleContainer inventory, ItemStack result) {
        ItemStack existing = inventory.getItem(0);
        boolean stackCompat = (existing.getMaxStackSize() > existing.getCount());
        boolean sameType = (existing.getItem() == result.getItem());
        boolean outputEmpty = existing.isEmpty();

        if(outputEmpty)return true;
        return (stackCompat && sameType);
    }

    private void resetProgress() {
        progress=0;

    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    @Override
    public void setEnergy(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }


    @Override
    public int getEnergy() {
        return ENERGY_STORAGE.getEnergy();
    }
}
