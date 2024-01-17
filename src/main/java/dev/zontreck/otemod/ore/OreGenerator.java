package dev.zontreck.otemod.ore;


import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class OreGenerator {


    public static final List<ConfiguredFeature<OreConfiguration, OreFeature>> OVERWORLD_ORES = new ArrayList();


    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_ETERNIUM_ORE = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ETERNIUM_ORE_BLOCK.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_ETERNIUM_ORE_BLOCK.get().defaultBlockState()));

    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_VAULTSTEEL_ORE = List.of(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.VAULT_STEEL_ORE_BLOCK.get().defaultBlockState()));

    public static final List<OreConfiguration.TargetBlockState> NETHER_VAULTSTEEL_ORE = List.of( OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES,  ModBlocks.NETHER_VAULT_STEEL_ORE_BLOCK.get().defaultBlockState()));

    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_ILUSIUM_ORE = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ILUSIUM_ORE_BLOCK.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_ILUSIUM_ORE_BLOCK.get().defaultBlockState()));


    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ETERNIUM_ORE = FeatureUtils.register("eternium_ore_block", Feature.ORE, new OreConfiguration(OVERWORLD_ETERNIUM_ORE, 4));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> VAULTSTEEL_ORE = FeatureUtils.register("vault_steel_ore_block", Feature.ORE, new OreConfiguration(OVERWORLD_VAULTSTEEL_ORE, 2));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ILUSIUM_ORE = FeatureUtils.register("ilusium_ore_block", Feature.ORE, new OreConfiguration(OVERWORLD_ILUSIUM_ORE, 8));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> VAULTSTEEL_ORE_NETHER = FeatureUtils.register("nether_vault_steel_ore_block", Feature.ORE, new OreConfiguration(NETHER_VAULTSTEEL_ORE, 2));

    public static final Holder<PlacedFeature> ETERNIUM_ORE_PLACED = PlacementUtils.register("eternium_ore_placed",
            ETERNIUM_ORE, ModdedOrePlacement.rareOrePlacement(3, // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(20))));

    public static final Holder<PlacedFeature> ILUSIUM_ORE_PLACED = PlacementUtils.register("ilusium_ore_placed", ILUSIUM_ORE, ModdedOrePlacement.commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(30))));

    public static final Holder<PlacedFeature> VAULT_STEEL_ORE_PLACED = PlacementUtils.register("vaultsteel_ore_placed", VAULTSTEEL_ORE, ModdedOrePlacement.rareOrePlacement(4, // Veins per chunk
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(-63), VerticalAnchor.absolute(-50))));

    public static final Holder<PlacedFeature> NETHER_VAULTSTEEL_ORE_PLACED = PlacementUtils.register("nether_vaultsteel_ore_placed", VAULTSTEEL_ORE_NETHER, ModdedOrePlacement.rareOrePlacement(5, // Veins per chunk
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(1), VerticalAnchor.absolute(16))));


    @SubscribeEvent
    public static void generateOres(final BiomeLoadingEvent ev)
    {
            
        List<Holder<PlacedFeature>> base = ev.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        base.add(ETERNIUM_ORE_PLACED);
        base.add(VAULT_STEEL_ORE_PLACED);
        base.add(NETHER_VAULTSTEEL_ORE_PLACED);
        base.add(ILUSIUM_ORE_PLACED);
    }
}
