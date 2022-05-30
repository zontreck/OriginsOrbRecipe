package dev.zontreck.shapedaionresources.blocks;

import java.util.function.Supplier;

import dev.zontreck.shapedaionresources.ShapedAionResources;
import dev.zontreck.shapedaionresources.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ShapedAionResources.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShapedAionResources.MOD_ID);

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        ShapedAionResources.LOGGER.info("Registering all blocks...");
    }


    
    public static final RegistryObject<Block> AION_ORE_BLOCK = BLOCKS.register("aion_ore_block", () -> new OreBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(9f).explosionResistance(1200).destroyTime(10)));

    public static final RegistryObject<Item> AION_ORE_BLOCK_I = ITEMS.register("aion_ore_block", () -> new BlockItem(AION_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Block> DEEPSLATE_AION_ORE_BLOCK = BLOCKS.register("deepslate_aion_ore_block", () -> new OreBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(9f).explosionResistance(1200).destroyTime(10)));

    public static final RegistryObject<Item> DEEPSLATE_AION_ORE_BLOCK_I = ITEMS.register("deepslate_aion_ore_block", () -> new BlockItem(DEEPSLATE_AION_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Block> AION_BLOCK = BLOCKS.register("aion_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(9f).explosionResistance(100000f).destroyTime(10).sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Item> AION_BLOCK_I = ITEMS.register("aion_block", () -> new BlockItem(AION_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

}
