package dev.zontreck.otemod.implementation.scrubber;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScrubberScreen extends AbstractContainerScreen<ScrubberMenu>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(OTEMod.MOD_ID, "textures/gui/item_scrubber_gui.png");

    public ScrubberScreen(ScrubberMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);

        
        this.topPos=0;
        this.leftPos=0;
        
        this.imageWidth = 207;
        this.imageHeight = 164;
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        

        blit(poseStack, this.leftPos, this.topPos, 0,0, imageWidth, imageHeight);
        renderUncraftingProgress(poseStack);
    }

    
    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
    {
        this.font.draw(stack, this.title.getString(), 63, 12, 0xFFFFFF);
        
        //this.font.draw(stack, this.playerInventoryTitle.getString(), this.leftPos + 17, this.topPos + 123, 0xFFFFFF);
    }

    private void renderUncraftingProgress(PoseStack stack)
    {
        if(menu.isCrafting())
        {
            blit(stack, leftPos+42, topPos+45, 1, 168, menu.getScaledProgress(),6);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }
    
}
