package dev.zontreck.otemod.implementation.inits;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.scrubber.ScrubberMenu;
import dev.zontreck.otemod.implementation.vault.VaultMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenuTypes 
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OTEMod.MOD_ID);

    public static final RegistryObject <MenuType <VaultMenu>> VAULT = CONTAINERS.register("vault", ()-> new MenuType<>(VaultMenu::new));

    public static final RegistryObject<MenuType<ScrubberMenu>> SCRUBBER = registerMenuType(ScrubberMenu::new, "item_scrubber_menu");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name)
    {
        return CONTAINERS.register(name, ()->IForgeMenuType.create(factory));
    }

    public static void register(IEventBus bus)
    {
        CONTAINERS.register(bus);
    }
}
