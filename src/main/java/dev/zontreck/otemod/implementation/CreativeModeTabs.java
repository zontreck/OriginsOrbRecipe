package dev.zontreck.otemod.implementation;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeModeTabs
{

    public static <T extends Item> RegistryObject<T> addToOTEModTab(RegistryObject<T> itemLike)
    {
        return itemLike;
    }

}
