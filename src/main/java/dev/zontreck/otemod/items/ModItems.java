package dev.zontreck.otemod.items;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    public static final RegistryObject<Item> ETERNIUM_FRAGMENT = CreativeModeTabs.addToOTEModTab(ITEMS.register("eternium_fragment", () -> new Item(new Item.Properties())));

    public static final RegistryObject<Item> IHAN_CRYSTAL = CreativeModeTabs.addToOTEModTab(ITEMS.register("ihan_crystal", () -> new IhanCrystal()));



    public static final RegistryObject<Item> ETERNIUM_RAW_ORE = CreativeModeTabs.addToOTEModTab(ITEMS.register("eternium_ore", () -> new Item(new Item.Properties())));

    public static final RegistryObject<Item> ETERNIUM_INGOT = CreativeModeTabs.addToOTEModTab(ITEMS.register("eternium_ingot", ()-> new SimpleFoiledItem(new Item.Properties())));



    public static final RegistryObject<Item> MELTED_ENDER_PEARL = CreativeModeTabs.addToOTEModTab(ITEMS.register("melted_ender_pearl", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> SINGULARITY = CreativeModeTabs.addToOTEModTab(ITEMS.register("singularity", () -> new UnstableSingularity(new Item.Properties().stacksTo(1))));

    public static final RegistryObject<Item> COMPRESSED_OBSIDIAN_SHEET = CreativeModeTabs.addToOTEModTab(ITEMS.register("compressed_obsidian_sheet", ()->new Item(new Item.Properties())));

    public static final RegistryObject<Item> LAYERED_COMPRESSED_OBSIDIAN_SHEET = CreativeModeTabs.addToOTEModTab(ITEMS.register("layered_compressed_obsidian_sheet", ()->new SimpleFoiledItem(new Item.Properties())));
    public static final RegistryObject<Item> ENCASED_SINGULARITY = CreativeModeTabs.addToOTEModTab(ITEMS.register("encased_singularity", ()->new SimpleFoiledItem(new Item.Properties())));

    public static final RegistryObject<Item> ETERNIUM_ROD = CreativeModeTabs.addToOTEModTab(ITEMS.register("eternium_rod", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> SCRUBBER_FRAME_PIECE = CreativeModeTabs.addToOTEModTab(ITEMS.register("scrubber_frame_piece", () -> new Item(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> SCRUBBER_FRAME = CreativeModeTabs.addToOTEModTab(ITEMS.register("scrubber_frame", () -> new Item(new Item.Properties().stacksTo(64))));


    public static final RegistryObject<Item> VAULTSTEEL_INGOT = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_steel_ingot", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_BL = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_lower_left", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_LC = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_lower", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_BR = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_lower_right", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_L = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_left", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_R = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_right", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_UR = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_upper_right", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_UL = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_upper_left", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_U = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_upper", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> VAULT_FRAG_C = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault_fragment_center", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64))));

    
    public static final RegistryObject<Item> VAULT = CreativeModeTabs.addToOTEModTab(ITEMS.register("vault", () -> new VaultItem(new Item.Properties().stacksTo(64))));
    
    public static final RegistryObject<Item> VAULT_RAW_ORE = CreativeModeTabs.addToOTEModTab(ITEMS.register("raw_vault_steel_ore", () -> new Item(new Item.Properties().stacksTo(64))));

    @Deprecated
    public static final RegistryObject<Item> POSS_BALL = CreativeModeTabs.addToOTEModTab(ITEMS.register("poss_ball", () -> new DeprecatedItem()));

    public static final RegistryObject<Item> MIAB = CreativeModeTabs.addToOTEModTab(ITEMS.register("mob_capture_ball", ()->new MobCaptureBall()));


    public static final RegistryObject<Item> EMPTY_SPAWN_EGG = CreativeModeTabs.addToOTEModTab(ITEMS.register("empty_spawn_egg", () -> new Item(new Item.Properties())));

    public static final RegistryObject<Item> GENERIC_DEPRECATED_ITEM = CreativeModeTabs.addToOTEModTab(ITEMS.register("deprecated", ()->new DeprecatedItem()));

    
    //public static final RegistryObject<Item> POSSUM_SPAWN_EGG = ITEMS.register("possum_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.POSSUM, 0x938686, 0xc68787, new Item.Properties())));



    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
    
}
