package dev.zontreck.shapedaionresources.items;

import dev.zontreck.shapedaionresources.ShapedAionResources;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShapedAionResources.MOD_ID);

    public static final RegistryObject<Item> AION_FRAGMENT = ITEMS.register("aion_fragment", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> AION_CRYSTAL = ITEMS.register("aion_crystal", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static final RegistryObject<Item> AION_RAW_ORE = ITEMS.register("aion_ore", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
    
}
