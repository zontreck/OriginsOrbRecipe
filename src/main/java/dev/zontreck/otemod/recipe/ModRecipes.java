package dev.zontreck.otemod.recipe;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OTEMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CompressionChamberRecipe>> COMPRESSING_SERIALIZER = SERIALIZERS.register("compressing", ()->CompressionChamberRecipe.Serializer.INSTANCE);

    //public static final RegistryObject<RecipeSerializer<UncraftingRecipe>> UNCRAFTING_SERIALIZER = SERIALIZERS.register("uncrafting", UncraftingRecipe.Serializer::new);

    public static void register(IEventBus bus)
    {
        SERIALIZERS.register(bus);
    }
}
