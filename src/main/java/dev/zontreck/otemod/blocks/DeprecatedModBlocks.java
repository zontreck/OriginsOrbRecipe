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


    static {
        REGISTERED_ITEMS = new HashMap<>();
        REGISTERED_BLOCKS=new HashMap<>();

        registerDeprecated("ilusium_ore_block");
        registerDeprecated("deepslate_ilusium_ore_block");
        registerDeprecated("ilusium_block");
        registerDeprecated("clear_glass_block");
        registerDeprecated("liminal_tiles");
        registerDeprecated("black");
        registerDeprecated("liminal_tile_stairs");
        registerDeprecated("liminal_tile_slab");
        registerDeprecated("liminal_window");

        registerDeprecated("lime");
        registerDeprecated("lime_tile");
        registerDeprecated("lime_stairs");
        registerDeprecated("lime_tile_br");
        registerDeprecated("lime_tile_to_wall");
        registerDeprecated("lime_wall_variant_1");
        registerDeprecated("lime_wall_variant_2");

    }
}
