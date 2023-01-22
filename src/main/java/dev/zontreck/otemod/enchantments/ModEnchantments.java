package dev.zontreck.otemod.enchantments;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> REGISTERS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, OTEMod.MOD_ID);

    public static final RegistryObject<Enchantment> MOB_EGGING_ENCHANTMENT = REGISTERS.register("mob_egging", ()->new MobEggEnchantment());

    public static void register(IEventBus bus){
        REGISTERS.register(bus);
    }
}
