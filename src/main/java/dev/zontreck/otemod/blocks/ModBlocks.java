package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
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

    /**
     * This variant of the method is meant for complex blocks that require a custom block item
     * @param name
     * @param blocky
     * @return
     */
    public static RegistryObj registerWithItem(String name, Supplier<Blocky> blocky)
    {
        RegistryObject<Block> ret = BLOCKS.register(name, ()->blocky.get().block);
        var item = CreativeModeTabs.addToOTEModTab(ITEMS.register(name, ()->blocky.get().item));

        return new RegistryObj(ret,item);
    }

    /**
     * Simple block registration with a normal block item.
     * @param name
     * @param a
     * @param props
     * @return
     */
    public static RegistryObj registerWithItem(String name, Block a, Item.Properties props)
    {
        RegistryObject<Block> ret = BLOCKS.register(name, ()->a);
        var item = CreativeModeTabs.addToOTEModTab(ITEMS.register(name, ()->new BlockItem(ret.get(), props)));

        return new RegistryObj(ret,item);
    }

    public static RegistryObj registerDeprecated(String name)
    {
        RegistryObject<Block> ret = BLOCKS.register(name, ()->new DeprecatedBlock());
        var item = CreativeModeTabs.addToOTEModTab(ITEMS.register(name, ()->new DeprecatedBlockItem(ret.get())));

        return new RegistryObj(ret,item);
    }
    private static class Blocky
    {
        public Block block;
        public BlockItem item;

        public Blocky(Block block, BlockItem item)
        {
            this.block=block;
            this.item=item;
        }
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


    public static final RegistryObj ETERNIUM_ORE_BLOCK = registerWithItem("eternium_ore_block", new DropExperienceBlock(explosionResistance), new Item.Properties());

    public static final RegistryObj VAULT_STEEL_ORE_BLOCK = registerWithItem("vault_steel_ore_block", new DropExperienceBlock(explosionResistance), new Item.Properties());

    public static final RegistryObj NETHER_VAULT_STEEL_ORE_BLOCK = registerWithItem("nether_vault_steel_ore_block", new DropExperienceBlock(explosionResistance), new Item.Properties());

    public static final RegistryObj ETERNIUM_BLOCK = registerWithItem("eternium_block", new Block(explosionResistance), new Item.Properties());
    public static final RegistryObj DEEPSLATE_ETERNIUM_ORE_BLOCK = registerWithItem("deepslate_eternium_ore_block", new DropExperienceBlock(explosionResistance), new Item.Properties());
    public static final RegistryObj ITEM_SCRUBBER = registerWithItem("item_scrubber", new ItemScrubberBlock(noViewBlocking), new Item.Properties());
    public static final RegistryObj MAGICAL_SCRUBBER = registerWithItem("magical_scrubber", new MagicalScrubberBlock(noViewBlocking), new Item.Properties());
    public static final RegistryObj STABLE_SINGULARITY = registerWithItem("stable_singularity", new Block(noViewBlocking), new Item.Properties());
    public static final RegistryObj COMPRESSION_CHAMBER = registerWithItem("compression_chamber", new CompressionChamberBlock(noViewBlocking), new Item.Properties());
    public static final RegistryObj COMPRESSED_OBSIDIAN_BLOCK = registerWithItem("compressed_obsidian_block", new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)), new Item.Properties());
    public static final RegistryObj LAYERED_COMPRESSED_OBSIDIAN_BLOCK = registerWithItem("layered_compressed_obsidian_block", new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)), new Item.Properties());
    public static final RegistryObj VOID = registerWithItem("void", new Block(fullBright().noCollission()), new Item.Properties());
    public static final RegistryObj WHITEOUT = registerWithItem("whiteout", new Block(fullBright().noCollission()), new Item.Properties());
    public static final RegistryObj BLOOD_RED = registerWithItem("blood_red", new Block(fullBright()), new Item.Properties());
    public static final RegistryObj RED_TILE = registerWithItem("red_tile", new Block(fullBright()), new Item.Properties());
    public static final RegistryObj RED_STAIRS = registerWithItem("red_stairs", new StairBlock(BLOOD_RED.block.get()::defaultBlockState, fullBright()), new Item.Properties());
    public static final RegistryObj RED_TILE_BR = registerWithItem("red_tile_br", new RotatableBlock(fullBright()), new Item.Properties());

    public static final RegistryObj RED_TILE_TO_WALL = registerWithItem("red_tile_to_wall", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj RED_WALL_V1 = registerWithItem("red_wall_variant_1", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj RED_WALL_V2 = registerWithItem("red_wall_variant_2", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj CYAN = registerWithItem("cyan", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj CYAN_TILE = registerWithItem("cyan_tile", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj CYAN_STAIRS = registerWithItem("cyan_stairs", new StairBlock(CYAN.block.get()::defaultBlockState, fullBright()), new Item.Properties());

    public static final RegistryObj CYAN_TILE_BR = registerWithItem("cyan_tile_br", new RotatableBlock(fullBright()), new Item.Properties());

    public static final RegistryObj CYAN_TILE_TO_WALL = registerWithItem("cyan_tile_to_wall", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj CYAN_WALL_V1 = registerWithItem("cyan_wall_variant_1", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj CYAN_WALL_V2 = registerWithItem("cyan_wall_variant_2", new Block(fullBright()), new Item.Properties());

    public static final RegistryObj POOL_TILE = registerWithItem("pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj POOL_TILE_STAIRS = registerWithItem("pool_tile_stairs", new StairBlock(POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj POOL_TILE_SLAB = registerWithItem("pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    public static final RegistryObj POOL_LIGHT = registerWithItem("pool_light", new Block(poolLightClean), new Item.Properties());

    public static final RegistryObj DIRTY_POOL_TILE = registerWithItem("dirty_pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj DIRTY_POOL_TILE_STAIRS = registerWithItem("dirty_pool_tile_stairs", new StairBlock(DIRTY_POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj DIRTY_POOL_TILE_SLAB = registerWithItem("dirty_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    public static final RegistryObj DIRTY_POOL_LIGHT = registerWithItem("dirty_pool_light", new Block(poolLightDirty), new Item.Properties());

    public static final RegistryObj FILTHY_POOL_LIGHT = registerWithItem("filthy_pool_light", new Block(poolLightFilthy), new Item.Properties());

    public static final RegistryObj DARK_POOL_TILE = registerWithItem("dark_pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj DARK_POOL_LIGHT = registerWithItem("dark_pool_light", new Block(poolLightClean), new Item.Properties());

    public static final RegistryObj DARK_POOL_TILE_STAIRS = registerWithItem("dark_pool_tile_stairs", new StairBlock(DARK_POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj DARK_POOL_TILE_SLAB = registerWithItem("dark_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    public static final RegistryObj BLUE_POOL_TILE = registerWithItem("blue_pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj BLUE_POOL_TILE_STAIRS = registerWithItem("blue_pool_tile_stairs", new StairBlock(BLUE_POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj BLUE_POOL_TILE_SLAB = registerWithItem("blue_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    public static final RegistryObj BLUE_POOL_LIGHT = registerWithItem("blue_pool_light", new Block(poolLightClean), new Item.Properties());

    public static final RegistryObj DIRTY_BLUE_POOL_TILE = registerWithItem("dirty_blue_pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj DIRTY_BLUE_POOL_TILE_STAIRS = registerWithItem("dirty_blue_pool_tile_stairs", new StairBlock(DIRTY_BLUE_POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj DIRTY_BLUE_POOL_TILE_SLAB = registerWithItem("dirty_blue_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    public static final RegistryObj DIRTY_BLUE_POOL_LIGHT = registerWithItem("dirty_blue_pool_light", new Block(poolLightDirty), new Item.Properties());

    public static final RegistryObj FILTHY_BLUE_POOL_LIGHT = registerWithItem("filthy_blue_pool_light", new Block(poolLightFilthy), new Item.Properties());

    public static final RegistryObj RED_POOL_TILE = registerWithItem("red_pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj RED_POOL_TILE_STAIRS = registerWithItem("red_pool_tile_stairs", new StairBlock(RED_POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj RED_POOL_TILE_SLAB = registerWithItem("red_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    public static final RegistryObj RED_POOL_LIGHT = registerWithItem("red_pool_light", new Block(poolLightClean), new Item.Properties());

    public static final RegistryObj DIRTY_RED_POOL_TILE = registerWithItem("dirty_red_pool_tile", new Block(stone), new Item.Properties());

    public static final RegistryObj DIRTY_RED_POOL_LIGHT = registerWithItem("dirty_red_pool_light", new Block(poolLightDirty), new Item.Properties());

    public static final RegistryObj FILTHY_RED_POOL_LIGHT = registerWithItem("filthy_red_pool_light", new Block(poolLightFilthy), new Item.Properties());

    public static final RegistryObj DIRTY_RED_POOL_TILE_STAIRS = registerWithItem("dirty_red_pool_tile_stairs", new StairBlock(DIRTY_RED_POOL_TILE.block.get()::defaultBlockState, stone), new Item.Properties());

    public static final RegistryObj DIRTY_RED_POOL_TILE_SLAB = registerWithItem("dirty_red_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

}
