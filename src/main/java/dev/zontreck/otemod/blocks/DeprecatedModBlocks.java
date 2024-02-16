package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DeprecatedModBlocks
{

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OTEMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        OTEMod.LOGGER.info("Registering all blocks...");
    }


    public static final RegistryObject<Block> ILUSIUM_ORE_BLOCK = BLOCKS.register("ilusium_ore_block", () -> new Block(BlockBehaviour.Properties.copy(ModBlocks.ETERNIUM_ORE_BLOCK.get()).noOcclusion()));

    public static final RegistryObject<Item> ILUSIUM_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_ore_block", () -> new BlockItem(ILUSIUM_ORE_BLOCK.get(), new Item.Properties())));

    public static final RegistryObject<Block> DEEPSLATE_ILUSIUM_ORE_BLOCK = BLOCKS.register("deepslate_ilusium_ore_block", () -> new Block(BlockBehaviour.Properties.copy(ILUSIUM_ORE_BLOCK.get()).noOcclusion()));

    public static final RegistryObject<Item> DEEPSLATE_ILUSIUM_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("deepslate_ilusium_ore_block", () -> new BlockItem(DEEPSLATE_ILUSIUM_ORE_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> ILUSIUM_BLOCK = BLOCKS.register("ilusium_block", () -> new Block(BlockBehaviour.Properties.copy(ModBlocks.ETERNIUM_BLOCK.get()).noOcclusion().strength(5, 20).requiresCorrectToolForDrops()));

    public static final RegistryObject<Item> ILUSIUM_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_block", () -> new BlockItem(ILUSIUM_BLOCK.get(), new Item.Properties())));
}
