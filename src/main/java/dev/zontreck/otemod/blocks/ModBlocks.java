package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OTEMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        OTEMod.LOGGER.info("Registering all blocks...");
    }

    public static final RegistryObject<Block> ETERNIUM_ORE_BLOCK = BLOCKS.register("eternium_ore_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(7F).explosionResistance(1200).destroyTime(6)));

    public static final RegistryObject<Item> ETERNIUM_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("eternium_ore_block", () -> new BlockItem(ETERNIUM_ORE_BLOCK.get(), new Item.Properties())));

    public static final RegistryObject<Block> VAULT_STEEL_ORE_BLOCK = BLOCKS.register("vault_steel_ore_block", ()->new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(8F).explosionResistance(1200).destroyTime(100)));

    public static final RegistryObject<Item> VAULT_STEEL_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_steel_ore_block", ()->new BlockItem(VAULT_STEEL_ORE_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> NETHER_VAULT_STEEL_ORE_BLOCK = BLOCKS.register("nether_vault_steel_ore_block", ()->new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(8F).explosionResistance(1200).destroyTime(100)));

    public static final RegistryObject<Item> NETHER_VAULT_STEEL_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("nether_vault_steel_ore_block", ()->new BlockItem(NETHER_VAULT_STEEL_ORE_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> ETERNIUM_BLOCK = BLOCKS.register("eternium_block", ()->new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(8F).explosionResistance(1200).destroyTime(100)));

    public static final RegistryObject<Item> ETERNIUM_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("eternium_block", ()->new FoiledBlockItem(ETERNIUM_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> DEEPSLATE_ETERNIUM_ORE_BLOCK = BLOCKS.register("deepslate_eternium_ore_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5f).explosionResistance(1200).destroyTime(7)));

    public static final RegistryObject<Item> DEEPSLATE_ETERNIUM_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("deepslate_eternium_ore_block", () -> new BlockItem(DEEPSLATE_ETERNIUM_ORE_BLOCK.get(), new Item.Properties())));

    public static final RegistryObject<Block> AURORA_BLOCK = BLOCKS.register("aurora_block", () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(9f).explosionResistance(100000f).destroyTime(10).sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Item> AURORA_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("aurora_block", () -> new BlockItem(AURORA_BLOCK.get(), new Item.Properties())));



    public static final RegistryObject<Block> AURORA_DOOR = BLOCKS.register("aurora_door", AuroraDoorBlock::new);

    public static final RegistryObject<Item> AURORA_DOOR_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("aurora_door", () -> new BlockItem(AURORA_DOOR.get(), new Item.Properties())));


    public static final RegistryObject<Block> CLEAR_GLASS_BLOCK = BLOCKS.register("clear_glass_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(1f).destroyTime(6).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> CLEAR_GLASS_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("clear_glass_block", () -> new BlockItem(CLEAR_GLASS_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> ITEM_SCRUBBER = BLOCKS.register("item_scrubber", ()->new ItemScrubberBlock(BlockBehaviour.Properties.copy(ModBlocks.AURORA_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> ITEM_SCRUBBER_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("item_scrubber", ()->new BlockItem(ITEM_SCRUBBER.get(), new Item.Properties())));

    public static final RegistryObject<Block> MAGICAL_SCRUBBER = BLOCKS.register("magical_scrubber", ()->new MagicalScrubberBlock(BlockBehaviour.Properties.copy(ModBlocks.AURORA_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> MAGICAL_SCRUBBER_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("magical_scrubber", ()->new BlockItem(MAGICAL_SCRUBBER.get(), new Item.Properties())));
    

    public static final RegistryObject<Block> STABLE_SINGULARITY = BLOCKS.register("stable_singularity", ()->new Block(BlockBehaviour.Properties.copy(ModBlocks.AURORA_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> STABLE_SINGULARITY_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("stable_singularity", ()->new BlockItem(STABLE_SINGULARITY.get(), new Item.Properties())));

    public static final RegistryObject<Block> ILUSIUM_ORE_BLOCK = BLOCKS.register("ilusium_ore_block", () -> new Block(BlockBehaviour.Properties.copy(ModBlocks.ETERNIUM_ORE_BLOCK.get()).noOcclusion()));

    public static final RegistryObject<Item> ILUSIUM_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_ore_block", () -> new BlockItem(ILUSIUM_ORE_BLOCK.get(), new Item.Properties())));

    public static final RegistryObject<Block> DEEPSLATE_ILUSIUM_ORE_BLOCK = BLOCKS.register("deepslate_ilusium_ore_block", () -> new Block(BlockBehaviour.Properties.copy(ILUSIUM_ORE_BLOCK.get()).noOcclusion()));

    public static final RegistryObject<Item> DEEPSLATE_ILUSIUM_ORE_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("deepslate_ilusium_ore_block", () -> new BlockItem(DEEPSLATE_ILUSIUM_ORE_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> ILUSIUM_BLOCK = BLOCKS.register("ilusium_block", () -> new Block(BlockBehaviour.Properties.copy(ModBlocks.ETERNIUM_BLOCK.get()).noOcclusion().strength(5, 20).requiresCorrectToolForDrops()));

    public static final RegistryObject<Item> ILUSIUM_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_block", () -> new BlockItem(ILUSIUM_BLOCK.get(), new Item.Properties())));

    public static final RegistryObject<Block> COMPRESSION_CHAMBER_BLOCK = BLOCKS.register("compression_chamber", ()->new CompressionChamberBlock(BlockBehaviour.Properties.copy(ModBlocks.ILUSIUM_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> COMPRESSION_CHAMBER_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("compression_chamber", ()->new BlockItem(COMPRESSION_CHAMBER_BLOCK.get(), new Item.Properties())));


    public static final RegistryObject<Block> COMPRESSED_OBSIDIAN_BLOCK = BLOCKS.register("compressed_obsidian_block", ()->new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)));

    public static final RegistryObject<Item> COMPRESSED_OBSIDIAN_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("compressed_obsidian_block", ()->new BlockItem(COMPRESSED_OBSIDIAN_BLOCK.get(), new Item.Properties())));
    public static final RegistryObject<Block> LAYERED_COMPRESSED_OBSIDIAN_BLOCK = BLOCKS.register("layered_compressed_obsidian_block", ()->new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)));

    public static final RegistryObject<Item> LAYERED_COMPRESSED_OBSIDIAN_BLOCK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("layered_compressed_obsidian_block", ()->new BlockItem(LAYERED_COMPRESSED_OBSIDIAN_BLOCK.get(), new Item.Properties())));









    public static final RegistryObject<Block> LIMINAL_TILES = BLOCKS.register("liminal_tiles", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK)));

    public static final RegistryObject<Item> LIMINAL_TILES_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("liminal_tiles", ()->new BlockItem(LIMINAL_TILES.get(), new Item.Properties())));

    public static final RegistryObject<Block> BLACK = BLOCKS.register("black", ()->new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK)));

    public static final RegistryObject<Item> BLACK_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("black", ()->new FoiledBlockItem(BLACK.get(), new Item.Properties().stacksTo(128))));

    public static final RegistryObject<Block> LIMINAL_TILE_STAIRS = BLOCKS.register("liminal_tile_stairs", ()->new StairBlock(LIMINAL_TILES.get()::defaultBlockState, BlockBehaviour.Properties.copy(Blocks.BEDROCK).destroyTime(1000).strength(1000)));

    public static final RegistryObject<Item> LIMINAL_TILE_STAIRS_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("liminal_tile_stairs", ()->new BlockItem(LIMINAL_TILE_STAIRS.get(), new Item.Properties())));

    public static final RegistryObject<Block> LIMINAL_TILE_SLAB = BLOCKS.register("liminal_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BEDROCK)));

    public static final RegistryObject<Item> LIMINAL_TILE_SLAB_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("liminal_tile_slab", ()->new BlockItem(LIMINAL_TILE_SLAB.get(), new Item.Properties())));

    public static final RegistryObject<Block> LIMINAL_WINDOW = BLOCKS.register("liminal_window", () -> new ParallaxWindow(15));

    public static final RegistryObject<Item> LIMINAL_WINDOW_I = CreativeModeTabs.addToOTEModTab(ITEMS.register("liminal_window", () -> new BlockItem(LIMINAL_WINDOW.get(), new Item.Properties())));


    /*


    public static final class_2248 LIMINAL_WINDOW_NOON = (class_2248)createBlock("liminal_window_noon", new LiminalWindowBlock(QuiltBlockSettings.method_9630((class_4970)class_2246.field_31037).method_9626(class_2498.field_11537)), true, (class_1761)ModItems.LIMINAL_POOLS_ITEM_GROUP);

    public static final class_2248 LIMINAL_WINDOW_DUSK = (class_2248)createBlock("liminal_window_dusk", new LiminalWindowBlock(QuiltBlockSettings.method_9630((class_4970)class_2246.field_31037).method_9626(class_2498.field_11537)), true, (class_1761)ModItems.LIMINAL_POOLS_ITEM_GROUP);

    public static final class_2248 LIMINAL_WINDOW_ABYSS = (class_2248)createBlock("liminal_window_abyss", new LiminalWindowBlock(QuiltBlockSettings.method_9630((class_4970)class_2246.field_31037).method_9626(class_2498.field_11537)), true, (class_1761)ModItems.LIMINAL_POOLS_ITEM_GROUP);*/


    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}
