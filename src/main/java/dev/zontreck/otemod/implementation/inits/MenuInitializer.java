package dev.zontreck.otemod.implementation.inits;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.VaultMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MenuInitializer 
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, OTEMod.MOD_ID);

    public static final RegistryObject <MenuType <VaultMenu>> VAULT = CONTAINERS.register("vault", ()-> new MenuType<>(VaultMenu::new));


    private MenuInitializer(){}
}
