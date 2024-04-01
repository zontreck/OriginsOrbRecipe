package dev.zontreck.otemod.effects;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, OTEMod.MOD_ID);


    public static final RegistryObject<Potion> AWKWARD_FLIGHT = REGISTRY.register("flight_awkward", ()->new Potion(new MobEffectInstance(ModEffects.FLIGHT.get(), 30*20, 1)));

    public static final RegistryObject<Potion> MUNDANE_FLIGHT = REGISTRY.register("flight_basic", ()->new Potion(new MobEffectInstance(ModEffects.FLIGHT.get(), 60*20, 1)));

    public static final RegistryObject<Potion> FLIGHT = REGISTRY.register("flight", ()->new Potion(new MobEffectInstance(ModEffects.FLIGHT.get(), 360*20, 1)));


    public static void register(IEventBus bus)
    {
        REGISTRY.register(bus);
    }
}
