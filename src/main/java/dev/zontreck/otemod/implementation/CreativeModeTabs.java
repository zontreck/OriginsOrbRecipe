package dev.zontreck.otemod.implementation;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OTEMod.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> OTEMOD_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> OTE_TAB = REGISTER.register("otemod", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tabs.otemod"))
            .icon(ModItems.IHAN_CRYSTAL.get()::getDefaultInstance)
            .displayItems((display, output) -> OTEMOD_TAB_ITEMS.forEach(it->output.accept(it.get())))
            .build()
    );

    public static <T extends Item> RegistryObject<T> addToOTEModTab(RegistryObject<T> itemLike)
    {
        OTEMOD_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

}
