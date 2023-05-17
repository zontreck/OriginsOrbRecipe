package dev.zontreck.otemod.implementation.vault;

import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.commands.vaults.VaultCommand;
import dev.zontreck.otemod.networking.ModMessages;
import dev.zontreck.otemod.networking.packets.OpenVaultC2SPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;

public class VaultScreen extends AbstractContainerScreen <VaultMenu>
{
    // 176x224
    public final UUID VaultMenuID;
    public final Player thePlayer;
    public final VaultMenu THE_CONTAINER;

    private static final ResourceLocation TEXTURE = new ResourceLocation(OTEMod.MOD_ID, "textures/gui/vault.png");

    public VaultScreen(VaultMenu container, Inventory playerInv, Component comp){
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
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
    {
        this.font.draw(stack, this.title, 63, 12, 0xFFFFFF);
        this.font.draw(stack, this.playerInventoryTitle, 63, 146, 0xFFFFFF);
        //this.font.draw(stack, this.title.getString(), this.leftPos + 17, this.topPos + 15, 0xFFFFFF);
        
        //this.font.draw(stack, this.playerInventoryTitle.getString(), this.leftPos + 17, this.topPos + 123, 0xFFFFFF);
    }

    @Override
    protected void init()
    {
        super.init();
        // This is where custom controls would be added!

        this.addWidget(Button.builder(Component.literal(""), (BTN)->{
            thePlayer.closeContainer();
            ModMessages.sendToServer(new OpenVaultC2SPacket(0, true, -1));
        }).size(16,16).pos(this.leftPos+7, this.topPos+86).build());


        this.addWidget(Button.builder(Component.literal(""), (BTN)->{
            thePlayer.closeContainer();
            ModMessages.sendToServer(new OpenVaultC2SPacket(0, true, 1));
        }).size(16,16).pos(this.leftPos+187, this.topPos+84).build());

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
}
