package dev.zontreck.otemod.implementation.uncrafting;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.MouseHelpers;
import dev.zontreck.otemod.implementation.compressor.CompressionChamberMenu;
import dev.zontreck.otemod.implementation.energy.screenrenderer.EnergyInfoArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class UncrafterScreen extends AbstractContainerScreen<UncrafterMenu>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(OTEMod.MOD_ID, "textures/gui/uncrafter.png");

    private EnergyInfoArea EIA;

    public UncrafterScreen(UncrafterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.topPos=0;
        this.leftPos=0;

        this.imageWidth=198;
        this.imageHeight=167;
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyArea();
    }

    private void assignEnergyArea() {
        int x = (width - imageWidth )/2;
        int y = (height - imageHeight)/2;

        EIA = new EnergyInfoArea(x+186, y+143, menu.entity.getEnergyStorage(), 5, 63);
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
        if(isMouseAbove(mouseX, mouseY, x, y, leftPos + 182, topPos + 126, 5, 63)){
            stack.renderTooltip(font, EIA.getTooltips(), Optional.empty(), mouseX-x, mouseY-y);
        }
    }


    private void renderCraftingProgress(GuiGraphics stack)
    {
        if(menu.isCrafting())
        {
            stack.blit(TEXTURE, leftPos+68, topPos+52, 2, 210, menu.getScaledProgress(),9);
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
