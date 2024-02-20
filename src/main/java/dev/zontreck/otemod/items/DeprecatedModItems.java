package dev.zontreck.otemod.items;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DeprecatedModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OTEMod.MOD_ID);

    public static final RegistryObject<Item> ILUSIUM_ORE = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_ore", () -> new DeprecatedItem()));
    public static final RegistryObject<Item> ILUSIUM_ROD = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_rod", () -> new DeprecatedItem()));


    public static final RegistryObject<Item> ILUSIUM_INGOT = CreativeModeTabs.addToOTEModTab(ITEMS.register("ilusium_ingot", () -> new DeprecatedItem()));

    public static final RegistryObject<Item> AURORA_COMPOUND = CreativeModeTabs.addToOTEModTab(ITEMS.register("aurora_compound", () -> new DeprecatedItem()));

    public static final RegistryObject<Item> POSS_BALL = CreativeModeTabs.addToOTEModTab(ITEMS.register("poss_ball", () -> new DeprecatedItem()));

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
