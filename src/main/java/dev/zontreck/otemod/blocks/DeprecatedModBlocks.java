package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Deprecated
public class DeprecatedModBlocks
{

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OTEMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);


    public static void register(IEventBus bus){
        BLOCKS.register(bus);
        ITEMS.register(bus);
        OTEMod.LOGGER.info("Registering all deprecated blocks...");
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
    private static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
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
        return standardBehavior().noOcclusion().isViewBlocking(DeprecatedModBlocks::never);
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

    public static RegistryObject<Block> registerDeprecated(RegistryObject<Block> blk)
    {
        CreativeModeTabs.addToOTEModTab(ITEMS.register(blk.getId().getPath(), ()->new DeprecatedBlockItem(blk.get())));

        return blk;
    }


    public static final RegistryObject<Block> ILUSIUM_ORE_BLOCK = registerDeprecated(BLOCKS.register("ilusium_ore_block", ()-> new DeprecatedBlock()));

    public static final RegistryObject<Block> DEEPSLATE_ILUSIUM_ORE_BLOCK = registerDeprecated(BLOCKS.register("deepslate_ilusium_ore_block", ()->new DeprecatedBlock()));
    public static final RegistryObject<Block> ILUSIUM_BLOCK = registerDeprecated(BLOCKS.register("ilusium_block", ()->new DeprecatedBlock()));
    public static final RegistryObject<Block> CLEAR_GLASS_BLOCK = registerDeprecated(BLOCKS.register("clear_glass_block", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIMINAL_TILES = registerDeprecated(BLOCKS.register("liminal_tiles", ()->new DeprecatedBlock()));
    public static final RegistryObject<Block> LIMINAL_TILE_STAIRS = registerDeprecated(BLOCKS.register("liminal_tile_stairs", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIMINAL_TILE_SLAB = registerDeprecated(BLOCKS.register("liminal_tile_slab", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIMINAL_WINDOW = registerDeprecated(BLOCKS.register("liminal_window", ()->new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME = registerDeprecated(BLOCKS.register("lime", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME_TILE = registerDeprecated(BLOCKS.register("lime_tile", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME_STAIRS = registerDeprecated(BLOCKS.register("lime_stairs", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME_TILE_BR = registerDeprecated(BLOCKS.register("lime_tile_br", ()->new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME_TILE_TO_WALL = registerDeprecated(BLOCKS.register("lime_tile_to_wall", ()-> new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME_WALL_V1 = registerDeprecated(BLOCKS.register("lime_wall_variant_1", ()->new DeprecatedBlock()));
    public static final RegistryObject<Block> LIME_WALL_V2 = registerDeprecated(BLOCKS.register("lime_wall_variant_2", ()->new DeprecatedBlock()));

}
