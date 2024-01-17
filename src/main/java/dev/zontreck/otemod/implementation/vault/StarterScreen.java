package dev.zontreck.otemod.implementation.vault;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class StarterScreen extends AbstractContainerScreen <StarterMenu>
{
    // 176x224
    public final UUID VaultMenuID;
    public final Player thePlayer;
    public final StarterMenu THE_CONTAINER;

    private static final ResourceLocation TEXTURE = new ResourceLocation(OTEMod.MOD_ID, "textures/gui/vault.png");

    public StarterScreen(StarterMenu container, Inventory playerInv, Component comp){
        super(container, playerInv, comp);
        thePlayer=playerInv.player;

        this.VaultMenuID = container.VaultMenuID;
        this.leftPos = 0;
        this.topPos = 0;
        this.THE_CONTAINER=container;

        this.imageWidth = 207;
        this.imageHeight = 238;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);

    }

    @Override
    protected void init()
    {
        super.init();
        this.inventoryLabelY = 12;
        this.inventoryLabelX = 63;

    }


    @Override
    protected void renderBg(PoseStack pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY)
    {
        renderBackground(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor (1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        blit(pGuiGraphics, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
