package dev.zontreck.otemod.events;

import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.items.tags.ItemStatTag;
import dev.zontreck.otemod.items.tags.ItemStatType;
import dev.zontreck.otemod.items.tags.ItemStatistics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid=OTEMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class LoreHandlers {
    
    @SubscribeEvent
    public void onBlockMined(BlockEvent.BreakEvent ev)
    {
        if(ev.getLevel().isClientSide())return;

        ServerPlayer sp = (ServerPlayer)ev.getPlayer();
        ItemStack itemUsed = sp.getItemInHand(InteractionHand.MAIN_HAND);
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(itemUsed.getItem());
        String itemModName = ChatHelpers.macroize("[0]:[1]", loc.getNamespace(), loc.getPath());
        if(itemModName.contains("pickaxe"))
        {
            updateItem(itemUsed, ItemStatType.PICK);
        }else if(itemModName.contains("shovel"))
        {
            updateItem(itemUsed, ItemStatType.SHOVEL);
        } else if(itemModName.contains("axe"))
        {
            updateItem(itemUsed, ItemStatType.AXE);
        } else if(itemModName.contains("pickadze"))
        {
            updateItem(itemUsed, ItemStatType.PICK);
        }
    }

    @SubscribeEvent
    public void onBlock(BlockEvent.BlockToolModificationEvent ev)
    {
        
        if(ev.getLevel().isClientSide())return;
    
        // Check the block right clicked, and the item in hand

        ServerPlayer sp = (ServerPlayer)ev.getPlayer();
        ItemStack itemUsed = sp.getMainHandItem();
        BlockState bs = ev.getState();
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(itemUsed.getItem());
        String itemModName = ChatHelpers.macroize("[0]:[1]", loc.getNamespace(), loc.getPath());

        if(itemModName.contains("hoe"))
        {
            if(bs.is(Blocks.DIRT) || bs.is(Blocks.GRASS_BLOCK))
            {
                OTEMod.LOGGER.info("DIRT!");
                updateItem(itemUsed, ItemStatType.HOE);
            }
        } else if(itemModName.contains("shovel"))
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
        
        if(ev.getLevel().isClientSide)return;
        if(ev.getCancellationResult() == InteractionResult.PASS)
        {
            // Check the entity right-clicked, and the item in hand

            OTEMod.LOGGER.info("Success");
            ServerPlayer sp = (ServerPlayer)ev.getEntity();
            ItemStack itemUsed = sp.getMainHandItem();
            Entity target = ev.getTarget();
            ResourceLocation loc = ForgeRegistries.ITEMS.getKey(itemUsed.getItem());
            String itemModName = ChatHelpers.macroize("[0]:[1]", loc.getNamespace(), loc.getPath());
            ResourceLocation locEnt = ForgeRegistries.ENTITY_TYPES.getKey(ev.getTarget().getType());
            String entityModName = ChatHelpers.macroize("[0]:[1]", locEnt.getNamespace(), locEnt.getPath());
            if(itemModName.contains("shears"))
            {
                if(entityModName.contains("sheep"))
                {
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
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(weaponUsed.getItem());
        String itemModName = ChatHelpers.macroize("[0]:[1]", loc.getNamespace(), loc.getPath());
        if(itemModName.contains("sword"))
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
