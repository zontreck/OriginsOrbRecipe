// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package dev.zontreck.otemod.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PossumEntity<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "possum"), "main");
	private final ModelPart tail;
	private final ModelPart body;
	private final ModelPart feet;
	private final ModelPart head;
	private final ModelPart ears;

	public PossumEntity(ModelPart root) {
		this.tail = root.getChild("tail");
		this.body = root.getChild("body");
		this.feet = root.getChild("feet");
		this.head = root.getChild("head");
		this.ears = root.getChild("ears");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(10, 29).addBox(-12.0F, -5.0F, -9.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 29).addBox(-12.0F, -4.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 25).addBox(-12.0F, -2.0F, -11.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 25).addBox(-12.0F, -1.0F, -12.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 25).addBox(-11.0F, -2.0F, -12.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 25).addBox(-12.0F, -1.0F, -13.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-13.0F, -4.0F, -10.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-12.0F, -4.0F, -9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 25).addBox(-12.0F, -4.0F, -11.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-14.0F, -3.0F, -12.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-12.0F, -2.0F, -13.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 31).addBox(-13.0F, -1.0F, -16.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 29).addBox(-13.0F, -2.0F, -14.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 25).addBox(-13.0F, 2.0F, -18.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 25).addBox(-12.0F, -1.0F, -15.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-13.0F, 0.0F, -17.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-12.0F, 1.0F, -17.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-13.0F, 1.0F, -18.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 31).addBox(-14.0F, 1.0F, -18.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 23.0F, 10.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(14, 14).addBox(-4.0F, -7.0F, 6.0F, 7.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -7.75F, -2.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(20, 19).addBox(-4.0F, -7.0F, -3.0F, 7.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition feet = partdefinition.addOrReplaceChild("feet", CubeListBuilder.create().texOffs(26, 0).addBox(-5.0F, -2.0F, -1.9F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(22, 24).addBox(-5.0F, -2.0F, 3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(26, 4).addBox(1.0F, -2.0F, -1.9F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(23, 28).addBox(1.0F, -2.0F, 3.1F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(2.0F, -12.0F, -1.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(6.0F, -10.0F, 0.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(31, 24).addBox(7.0F, -9.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 24).addBox(1.0F, -11.0F, 0.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition ears = partdefinition.addOrReplaceChild("ears", CubeListBuilder.create().texOffs(30, 14).addBox(3.0F, -13.0F, -2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(3.0F, -13.0F, 4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		feet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ears.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}