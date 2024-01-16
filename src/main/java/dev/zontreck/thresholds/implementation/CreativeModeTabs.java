package dev.zontreck.thresholds.implementation;

import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ThresholdsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ThresholdsMod.MOD_ID);

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
