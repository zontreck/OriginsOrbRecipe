package dev.zontreck.otemod.events;

import dev.zontreck.libzontreck.items.lore.LoreContainer;
import dev.zontreck.libzontreck.items.lore.LoreEntry;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.items.tags.ItemStatTag;
import dev.zontreck.otemod.items.tags.ItemStatType;
import dev.zontreck.otemod.items.tags.ItemStatistics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=OTEMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class LoreHandlers {
    
    @SubscribeEvent
    public void onBlockMined(BlockEvent.BreakEvent ev)
    {
        if(ev.getWorld().isClientSide())return;

        ServerPlayer sp = (ServerPlayer)ev.getPlayer();
        ItemStack itemUsed = sp.getItemInHand(InteractionHand.MAIN_HAND);
        if(itemUsed.getItem() instanceof PickaxeItem)
        {
            updateItem(itemUsed, ItemStatType.PICK);
        }else if(itemUsed.getItem() instanceof ShovelItem)
        {
            updateItem(itemUsed, ItemStatType.SHOVEL);
        } else if(itemUsed.getItem() instanceof AxeItem)
        {
            updateItem(itemUsed, ItemStatType.AXE);
        }
    }

    @SubscribeEvent
    public void onBlock(BlockEvent.BlockToolModificationEvent ev)
    {
        
        if(ev.getWorld().isClientSide())return;
    
        // Check the block right clicked, and the item in hand

        ServerPlayer sp = (ServerPlayer)ev.getPlayer();
        ItemStack itemUsed = sp.getMainHandItem();
        BlockState bs = ev.getState();

        if(itemUsed.getItem() instanceof HoeItem)
        {
            if(bs.is(Blocks.DIRT) || bs.is(Blocks.GRASS_BLOCK))
            {
                OTEMod.LOGGER.info("DIRT!");
                updateItem(itemUsed, ItemStatType.HOE);
            }
        } else if(itemUsed.getItem() instanceof ShovelItem)
        {
            if(bs.is(Blocks.GRASS_BLOCK))
            {
                updateItem(itemUsed, ItemStatType.SHOVELPATH);
            }
        }

        
    }

    @SubscribeEvent
    public void onShears(PlayerInteractEvent.EntityInteract ev)
    {
        
        if(ev.getWorld().isClientSide)return;
        if(ev.getCancellationResult() == InteractionResult.PASS)
        {
            // Check the entity right clicked, and the item in hand

            OTEMod.LOGGER.info("Success");
            ServerPlayer sp = (ServerPlayer)ev.getEntity();
            ItemStack itemUsed = sp.getMainHandItem();
            Entity target = ev.getTarget();
            if(itemUsed.getItem() instanceof ShearsItem)
            {
                OTEMod.LOGGER.info("Shears");
                if(target instanceof Sheep)
                {
                    OTEMod.LOGGER.info("Sheep");
                    updateItem(itemUsed, ItemStatType.SHEARS);
                }
            }
        }
    }


    @SubscribeEvent
    public void onEntityKilled(LivingDeathEvent ev)
    {
        if(ev.getEntity().level.isClientSide)return;

        // Handle two things

        // 1. Update mob kill count on a sword if wielded.
        // 2. If mob, process randomness. If death by player with looting 3, 0.1% chance for a spawn egg to drop
        Entity source = ev.getSource().getEntity();
        ServerPlayer sp= null;
        if(source instanceof Player)
        {
            sp = (ServerPlayer)source;
        }

        if(sp==null)return;

        ItemStack weaponUsed = sp.getItemInHand(InteractionHand.MAIN_HAND);
        if(weaponUsed.getItem() instanceof SwordItem)
        {
            updateItem(weaponUsed, ItemStatType.SWORD);
            
        }

    }

    // Only valid to be used by OTEMod
    protected static void updateItem(ItemStack weaponUsed, ItemStatType type)
    {
    
        // Update the mob kill count
        CompoundTag props = weaponUsed.getTag();
        if(props==null)props=new CompoundTag();
        CompoundTag container = props.getCompound(ItemStatTag.STATS_TAG+"_"+type.name().toLowerCase());
        LoreContainer contain;
        if(container.isEmpty())
        {
            contain = new LoreContainer(weaponUsed);
        }else {
            contain = new LoreContainer(container, weaponUsed);
        }

        ItemStatTag isTag;
        try{
            isTag = new ItemStatTag(type, container.getInt(ItemStatTag.STATS_TAG+"_"+type.name().toLowerCase()));
        }catch (Exception e){
            isTag = new ItemStatTag(type, 0);
        }
        isTag.increment();
        LoreEntry entry;
        
        if(contain.miscData.LoreData.size()==0)
        {
            // Missing entry
            entry = new LoreEntry();
            entry.text = ItemStatistics.makeText(isTag);
            contain.miscData.LoreData.add(entry);
        }else {
            entry = contain.miscData.LoreData.get(0); // Stat is set at 0
            entry.text = ItemStatistics.makeText(isTag);
        }

        // Update item
        contain.commitLore();

        // Save the misc data to the item for later
        // Reinitialize the container as the contain NBT
        container = new CompoundTag();
        contain.save(container);
        isTag.save(container);
        props.put(ItemStatTag.STATS_TAG+"_"+type.name().toLowerCase(), container);
        weaponUsed.setTag(props);
    }
}
