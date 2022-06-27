package dev.zontreck.otemod.ore;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import cpw.mods.modlauncher.api.ITransformationService.Resource;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Modifier 
{
    private static final String ETERNIUM_ORE = "eternium_oregen_overworld";
    private static final ResourceLocation ETERNIUM_ORE_RL = new ResourceLocation(OTEMod.MOD_ID, ETERNIUM_ORE);
    private static final ResourceKey<PlacedFeature> ETERNIUM_ORE_KEY = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, ETERNIUM_ORE_RL);
    private static final ResourceKey<ConfiguredFeature<?,?>> ETERNIUM_ORE_KEY_CFG = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, ETERNIUM_ORE_RL);


    private static final String ADD_FEATURE_ETERNIUM_ORE_OVERWORLD = "add_eternium_ow";
    private static final ResourceLocation ADD_ETERNIUM_ORE_OW = new ResourceLocation(OTEMod.MOD_ID, ADD_FEATURE_ETERNIUM_ORE_OVERWORLD);

    
    public static void DoProcess(GatherDataEvent ev) {
        /*
        final DataGenerator generator = ev.getGenerator();
        final ExistingFileHelper fExistingFileHelper = ev.getExistingFileHelper();
        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());

        // Create the placed ore feature
        //final ResourceKey<ConfiguredFeature<?,?>> configuredFeatKey = OreFeatures.ORE_DIAMOND_SMALL.unwrapKey().get().cast(Registry.CONFIGURED_FEATURE_REGISTRY).get();

        final Holder<ConfiguredFeature<?,?>> configuredFeatHolder = ops.registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ETERNIUM_ORE_KEY_CFG);

        final PlacedFeature eternium_ore_place = new PlacedFeature(configuredFeatHolder, List.of(
            BiomeFilter.biome()
        ));

        final PlacedFeature pfeat = ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().get(ETERNIUM_ORE_RL);

        final HolderSet.Named<Biome> hfeatBiome = new Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD);

        
        final BiomeModifier modif = new AddFeaturesBiomeModifier(
            hfeatBiome,
            HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ETERNIUM_ORE_KEY)), 
            Decoration.UNDERGROUND_ORES
        );


        generator.addProvider(ev.includeServer(), JsonCodecProvider.forDatapackRegistry(generator, fExistingFileHelper, OTEMod.MOD_ID, ops, Registry.PLACED_FEATURE_REGISTRY, Map.of(
            ETERNIUM_ORE_RL, pfeat
        )));

        generator.addProvider(ev.includeServer(), JsonCodecProvider.forDatapackRegistry(generator, fExistingFileHelper, OTEMod.MOD_ID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS, Map.of(
            ADD_ETERNIUM_ORE_OW, modif
        )));
        */
    }


    public record ModifierOfBiomes(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) implements BiomeModifier
    {
        private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(OTEMod.MODIFY_BIOMES_RL, ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, OTEMod.MOD_ID);

        @Override
        public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
            if(phase == Phase.ADD && biomes.contains(biome)){
                builder.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, feature);
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            return SERIALIZER.get();
        }

        public static Codec<ModifierOfBiomes> makeCodec()
        {
            return RecordCodecBuilder.create(builder->builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(ModifierOfBiomes::biomes),
                PlacedFeature.CODEC.fieldOf("feature").forGetter(ModifierOfBiomes::feature)
            ).apply(builder, ModifierOfBiomes::new));
        }

    }
    
}
