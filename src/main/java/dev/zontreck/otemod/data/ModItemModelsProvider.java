package dev.zontreck.otemod.data;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.items.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModItemModelsProvider extends ItemModelProvider
{
    public ModItemModelsProvider(PackOutput output, ExistingFileHelper helper)
    {
        super(output, OTEMod.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {

        item(ModItems.ETERNIUM_FRAGMENT);
        item(ModItems.IHAN_CRYSTAL);
        item(ModItems.ETERNIUM_RAW_ORE);
        item(ModItems.ETERNIUM_INGOT);
        item(ModItems.MELTED_ENDER_PEARL);
        item(ModItems.SINGULARITY);
        item(ModItems.COMPRESSED_OBSIDIAN_SHEET);
        item(ModItems.LAYERED_COMPRESSED_OBSIDIAN_SHEET);
        item(ModItems.ENCASED_SINGULARITY);
        item(ModItems.ETERNIUM_ROD);
        item(ModItems.SCRUBBER_FRAME_PIECE);
        item(ModItems.SCRUBBER_FRAME);
        item(ModItems.VAULTSTEEL_INGOT);
        item(ModItems.VAULT_FRAG_BL);
        item(ModItems.VAULT_FRAG_BR);
        item(ModItems.VAULT_FRAG_LC);
        item(ModItems.VAULT_FRAG_L);
        item(ModItems.VAULT_FRAG_R);
        item(ModItems.VAULT_FRAG_UR);
        item(ModItems.VAULT_FRAG_UL);
        item(ModItems.VAULT_FRAG_U);
        item(ModItems.VAULT_FRAG_C);
        item(ModItems.VAULT);
        item(ModItems.VAULT_RAW_ORE);
        item(ModItems.MIAB);
        item(ModItems.EMPTY_SPAWN_EGG);
        item(ModItems.GENERIC_DEPRECATED_ITEM);
        item(ModItems.WHITE_BRICK);
        item(ModItems.BLUE_BRICK);
        item(ModItems.LIGHT_BLUE_BRICK);
        item(ModItems.CYAN_BRICK);
        item(ModItems.DARK_RED_DYE);


        /*
        DEPRECATED ITEMS
         */

        deprecated(DeprecatedModItems.ILUSIUM_ORE);
        deprecated(DeprecatedModItems.ILUSIUM_ROD);
        deprecated(DeprecatedModItems.ILUSIUM_INGOT);
        deprecated(DeprecatedModItems.POSS_BALL);
        deprecated(DeprecatedModItems.AURORA_COMPOUND);
    }



    private ItemModelBuilder item(RegistryObject<Item> ite) {
        return this.item((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ite.get())));
    }

    private ItemModelBuilder item(ResourceLocation item) {
        return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(new ModelFile.UncheckedModelFile("item/generated"))).texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }
    private ItemModelBuilder deprecated(RegistryObject<Item> ite) {
        return this.deprecated((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ite.get())));
    }

    private ItemModelBuilder deprecated(ResourceLocation item) {
        return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(new ModelFile.UncheckedModelFile("item/generated"))).texture("layer0", new ResourceLocation(item.getNamespace(), "item/deprecated"));
    }
}
