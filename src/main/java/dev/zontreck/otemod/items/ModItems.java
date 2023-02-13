package dev.zontreck.otemod.items;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    public static final RegistryObject<Item> ETERNIUM_FRAGMENT = ITEMS.register("eternium_fragment", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> IHAN_CRYSTAL = ITEMS.register("ihan_crystal", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> ETERNIUM_DUST = ITEMS.register("eternium_dust", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> AURORA_COMPOUND = ITEMS.register("aurora_compound", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Item> ETERNIUM_RAW_ORE = ITEMS.register("eternium_ore", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> RAW_COBALT = ITEMS.register("raw_cobalt", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> COBALT_NUGGET = ITEMS.register("cobalt_nugget", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> COBALT_INGOT = ITEMS.register("cobalt_ingot", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> PATTERN = ITEMS.register("pattern", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SCORCHED_BRICK = ITEMS.register("scorched_brick", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> COPPER_CAN = ITEMS.register("copper_can", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)));
    public static final RegistryObject<Item> ETERNIUM_INGOT = ITEMS.register("eternium_ingot", ()-> new SimpleFoiledItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));



    public static final RegistryObject<Item> MELTED_NETHER_STAR = ITEMS.register("melted_nether_star", () -> new SimpleFoiledItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64)));
    public static final RegistryObject<Item> SINGULARITY = ITEMS.register("singularity", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
    public static final RegistryObject<Item> ETERNIUM_ROD = ITEMS.register("eternium_rod", () -> new SimpleFoiledItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64)));
    public static final RegistryObject<Item> SCRUBBER_FRAME_PIECE = ITEMS.register("scrubber_frame_piece", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64)));
    public static final RegistryObject<Item> SCRUBBER_FRAME = ITEMS.register("scrubber_frame", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(64)));



    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
    
}
