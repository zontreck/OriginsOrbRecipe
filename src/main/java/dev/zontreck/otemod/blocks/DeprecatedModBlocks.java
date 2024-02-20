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


    public static final RegistryObj ILUSIUM_ORE_BLOCK = registerDeprecated("ilusium_ore_block");
    public static final RegistryObj DEEPSLATE_ILUSIUM_ORE_BLOCK = registerDeprecated("deepslate_ilusium_ore_block");
    public static final RegistryObj ILUSIUM_BLOCK = registerDeprecated("ilusium_block");
    public static final RegistryObj CLEAR_GLASS_BLOCK = registerDeprecated("clear_glass_block");
    public static final RegistryObj LIMINAL_TILES = registerDeprecated("liminal_tiles");
    public static final RegistryObj BLACK = registerDeprecated("black");
    public static final RegistryObj LIMINAL_TILE_STAIRS = registerDeprecated("liminal_tile_stairs");
    public static final RegistryObj LIMINAL_TILE_SLAB = registerDeprecated("liminal_tile_slab");
    public static final RegistryObj LIMINAL_WINDOW = registerDeprecated("liminal_window");
    public static final RegistryObj LIME = registerDeprecated("lime");
    public static final RegistryObj LIME_TILE = registerDeprecated("lime_tile");
    public static final RegistryObj LIME_STAIRS = registerDeprecated("lime_stairs");
    public static final RegistryObj LIME_TILE_BR = registerDeprecated("lime_tile_br");
    public static final RegistryObj LIME_TILE_TO_WALL = registerDeprecated("lime_tile_to_wall");
    public static final RegistryObj LIME_WALL_V1 = registerDeprecated("lime_wall_variant_1");
    public static final RegistryObj LIME_WALL_V2 = registerDeprecated("lime_wall_variant_2");

}
