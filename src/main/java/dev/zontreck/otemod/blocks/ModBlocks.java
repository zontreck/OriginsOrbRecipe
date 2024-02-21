package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import dev.zontreck.otemod.implementation.ModDyes;
import dev.zontreck.otemod.mixins.DyeColorMixin;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    private static BlockBehaviour.StatePredicate shulkerState = (p_152653_, p_152654_, p_152655_) -> {
        BlockEntity blockentity = p_152654_.getBlockEntity(p_152655_);
        if (!(blockentity instanceof ShulkerBoxBlockEntity)) {
            return true;
        } else {
            ShulkerBoxBlockEntity shulkerboxblockentity = (ShulkerBoxBlockEntity)blockentity;
            return shulkerboxblockentity.isClosed();
        }
    };


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OTEMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
    private static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }



    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        OTEMod.LOGGER.info("Registering all blocks...");
    }

    private static BlockBehaviour.Properties standardBehavior()
    {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(7F).destroyTime(6);
    }
    private static BlockBehaviour.Properties stoneLikeBehavior()
    {
        return BlockBehaviour.Properties.copy(Blocks.COBBLESTONE);
    }

    private static BlockBehaviour.Properties explosionResistance()
    {
        return standardBehavior().explosionResistance(1200);
    }

    private static BlockBehaviour.Properties noViewBlocking()
    {
        return standardBehavior().noOcclusion().isViewBlocking(ModBlocks::never);
    }

    private static BlockBehaviour.Properties fullBright()
    {
        return standardBehavior().lightLevel((X)->{
            return 15;
        }).noOcclusion();
    }

    private static BlockBehaviour.Properties standard = standardBehavior();

    private static BlockBehaviour.Properties explosionResistance = explosionResistance();

    private static BlockBehaviour.Properties noViewBlocking = noViewBlocking();

    private static BlockBehaviour.Properties stone = stoneLikeBehavior();

    private static BlockBehaviour.Properties poolLightClean = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 15);
    private static BlockBehaviour.Properties poolLightDirty = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 12);
    private static BlockBehaviour.Properties poolLightFilthy = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 4);


    public static RegistryObject<Block> registerWithItem(RegistryObject<Block> blk, Item.Properties props)
    {
        CreativeModeTabs.addToOTEModTab(ITEMS.register(blk.getId().getPath(), ()->new BlockItem(blk.get(), props)));

        return blk;
    }


    public static final RegistryObject<Block> ETERNIUM_ORE_BLOCK = registerWithItem(BLOCKS.register("eternium_ore_block", ()->new DropExperienceBlock(explosionResistance)), new Item.Properties());

    public static final RegistryObject<Block> VAULT_STEEL_ORE_BLOCK = registerWithItem(BLOCKS.register("vault_steel_ore_block", ()->new DropExperienceBlock(explosionResistance)), new Item.Properties());

    public static final RegistryObject<Block> NETHER_VAULT_STEEL_ORE_BLOCK = registerWithItem(BLOCKS.register("nether_vault_steel_ore_block", ()->new DropExperienceBlock(explosionResistance)), new Item.Properties());

    public static final RegistryObject<Block> ETERNIUM_BLOCK = registerWithItem(BLOCKS.register("eternium_block", ()->new Block(explosionResistance)), new Item.Properties());

    public static final RegistryObject<Block> DEEPSLATE_ETERNIUM_ORE_BLOCK = registerWithItem(BLOCKS.register("deepslate_eternium_ore_block", ()->new DropExperienceBlock(explosionResistance)), new Item.Properties());

    public static final RegistryObject<Block> ITEM_SCRUBBER = registerWithItem(BLOCKS.register("item_scrubber", ()->new ItemScrubberBlock(noViewBlocking)), new Item.Properties());

    public static final RegistryObject<Block> MAGICAL_SCRUBBER = registerWithItem(BLOCKS.register("magical_scrubber", ()->new MagicalScrubberBlock(noViewBlocking)), new Item.Properties());

    public static final RegistryObject<Block> STABLE_SINGULARITY = registerWithItem(BLOCKS.register("stable_singularity", ()->new Block(noViewBlocking)), new Item.Properties());

    public static final RegistryObject<Block> COMPRESSION_CHAMBER = registerWithItem(BLOCKS.register("compression_chamber", ()->new CompressionChamberBlock(noViewBlocking)), new Item.Properties());

    public static final RegistryObject<Block> COMPRESSED_OBSIDIAN_BLOCK = registerWithItem(BLOCKS.register("compressed_obsidian_block", ()->new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN))), new Item.Properties());

    public static final RegistryObject<Block> LAYERED_COMPRESSED_OBSIDIAN_BLOCK = registerWithItem(BLOCKS.register("layered_compressed_obsidian_block", ()->new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN))), new Item.Properties());

    public static final RegistryObject<Block> VOID = registerWithItem(BLOCKS.register("void", ()->new Block(fullBright().noCollission())), new Item.Properties());

    public static final RegistryObject<Block> WHITEOUT = registerWithItem(BLOCKS.register("whiteout", ()->new Block(fullBright().noCollission())), new Item.Properties());

    public static final RegistryObject<Block> BLOOD_RED = registerWithItem(BLOCKS.register("blood_red", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> RED_TILE = registerWithItem(BLOCKS.register("red_tile", ()-> new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> RED_STAIRS = registerWithItem(BLOCKS.register("red_stairs", ()-> new StairBlock(BLOOD_RED.get()::defaultBlockState, fullBright())), new Item.Properties());

    public static final RegistryObject<Block> RED_TILE_BR = registerWithItem(BLOCKS.register("red_tile_br", ()->new RotatableBlock(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> RED_TILE_TO_WALL = registerWithItem(BLOCKS.register("red_tile_to_wall", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> RED_WALL_V1 = registerWithItem(BLOCKS.register("red_wall_variant_1", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> RED_WALL_V2 = registerWithItem(BLOCKS.register("red_wall_variant_2", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN = registerWithItem(BLOCKS.register("cyan", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN_TILE = registerWithItem(BLOCKS.register("cyan_tile", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN_STAIRS = registerWithItem(BLOCKS.register("cyan_stairs", ()->new StairBlock(CYAN.get()::defaultBlockState, fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN_TILE_BR = registerWithItem(BLOCKS.register("cyan_tile_br", ()->new RotatableBlock(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN_TILE_TO_WALL = registerWithItem(BLOCKS.register("cyan_tile_to_wall", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN_WALL_V1 = registerWithItem(BLOCKS.register("cyan_wall_variant_1", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> CYAN_WALL_V2 = registerWithItem(BLOCKS.register("cyan_wall_variant_2", ()->new Block(fullBright())), new Item.Properties());

    public static final RegistryObject<Block> POOL_TILE = registerWithItem(BLOCKS.register("pool_tile", ()->new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("pool_tile_stairs", ()->new StairBlock(POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> POOL_TILE_SLAB = registerWithItem(BLOCKS.register("pool_tile_slab", ()->new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> POOL_LIGHT = registerWithItem(BLOCKS.register("pool_light", ()->new Block(poolLightClean)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_POOL_TILE = registerWithItem(BLOCKS.register("dirty_pool_tile", ()->new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("dirty_pool_tile_stairs", ()->new StairBlock(DIRTY_POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_POOL_TILE_SLAB = registerWithItem(BLOCKS.register("dirty_pool_tile_slab", ()-> new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_POOL_LIGHT = registerWithItem(BLOCKS.register("dirty_pool_light", ()->new Block(poolLightDirty)), new Item.Properties());

    public static final RegistryObject<Block> FILTHY_POOL_LIGHT = registerWithItem(BLOCKS.register("filthy_pool_light", ()->new Block(poolLightFilthy)), new Item.Properties());

    public static final RegistryObject<Block> DARK_POOL_TILE = registerWithItem(BLOCKS.register("dark_pool_tile", ()->new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> DARK_POOL_LIGHT = registerWithItem(BLOCKS.register("dark_pool_light", ()->new Block(poolLightClean)), new Item.Properties());

    public static final RegistryObject<Block> DARK_POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("dark_pool_tile_stairs", ()->new StairBlock(DARK_POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> DARK_POOL_TILE_SLAB = registerWithItem(BLOCKS.register("dark_pool_tile_slab",()-> new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> BLUE_POOL_TILE = registerWithItem(BLOCKS.register("blue_pool_tile",()-> new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> BLUE_POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("blue_pool_tile_stairs", ()->new StairBlock(BLUE_POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> BLUE_POOL_TILE_SLAB = registerWithItem(BLOCKS.register("blue_pool_tile_slab",()-> new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> BLUE_POOL_LIGHT = registerWithItem(BLOCKS.register("blue_pool_light", ()->new Block(poolLightClean)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_BLUE_POOL_TILE = registerWithItem(BLOCKS.register("dirty_blue_pool_tile", ()->new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_BLUE_POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("dirty_blue_pool_tile_stairs", ()->new StairBlock(DIRTY_BLUE_POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_BLUE_POOL_TILE_SLAB = registerWithItem(BLOCKS.register("dirty_blue_pool_tile_slab", ()->new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_BLUE_POOL_LIGHT = registerWithItem(BLOCKS.register("dirty_blue_pool_light", ()->new Block(poolLightDirty)), new Item.Properties());

    public static final RegistryObject<Block> FILTHY_BLUE_POOL_LIGHT = registerWithItem(BLOCKS.register("filthy_blue_pool_light", ()->new Block(poolLightFilthy)), new Item.Properties());

    public static final RegistryObject<Block> RED_POOL_TILE = registerWithItem(BLOCKS.register("red_pool_tile", ()->new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> RED_POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("red_pool_tile_stairs", ()->new StairBlock(RED_POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> RED_POOL_TILE_SLAB = registerWithItem(BLOCKS.register("red_pool_tile_slab", ()->new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> RED_POOL_LIGHT = registerWithItem(BLOCKS.register("red_pool_light", ()->new Block(poolLightClean)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_RED_POOL_TILE = registerWithItem(BLOCKS.register("dirty_red_pool_tile", ()->new Block(stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_RED_POOL_LIGHT = registerWithItem(BLOCKS.register("dirty_red_pool_light", ()->new Block(poolLightDirty)), new Item.Properties());

    public static final RegistryObject<Block> FILTHY_RED_POOL_LIGHT = registerWithItem(BLOCKS.register("filthy_red_pool_light", ()->new Block(poolLightFilthy)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_RED_POOL_TILE_STAIRS = registerWithItem(BLOCKS.register("dirty_red_pool_tile_stairs", ()->new StairBlock(DIRTY_RED_POOL_TILE.get()::defaultBlockState, stone)), new Item.Properties());

    public static final RegistryObject<Block> DIRTY_RED_POOL_TILE_SLAB = registerWithItem(BLOCKS.register("dirty_red_pool_tile_slab", ()->new SlabBlock(stone)), new Item.Properties());

    public static final RegistryObject<Block> DARK_RED_WOOL = registerWithItem(BLOCKS.register("dark_red_wool", ()->new Block(BlockBehaviour.Properties.copy(Blocks.RED_WOOL))), new Item.Properties());

    public static final RegistryObject<Block> DARK_RED_CARPET = registerWithItem(BLOCKS.register("dark_red_carpet", ()->new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.RED_CARPET))), new Item.Properties());

    public static final RegistryObject<Block> DARK_RED_BED = registerWithItem(BLOCKS.register("dark_red_bed", ()->new BedBlock(DyeColor.byName("dark_red", DyeColor.RED), BlockBehaviour.Properties.copy(Blocks.RED_BED).mapColor((X)->{
        return X.getValue(BedBlock.PART) == BedPart.FOOT ? ModDyes.DARK_RED.getMapColor() : MapColor.WOOL;
    }).noOcclusion())), new Item.Properties().stacksTo(1));

    public static final RegistryObject<Block> DARK_RED_SHULKER_BOX = registerWithItem((BLOCKS.register("dark_red_shulker_box", ()->new ShulkerBoxBlock(ModDyes.DARK_RED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(2.0F).dynamicShape().noOcclusion().isSuffocating(shulkerState).isViewBlocking(shulkerState)))), new Item.Properties().stacksTo(1));

}
