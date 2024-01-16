package dev.zontreck.thresholds.recipe;

import dev.zontreck.thresholds.ThresholdsMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ThresholdsMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CompressionChamberRecipe>> COMPRESSING_SERIALIZER = SERIALIZERS.register("compressing", ()->CompressionChamberRecipe.Serializer.INSTANCE);

    public static void register(IEventBus bus)
    {
        SERIALIZERS.register(bus);
    }
}
