package dev.zontreck.otemod.blocks;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.StatePredicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
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

    public static final RegistryObject<Block> ETERNIUM_ORE_BLOCK = BLOCKS.register("eternium_ore_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(7F).explosionResistance(1200).destroyTime(6)));

    public static final RegistryObject<Item> ETERNIUM_ORE_BLOCK_I = ITEMS.register("eternium_ore_block", () -> new BlockItem(ETERNIUM_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Block> VAULT_STEEL_ORE_BLOCK = BLOCKS.register("vault_steel_ore_block", ()->new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8F).explosionResistance(1200).destroyTime(100)));

    public static final RegistryObject<Item> VAULT_STEEL_ORE_BLOCK_I = ITEMS.register("vault_steel_ore_block", ()->new BlockItem(VAULT_STEEL_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));


    public static final RegistryObject<Block> NETHER_VAULT_STEEL_ORE_BLOCK = BLOCKS.register("nether_vault_steel_ore_block", ()->new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8F).explosionResistance(1200).destroyTime(100)));

    public static final RegistryObject<Item> NETHER_VAULT_STEEL_ORE_BLOCK_I = ITEMS.register("nether_vault_steel_ore_block", ()->new BlockItem(NETHER_VAULT_STEEL_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    //#region TINKERS BLOCKS

    public static final RegistryObject<Block> COBALT_ORE_BLOCK = BLOCKS.register("cobalt_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.NETHER).sound(SoundType.NETHER_ORE).requiresCorrectToolForDrops().strength(10.0F)));

    public static final RegistryObject<Item> COBALT_ORE_ITEM = ITEMS.register("cobalt_ore", ()->new BlockItem(COBALT_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Block> RAW_COBALT_ORE_BLOCK = BLOCKS.register("raw_cobalt_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).sound(SoundType.NETHER_ORE).requiresCorrectToolForDrops().strength(6.0F, 7.0F)));

    public static final RegistryObject<Item> RAW_COBALT_ORE_ITEM = ITEMS.register("raw_cobalt_ore", ()->new BlockItem(RAW_COBALT_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Block> COBALT_BLOCK = BLOCKS.register("cobalt_block", ()-> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Item> COBALT_BLOCK_I = ITEMS.register("cobalt_block", ()-> new BlockItem(COBALT_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    //#endregion


    public static final RegistryObject<Block> DEEPSLATE_ETERNIUM_ORE_BLOCK = BLOCKS.register("deepslate_eternium_ore_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5f).explosionResistance(1200).destroyTime(7)));

    public static final RegistryObject<Item> DEEPSLATE_ETERNIUM_ORE_BLOCK_I = ITEMS.register("deepslate_eternium_ore_block", () -> new BlockItem(DEEPSLATE_ETERNIUM_ORE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Block> AURORA_BLOCK = BLOCKS.register("aurora_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(9f).explosionResistance(100000f).destroyTime(10).sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Item> AURORA_BLOCK_I = ITEMS.register("aurora_block", () -> new BlockItem(AURORA_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Block> AURORA_DOOR = BLOCKS.register("aurora_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).requiresCorrectToolForDrops().strength(9f).explosionResistance(100000f).destroyTime(10).sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Item> AURORA_DOOR_I = ITEMS.register("aurora_door", () -> new BlockItem(AURORA_DOOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Block> CLEAR_GLASS_BLOCK = BLOCKS.register("clear_glass_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(1f).destroyTime(6).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> CLEAR_GLASS_BLOCK_I = ITEMS.register("clear_glass_block", () -> new BlockItem(CLEAR_GLASS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Block> ITEM_SCRUBBER = BLOCKS.register("item_scrubber", ()->new ItemScrubberBlock(BlockBehaviour.Properties.copy(ModBlocks.AURORA_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> ITEM_SCRUBBER_I = ITEMS.register("item_scrubber", ()->new BlockItem(ITEM_SCRUBBER.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    public static final RegistryObject<Block> MAGICAL_SCRUBBER = BLOCKS.register("magical_scrubber", ()->new MagicalScrubberBlock(BlockBehaviour.Properties.copy(ModBlocks.AURORA_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> MAGICAL_SCRUBBER_I = ITEMS.register("magical_scrubber", ()->new BlockItem(MAGICAL_SCRUBBER.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
    

    public static final RegistryObject<Block> STABLE_SINGULARITY = BLOCKS.register("stable_singularity", ()->new Block(BlockBehaviour.Properties.copy(ModBlocks.AURORA_BLOCK.get()).noOcclusion().isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<Item> STABLE_SINGULARITY_I = ITEMS.register("stable_singularity", ()->new BlockItem(STABLE_SINGULARITY.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));



    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}
