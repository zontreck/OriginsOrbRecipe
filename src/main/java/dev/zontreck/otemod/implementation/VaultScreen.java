package dev.zontreck.otemod.implementation;

import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class VaultScreen extends AbstractContainerScreen <VaultMenu>
{
    // 176x224
    public final UUID VaultMenuID;

    private static final ResourceLocation TEXTURE = new ResourceLocation(OTEMod.MOD_ID, "textures/gui/vault.png");

    public VaultScreen(VaultMenu container, Inventory playerInv, Component comp){
        super(container, playerInv, comp);

        this.VaultMenuID = container.VaultMenuID;
        this.leftPos = 0;
        this.topPos = 0;

        this.imageWidth = 176;
        this.imageHeight = 224;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.font.draw(stack, this.title, this.leftPos + 17, this.topPos + 15, 0xFFFFFF);
        this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 17, this.topPos + 123, 0xFFFFFF);
    }

    @Override
    protected void init()
    {
        super.init();
        // This is where custom controls would be added!
    }

    @Override
    protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks)
    {
        renderBackground(stack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor (1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels (PoseStack stack, int mouseX, int mouseY)
    {

    }
}
