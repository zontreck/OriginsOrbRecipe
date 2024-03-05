package dev.zontreck.otemod.networking.packets;

import dev.zontreck.libzontreck.LibZontreck;
import dev.zontreck.libzontreck.exceptions.InvalidDeserialization;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.implementation.energy.IThresholdsEnergy;
import dev.zontreck.otemod.networking.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Iterator;
import java.util.UUID;
import java.util.function.Supplier;

public class EnergyRequestC2SPacket
{
    private BlockPos position;
    private String dimension;
    private UUID player;

    public EnergyRequestC2SPacket(BlockPos position, Level level, Player player)
    {
        this.position=position;
        dimension = level.dimension().location().getNamespace() + ":" + level.dimension().location().getPath();
        this.player=player.getUUID();
    }

    public EnergyRequestC2SPacket(FriendlyByteBuf buf)
    {
        position = buf.readBlockPos();
        dimension = buf.readUtf();
        player = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(position);
        buf.writeUtf(dimension);
        buf.writeUUID(player);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(()->
        {
            if(position==null)return;
            BlockPos pos = position;
            ServerLevel lvl = getActualDimension();
            BlockEntity entity = lvl.getBlockEntity(pos);
            if(entity instanceof IThresholdsEnergy ite)
            {
                int energy = ite.getEnergy();
                ModMessages.sendToPlayer(new EnergySyncS2CPacket(energy, pos), ServerUtilities.getPlayerByID(player.toString()));
            }
        });
    }


    public ServerLevel getActualDimension() {
        String dim = this.dimension;
        String[] dims = dim.split(":");
        ResourceLocation rl = new ResourceLocation(dims[0], dims[1]);
        ServerLevel dimL = null;
        Iterator var5 = ServerLifecycleHooks.getCurrentServer().getAllLevels().iterator();

        while(var5.hasNext()) {
            ServerLevel lServerLevel = (ServerLevel)var5.next();
            ResourceLocation XL = lServerLevel.dimension().location();
            if (XL.getNamespace().equals(rl.getNamespace()) && XL.getPath().equals(rl.getPath())) {
                dimL = lServerLevel;
            }
        }

        if (dimL == null) {
            LibZontreck.LOGGER.error("DIMENSION COULD NOT BE FOUND : " + this.dimension);
            return null;
        } else {
            return dimL;
        }
    }

}
