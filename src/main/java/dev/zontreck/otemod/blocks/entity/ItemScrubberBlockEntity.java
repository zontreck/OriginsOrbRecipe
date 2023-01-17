package dev.zontreck.otemod.blocks.entity;

import javax.annotation.Nullable;

import dev.zontreck.otemod.implementation.OutputItemStackHandler;
import dev.zontreck.otemod.implementation.scrubber.ScrubberMenu;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemScrubberBlockEntity extends BlockEntity implements MenuProvider
{

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

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItems = LazyOptional.empty();

    public ItemScrubberBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.ITEM_SCRUBBER.get(), pos, state);
        outputSlot = new OutputItemStackHandler(outputItems);

        this.data = new ContainerData() {

            @Override
            public int get(int p_39284_) {
                return switch(p_39284_){
                    case 0 -> ItemScrubberBlockEntity.this.progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int p_39285_, int p_39286_) {
                switch(p_39285_)
                {
                    case 0 -> ItemScrubberBlockEntity.this.progress = p_39286_;
                }
                
            }

            @Override
            public int getCount() {
                return 1;
            }
            
        };
    }

    protected final ContainerData data;
    private int progress = 0;

    public static final int MAXIMUM_PROCESSING_TICKS = 750;


    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ScrubberMenu(id, inv, this, this.data);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.otemod.item_scrubber");
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
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
        lazyItemHandler = LazyOptional.of(()->itemsHandler);
        lazyOutputItems = LazyOptional.of(()->outputSlot);
    }


    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyOutputItems.invalidate();
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        nbt.put("inventory", itemsHandler.serializeNBT());
        nbt.put("output", outputItems.serializeNBT());
        nbt.putInt("prog", progress);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt){
        super.load(nbt);

        itemsHandler.deserializeNBT(nbt.getCompound("inventory"));
        outputItems.deserializeNBT(nbt.getCompound("output"));
        progress = nbt.getInt("prog");
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

    public static void tick(Level lvl, BlockPos pos, BlockState state, ItemScrubberBlockEntity entity)
    {
        if(lvl.isClientSide())return;

        if(hasRecipe(entity))
        {
            entity.progress++;
            setChanged(lvl, pos, state);

            if(entity.progress >= ItemScrubberBlockEntity.MAXIMUM_PROCESSING_TICKS)
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

    private static void craftItem(ItemScrubberBlockEntity entity) {
        if(hasRecipe(entity))
        {
            ItemStack existing = entity.outputItems.getStackInSlot(0);
            existing.setCount(existing.getCount()+1);
            if(existing.is(Items.AIR))
            {
                existing = makeOutputItem(entity.itemsHandler.getStackInSlot(0));
            }
            entity.itemsHandler.extractItem(0, 1, false);
            entity.outputItems.setStackInSlot(0, existing);

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(ItemScrubberBlockEntity entity) {
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


        boolean hasAnItem = !entity.itemsHandler.getStackInSlot(0).isEmpty();
        
        ItemStack result = null;
        if(hasAnItem)
        {
            result = makeOutputItem(entity.itemsHandler.getStackInSlot(0));
            
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

    private void resetProgress() {
        progress=0;
        
    }

    protected static ItemStack makeOutputItem(ItemStack original)
    {
        ItemStack newItem = new ItemStack(original.getItem(),1);
        return newItem;
    }
}
