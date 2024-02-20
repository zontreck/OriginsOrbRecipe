package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
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

    public static final Map<String, RegistryObject<Block>> REGISTERED_BLOCKS;
    public static final Map<String, RegistryObject<? extends BlockItem>> REGISTERED_ITEMS;


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
    public static RegistryObject<Block> registerWithItem(String name, Supplier<Blocky> blocky)
    {
        RegistryObject<Block> ret = BLOCKS.register(name, ()->blocky.get().block);
        var item = CreativeModeTabs.addToOTEModTab(ITEMS.register(name, ()->blocky.get().item));

        REGISTERED_BLOCKS.put(name, ret);
        REGISTERED_ITEMS.put(name, item);

        return ret;
    }

    /**
     * Simple block registration with a normal block item.
     * @param name
     * @param a
     * @param props
     * @return
     */
    public static RegistryObject<Block> registerWithItem(String name, Block a, Item.Properties props)
    {
        RegistryObject<Block> ret = BLOCKS.register(name, ()->a);
        var item = CreativeModeTabs.addToOTEModTab(ITEMS.register(name, ()->new BlockItem(ret.get(), props)));

        REGISTERED_BLOCKS.put(name, ret);
        REGISTERED_ITEMS.put(name, item);

        return ret;
    }

    public static RegistryObject<Block> registerDeprecated(String name)
    {
        RegistryObject<Block> ret = BLOCKS.register(name, ()->new DeprecatedBlock());
        var item = CreativeModeTabs.addToOTEModTab(ITEMS.register(name, ()->new DeprecatedBlockItem(ret.get())));

        REGISTERED_BLOCKS.put(name, ret);
        REGISTERED_ITEMS.put(name, item);

        return ret;
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
    public static Block getModdedBlock(String name)
    {
        if(REGISTERED_BLOCKS.containsKey(name)) return REGISTERED_BLOCKS.get(name).get();

        return null;
    }

    public static BlockItem getModdedBlockItem(String name)
    {
        if(REGISTERED_ITEMS.containsKey(name)) return REGISTERED_ITEMS.get(name).get();

        return null;
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

    static {
        REGISTERED_BLOCKS = new HashMap<>();
        REGISTERED_ITEMS = new HashMap<>();

        BlockBehaviour.Properties standard = standardBehavior();

        BlockBehaviour.Properties explosionResistance = explosionResistance();

        BlockBehaviour.Properties noViewBlocking = noViewBlocking();

        BlockBehaviour.Properties stone = stoneLikeBehavior();

        BlockBehaviour.Properties poolLightClean = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 15);
        BlockBehaviour.Properties poolLightDirty = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 12);
        BlockBehaviour.Properties poolLightFilthy = BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel((X) -> 4);


        registerWithItem("eternium_ore_block", new Block(explosionResistance), new Item.Properties());

        registerWithItem("vault_steel_ore_block", new Block(explosionResistance), new Item.Properties());

        registerWithItem("nether_vault_steel_ore_block", new Block(explosionResistance), new Item.Properties());

        registerWithItem("eternium_block", new Block(explosionResistance), new Item.Properties());

        registerWithItem("deepslate_eternium_ore_block", new Block(explosionResistance), new Item.Properties());

        registerWithItem("item_scrubber", new ItemScrubberBlock(noViewBlocking), new Item.Properties());

        registerWithItem("magical_scrubber", new MagicalScrubberBlock(noViewBlocking), new Item.Properties());

        registerWithItem("stable_singularity", new Block(noViewBlocking), new Item.Properties());

        registerWithItem("compression_chamber", new CompressionChamberBlock(noViewBlocking), new Item.Properties());

        registerWithItem("compressed_obsidian_block", new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)), new Item.Properties());

        registerWithItem("layered_compressed_obsidian_block", new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)), new Item.Properties());

        registerWithItem("void", new Block(fullBright().noCollission()), new Item.Properties());

        registerWithItem("whiteout", new Block(fullBright().noCollission()), new Item.Properties());

        registerWithItem("blood_red", new Block(fullBright()), new Item.Properties());

        registerWithItem("red_tile", new Block(fullBright()), new Item.Properties());

        registerWithItem("red_stairs", new StairBlock(getModdedBlock("blood_red")::defaultBlockState, fullBright()), new Item.Properties());

        registerWithItem("red_tile_br", new RotatableBlock(fullBright()), new Item.Properties());

        registerWithItem("red_tile_to_wall", new Block(fullBright()), new Item.Properties());

        registerWithItem("red_wall_variant_1", new Block(fullBright()), new Item.Properties());

        registerWithItem("red_wall_variant_2", new Block(fullBright()), new Item.Properties());

        registerWithItem("cyan", new Block(fullBright()), new Item.Properties());

        registerWithItem("cyan_tile", new Block(fullBright()), new Item.Properties());

        registerWithItem("cyan_stairs", new StairBlock(getModdedBlock("cyan")::defaultBlockState, fullBright()), new Item.Properties());

        registerWithItem("cyan_tile_br", new RotatableBlock(fullBright()), new Item.Properties());

        registerWithItem("cyan_tile_to_wall", new Block(fullBright()), new Item.Properties());

        registerWithItem("cyan_wall_variant_1", new Block(fullBright()), new Item.Properties());

        registerWithItem("cyan_wall_variant_2", new Block(fullBright()), new Item.Properties());

        registerWithItem("pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("pool_tile_stairs", new StairBlock(getModdedBlock("pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("pool_tile_slab", new SlabBlock(stone), new Item.Properties());

        registerWithItem("pool_light", new Block(poolLightClean), new Item.Properties());

        registerWithItem("dirty_pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("dirty_pool_tile_stairs", new StairBlock(getModdedBlock("dirty_pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("dirty_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

        registerWithItem("dirty_pool_light", new Block(poolLightDirty), new Item.Properties());

        registerWithItem("filthy_pool_light", new Block(poolLightFilthy), new Item.Properties());

        registerWithItem("dark_pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("dark_pool_light", new Block(poolLightClean), new Item.Properties());

        registerWithItem("dark_pool_tile_stairs", new StairBlock(getModdedBlock("dark_pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("dark_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

        registerWithItem("blue_pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("blue_pool_tile_stairs", new StairBlock(getModdedBlock("blue_pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("blue_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

        registerWithItem("blue_pool_light", new Block(poolLightClean), new Item.Properties());

        registerWithItem("dirty_red_pool_light", new Block(poolLightDirty), new Item.Properties());

        registerWithItem("filthy_red_pool_light", new Block(poolLightFilthy), new Item.Properties());

        registerWithItem("dirty_blue_pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("dirty_blue_pool_tile_stairs", new StairBlock(getModdedBlock("dirty_blue_pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("dirty_blue_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

        registerWithItem("dirty_blue_pool_light", new Block(poolLightDirty), new Item.Properties());

        registerWithItem("filthy_blue_pool_light", new Block(poolLightFilthy), new Item.Properties());


        registerWithItem("red_pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("red_pool_tile_stairs", new StairBlock(getModdedBlock("red_pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("red_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

        registerWithItem("red_pool_light", new Block(poolLightClean), new Item.Properties());

        registerWithItem("dirty_red_pool_tile", new Block(stone), new Item.Properties());

        registerWithItem("dirty_red_pool_tile_stairs", new StairBlock(getModdedBlock("dirty_red_pool_tile")::defaultBlockState, stone), new Item.Properties());

        registerWithItem("dirty_red_pool_tile_slab", new SlabBlock(stone), new Item.Properties());

    }
}
