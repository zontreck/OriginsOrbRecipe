package dev.zontreck.otemod.items;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.entities.ModEntityTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    public static final RegistryObject<Item> ETERNIUM_FRAGMENT = ITEMS.register("eternium_fragment", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IHAN_CRYSTAL = ITEMS.register("ihan_crystal", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AURORA_COMPOUND = ITEMS.register("aurora_compound", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> ETERNIUM_RAW_ORE = ITEMS.register("eternium_ore", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_COBALT = ITEMS.register("raw_cobalt", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COBALT_NUGGET = ITEMS.register("cobalt_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COBALT_INGOT = ITEMS.register("cobalt_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ETERNIUM_INGOT = ITEMS.register("eternium_ingot", ()-> new SimpleFoiledItem(new Item.Properties()));



    public static final RegistryObject<Item> MELTED_NETHER_STAR = ITEMS.register("melted_nether_star", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> SINGULARITY = ITEMS.register("singularity", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ETERNIUM_ROD = ITEMS.register("eternium_rod", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> SCRUBBER_FRAME_PIECE = ITEMS.register("scrubber_frame_piece", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> SCRUBBER_FRAME = ITEMS.register("scrubber_frame", () -> new Item(new Item.Properties().stacksTo(64)));


    public static final RegistryObject<Item> VAULTSTEEL_INGOT = ITEMS.register("vault_steel_ingot", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_BL = ITEMS.register("vault_fragment_lower_left", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_LC = ITEMS.register("vault_fragment_lower", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_BR = ITEMS.register("vault_fragment_lower_right", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_L = ITEMS.register("vault_fragment_left", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_R = ITEMS.register("vault_fragment_right", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_UR = ITEMS.register("vault_fragment_upper_right", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_UL = ITEMS.register("vault_fragment_upper_left", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_U = ITEMS.register("vault_fragment_upper", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> VAULT_FRAG_C = ITEMS.register("vault_fragment_center", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(64)));

    
    public static final RegistryObject<Item> VAULT = ITEMS.register("vault", () -> new VaultItem(new Item.Properties().stacksTo(64)));
    
    public static final RegistryObject<Item> VAULT_RAW_ORE = ITEMS.register("raw_vault_steel_ore", () -> new Item(new Item.Properties().stacksTo(64)));

    
    //public static final RegistryObject<Item> POSSUM_SPAWN_EGG = ITEMS.register("possum_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.POSSUM, 0x938686, 0xc68787, new Item.Properties()));



    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
    
}
