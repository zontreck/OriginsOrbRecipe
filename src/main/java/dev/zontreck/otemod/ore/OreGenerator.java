package dev.zontreck.otemod.ore;

import java.util.ArrayList;
import java.util.List;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class OreGenerator {
    public static final List<ConfiguredFeature<OreConfiguration, OreFeature>> OVERWORLD_ORES = new ArrayList();


    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_AION_ORE = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.AION_ORE_BLOCK.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_AION_ORE_BLOCK.get().defaultBlockState()));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> AION_ORE = FeatureUtils.register("aion_ore_block", Feature.ORE, new OreConfiguration(OVERWORLD_AION_ORE, 6));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> DEEPSLATE_AION_ORE = FeatureUtils.register("deepslate_aion_ore_block", Feature.ORE, new OreConfiguration(OVERWORLD_AION_ORE, 6));

    
    //public static final Holder<PlacedFeature> EBONY_PLACED = PlacementUtils.register("ebony_placed",
            //ModConfiguredFeatures.EBONY_SPAWN, VegetationPlacements.treePlacement(
                    //PlacementUtils.countExtra(3, 0.1f, 2)));

    //public static final Holder<PlacedFeature> PINK_ROSE_PLACED = PlacementUtils.register("pink_rose_placed",
            //ModConfiguredFeatures.PINK_ROSE, RarityFilter.onAverageOnceEvery(16),
            //InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        public static final Holder<PlacedFeature> AION_ORE_PLACED = PlacementUtils.register("aion_ore_placed",
            AION_ORE, ModdedOrePlacement.commonOrePlacement(3, // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(20))));


        //public static final Holder<PlacedFeature> DEEPSLATE_AION_ORE_PLACED = PlacementUtils.register("deepslate_aion_ore_placed",
        //                    DEEPSLATE_AION_ORE, ModdedOrePlacement.commonOrePlacement(3, // VeinsPerChunk
        //                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-20), VerticalAnchor.aboveBottom(0))));

    public static void generateOres(final BiomeLoadingEvent ev)
    {
            
        List<Holder<PlacedFeature>> base = ev.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        //ShapedAionResources.LOGGER.info("Register: AION_ORE");
        base.add(AION_ORE_PLACED);
        //base.add(DEEPSLATE_AION_ORE_PLACED);
    }
}
