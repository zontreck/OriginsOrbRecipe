package dev.zontreck.otemod.recipe;

import com.google.gson.JsonObject;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CompressionChamberRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    private final int seconds;

    public CompressionChamberRecipe(ResourceLocation id, ItemStack output, Ingredient input, int seconds)
    {
        this.id=id;
        this.output=output;
        this.input=input;
        this.seconds=seconds;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if(level.isClientSide) return false;

        return input.test(simpleContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public int getTime() {
        return seconds*20;
    }

    public static class Type implements RecipeType<CompressionChamberRecipe> {
        private Type(){}

        public static final Type INSTANCE = new Type();
        public static final String ID = "compressing";
    }

    public static class Serializer implements RecipeSerializer<CompressionChamberRecipe>
    {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(OTEMod.MOD_ID, Type.ID);

        @Override
        public CompressionChamberRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "input"));

            int seconds = GsonHelper.getAsInt(jsonObject, "time");

            return new CompressionChamberRecipe(resourceLocation, output, input, seconds);
        }

        @Override
        public @Nullable CompressionChamberRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            ItemStack output = friendlyByteBuf.readItem();

            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            int seconds = friendlyByteBuf.readInt();

            return new CompressionChamberRecipe(resourceLocation, output, input, seconds);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, CompressionChamberRecipe compressionChamberRecipe) {
            friendlyByteBuf.writeItem(compressionChamberRecipe.output);
            compressionChamberRecipe.input.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeInt(compressionChamberRecipe.seconds);
        }
    }
}
