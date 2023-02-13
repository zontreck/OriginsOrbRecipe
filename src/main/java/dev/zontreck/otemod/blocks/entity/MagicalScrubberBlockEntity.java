package dev.zontreck.otemod.blocks.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import dev.zontreck.otemod.implementation.OutputItemStackHandler;
import dev.zontreck.otemod.implementation.energy.OTEEnergy;
import dev.zontreck.otemod.implementation.scrubber.MagicalScrubberMenu;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.EnergySyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class MagicalScrubberBlockEntity extends BlockEntity implements MenuProvider
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

    private final OTEEnergy ENERGY_STORAGE = new OTEEnergy(ENERGY_REQ*3, ENERGY_REQ+512) {

        @Override
        public void onChanged() {
            setChanged();

            ModMessages.sendToAll(new EnergySyncS2CPacket(energy, getBlockPos()));
        }
        
    };

    private static final int ENERGY_REQ = 10000;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItems = LazyOptional.empty();

    public MagicalScrubberBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.MAGICAL_SCRUBBER.get(), pos, state);
        outputSlot = new OutputItemStackHandler(outputItems);

        this.data = new ContainerData() {

            @Override
            public int get(int p_39284_) {
                return switch(p_39284_){
                    case 0 -> MagicalScrubberBlockEntity.this.progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int p_39285_, int p_39286_) {
                switch(p_39285_)
                {
                    case 0 -> MagicalScrubberBlockEntity.this.progress = p_39286_;
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

    public static final int MAXIMUM_PROCESSING_TICKS = 1750;


    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new MagicalScrubberMenu(id, inv, this, this.data);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.otemod.magical_scrubber");
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
        lazyItemHandler = LazyOptional.of(()->itemsHandler);
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

    public static void tick(Level lvl, BlockPos pos, BlockState state, MagicalScrubberBlockEntity entity)
    {
        if(lvl.isClientSide())return;

        if(hasRecipe(entity))
        {
            if(!hasEnergy(entity))return; // Halt until sufficient energy has been received
            entity.progress++;
            setChanged(lvl, pos, state);
            drain(entity);

            if(entity.progress >= MagicalScrubberBlockEntity.MAXIMUM_PROCESSING_TICKS)
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

    private static void drain(MagicalScrubberBlockEntity entity) {
        entity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnergy(MagicalScrubberBlockEntity entity) {
        return (entity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ);
    }

    private static void craftItem(MagicalScrubberBlockEntity entity) {
        if(hasRecipe(entity))
        {
            ItemStack existing = entity.outputItems.getStackInSlot(0);
            ItemStack main = entity.itemsHandler.getStackInSlot(0);

            Map<Enchantment, Integer> enchants = main.getAllEnchantments();
            if(enchants.size()>0)
            {
                Iterator<Map.Entry<Enchantment,Integer>> iEntries = enchants.entrySet().iterator();
                Map.Entry<Enchantment,Integer> entry = iEntries.next();

                EnchantmentInstance eInst = new EnchantmentInstance(entry.getKey(), entry.getValue());
                existing = EnchantedBookItem.createForEnchantment(eInst);

                main.getAllEnchantments().remove(entry.getKey());
                //iEntries.remove();
                main = makeOutputItem(main);
                while(iEntries.hasNext())
                {
                    entry = iEntries.next();
                    main.enchant(entry.getKey(), entry.getValue());
                }

                if(main.getAllEnchantments().size()==0){
                    entity.itemsHandler.extractItem(0, 1, false);
                }else entity.itemsHandler.setStackInSlot(0, main);
                

            }else{

                existing.setCount(existing.getCount()+1);
                if(existing.is(Items.AIR))
                {
                    existing = makeOutputItem(entity.itemsHandler.getStackInSlot(0));
                }
                entity.itemsHandler.extractItem(0, 1, false);
            }

            entity.outputItems.setStackInSlot(0, existing);
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(MagicalScrubberBlockEntity entity) {
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

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergy(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }
}
