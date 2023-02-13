package dev.zontreck.otemod.entities.monsters.client;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.entities.monsters.PossumEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PossumModel extends AnimatedGeoModel<PossumEntity>
{

    @Override
    public ResourceLocation getAnimationResource(PossumEntity animatable) {
        return new ResourceLocation(OTEMod.MOD_ID, "animations/possum.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(PossumEntity object) {
        return new ResourceLocation(OTEMod.MOD_ID, "geo/possum.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PossumEntity object) {
        return new ResourceLocation(OTEMod.MOD_ID, "textures/entity/possum_texture.png");
    }

}