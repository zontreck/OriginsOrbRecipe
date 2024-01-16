package dev.zontreck.thresholds.effects;

import dev.zontreck.thresholds.ThresholdsMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects
{
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ThresholdsMod.MOD_ID);

    public static final RegistryObject<MobEffect> FLIGHT = REGISTRY.register("flight", ()->new FlightEffect(MobEffectCategory.BENEFICIAL, 0xFF0000FF));



    public static void register(IEventBus bus)
    {
        REGISTRY.register(bus);
    }
}
