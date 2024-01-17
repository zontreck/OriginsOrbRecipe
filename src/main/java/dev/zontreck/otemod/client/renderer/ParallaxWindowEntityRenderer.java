package dev.zontreck.otemod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import dev.zontreck.otemod.blocks.entity.ParallaxWindowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;

public class ParallaxWindowEntityRenderer implements BlockEntityRenderer<ParallaxWindowEntity>
{

    @Override
    public void render(ParallaxWindowEntity parallaxWindowEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

        Matrix4f m4f = poseStack.last().pose();
        var renderType = switch (parallaxWindowEntity.getSkyType()) {

            case Level1 -> RenderType.endPortal();
        };
        renderCube(parallaxWindowEntity, m4f, multiBufferSource.getBuffer(renderType));
    }


    private void renderCube(ParallaxWindowEntity entity, Matrix4f matrix, VertexConsumer buffer) {
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, Direction.SOUTH);
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Direction.NORTH);
        renderFace(entity, matrix, buffer, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.EAST);
        renderFace(entity, matrix, buffer, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, Direction.WEST);
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, Direction.DOWN);
        renderFace(entity, matrix, buffer, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, Direction.UP);
    }

    private void renderFace(ParallaxWindowEntity entity, Matrix4f matrix, VertexConsumer buffer, float f, float g, float h, float i, float j, float k, float l, float m, Direction direction) {
        if (entity.shouldRenderFace(direction)) {
            buffer.vertex(matrix, f, h, j).endVertex();
            buffer.vertex(matrix, g, h, k).endVertex();
            buffer.vertex(matrix, g, i, l).endVertex();
            buffer.vertex(matrix, f, i, m).endVertex();
        }
    }
    @Override
    public int getViewDistance() {
        return 256;
    }
}
