package dev.zontreck.otemod.ore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Modifier 
{

    
    public static void DoProcess(GatherDataEvent ev) {
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
