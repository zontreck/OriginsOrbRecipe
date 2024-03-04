package dev.zontreck.otemod.networking.packets;

import java.util.function.Supplier;

import dev.zontreck.otemod.blocks.entity.CompressionChamberBlockEntity;
import dev.zontreck.otemod.blocks.entity.ItemScrubberBlockEntity;
import dev.zontreck.otemod.blocks.entity.MagicalScrubberBlockEntity;
import dev.zontreck.otemod.blocks.entity.UncrafterBlockEntity;
import dev.zontreck.otemod.implementation.energy.IThresholdsEnergy;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class EnergySyncS2CPacket {
    private int energy;
    private BlockPos pos;
    
    public EnergySyncS2CPacket(int amount, BlockPos pos)
    {
        this.energy=amount;
        this.pos=pos;
    }

    public EnergySyncS2CPacket(FriendlyByteBuf buf)
    {
        energy = buf.readInt();
        pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(()->
        {
            // WE ARE NOW ON THE CLIENT
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof IThresholdsEnergy entity)
            {
                entity.setEnergy(energy);
            }

        });
    }
}
