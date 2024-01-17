package dev.zontreck.otemod.implementation.energy.screenrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */
public class EnergyInfoArea extends InfoArea {
    private final IEnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null,8,64);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy)  {
        this(xMin, yMin, energy,8,64);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        return List.of(ChatHelpers.macro(energy.getEnergyStored()+"/"+energy.getMaxEnergyStored()+" FE"));
    }

    @Override
    public void draw(PoseStack transform) {
        final int height = area.getHeight();
        final int width = area.getWidth();
        int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));


        if(area.getHeight() > area.getWidth())
            fillGradient(transform, area.getX(), area.getY() + (height + stored), area.getX() + area.getWidth(), area.getY() + area.getHeight(), 0xff0000, 0xff550000);
        else fillGradient(transform, area.getX() + (width + stored), area.getY(),area.getX() + area.getWidth(), area.getY() + area.getHeight(), 0xff0000, 0xff005500);
    }
}
