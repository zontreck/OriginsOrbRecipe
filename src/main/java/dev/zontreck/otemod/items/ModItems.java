package dev.zontreck.otemod.items;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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


    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
    
}
