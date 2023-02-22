package dev.zontreck.otemod.networking;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.networking.packets.EnergySyncS2CPacket;
import dev.zontreck.otemod.networking.packets.OpenVaultC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int PACKET_ID = 0;
    private static int id()
    {
        return PACKET_ID++;
    }

    public static void register()
    {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(OTEMod.MOD_ID, "messages"))
           .networkProtocolVersion(()-> "1.0")
            .clientAcceptedVersions(s->true)
            .serverAcceptedVersions(s->true)
            .simpleChannel();
        
        INSTANCE=net;

        net.messageBuilder(OpenVaultC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(OpenVaultC2SPacket::new)
            .encoder(OpenVaultC2SPacket::toBytes)
            .consumer(OpenVaultC2SPacket::handle)
            .add();

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(EnergySyncS2CPacket::new)
            .encoder(EnergySyncS2CPacket::toBytes)
            .consumer(EnergySyncS2CPacket::handle)
            .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player), message);
    }

    public static <MSG> void sendToAll(MSG message)
    {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
