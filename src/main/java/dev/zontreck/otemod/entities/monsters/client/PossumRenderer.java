package dev.zontreck.otemod.entities.monsters.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.entities.monsters.PossumEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PossumRenderer extends GeoEntityRenderer<PossumEntity>
{

    public PossumRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PossumModel());
        this.shadowRadius=0.3f;
    }


    @Override
    public ResourceLocation getTextureLocation(PossumEntity entity)
    {

        return new ResourceLocation(OTEMod.MOD_ID, "textures/entity/possum_texture.png");
    }


    @Override
    public RenderType getRenderType(PossumEntity animatable, float partialTicks, 
                        PoseStack stack, MultiBufferSource buffer,
                        VertexConsumer consumer, int packed, ResourceLocation loc)
    {
        return super.getRenderType(animatable, partialTicks, stack, buffer, consumer, packed, loc);
    }
    
}
