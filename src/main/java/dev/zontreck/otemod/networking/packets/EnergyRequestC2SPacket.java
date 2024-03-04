package dev.zontreck.otemod.networking.packets;

import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.implementation.energy.IThresholdsEnergy;
import dev.zontreck.otemod.networking.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class EnergyRequestC2SPacket
{
    private WorldPosition position;
    private UUID player;

    public EnergyRequestC2SPacket(WorldPosition position, Player player)
    {
        this.position=position;
        this.player=player.getUUID();
    }

    public EnergyRequestC2SPacket(FriendlyByteBuf buf)
    {
        try {
            position = new WorldPosition(buf.readAnySizeNbt(), false);
        } catch (InvalidDeserialization e) {
            throw new RuntimeException(e);
        }
        player = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeNbt(position.serialize());
        buf.writeUUID(player);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(()->
        {
            if(position==null)return;
            BlockPos pos = position.Position.asBlockPos();
            BlockEntity entity = position.getActualDimension().getBlockEntity(pos);
            if(entity instanceof IThresholdsEnergy ite)
            {
                int energy = ite.getEnergy();
                ModMessages.sendToPlayer(new EnergySyncS2CPacket(energy, pos), ServerUtilities.getPlayerByID(player.toString()));
            }
        });
    }

}
