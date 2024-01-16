package dev.zontreck.thresholds.implementation.compressor;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.implementation.MouseHelpers;
import dev.zontreck.thresholds.implementation.energy.screenrenderer.EnergyInfoArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class CompressionChamberScreen extends AbstractContainerScreen<CompressionChamberMenu> {


    private static final ResourceLocation TEXTURE = new ResourceLocation(ThresholdsMod.MOD_ID, "textures/gui/energized_compression_chamber.png");

    private EnergyInfoArea EIA;


    public CompressionChamberScreen(CompressionChamberMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.topPos=0;
        this.leftPos=0;

        this.imageWidth=176;
        this.imageHeight=177;
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyArea();
    }

    private void assignEnergyArea() {
        int x = (width - imageWidth )/2;
        int y = (height - imageHeight)/2;

        EIA = new EnergyInfoArea(x+63, y+46, menu.entity.getEnergyStorage(), 39, 6);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);


        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0,0, imageWidth, imageHeight);
        renderCraftingProgress(guiGraphics);
        EIA.draw(guiGraphics);
    }


    @Override
    protected void renderLabels(GuiGraphics stack, int mouseX, int mouseY)
    {
        stack.drawString(font, this.title.getString(), 32, 4, 0xFFFFFF);

        int x = (width - imageWidth )/2;
        int y = (height - imageHeight)/2;
        renderEnergy(stack, mouseX, mouseY, x, y);
        //this.font.draw(stack, this.playerInventoryTitle.getString(), this.leftPos + 17, this.topPos + 123, 0xFFFFFF);
    }

    private void renderEnergy(GuiGraphics stack, int mouseX, int mouseY, int x, int y) {
        if(isMouseAbove(mouseX, mouseY, x, y, 63, 46, 39, 6)){
            stack.renderTooltip(font, EIA.getTooltips(), Optional.empty(), mouseX-x, mouseY-y);
        }
    }


    private void renderCraftingProgress(GuiGraphics stack)
    {
        if(menu.isCrafting())
        {
            stack.blit(TEXTURE, leftPos+63, topPos+34, 179, 11, menu.getScaledProgress(),6);
        }
    }

    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float delta)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }

    private boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int offsetX, int offsetY, int width, int height)
    {
        return MouseHelpers.isMouseOver(mouseX, mouseY, x+offsetX, y+offsetY, width, height);
    }
}
