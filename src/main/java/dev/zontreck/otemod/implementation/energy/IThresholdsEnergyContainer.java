package dev.zontreck.otemod.implementation.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * This is used to mark a screen as a energy container to automatically send the energy request packets
 */
public interface IThresholdsEnergyContainer
{
    BlockPos getPosition();
    BlockEntity getEntity();
}
