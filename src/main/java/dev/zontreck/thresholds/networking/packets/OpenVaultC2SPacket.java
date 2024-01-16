package dev.zontreck.thresholds.networking.packets;

import java.util.function.Supplier;

import dev.zontreck.thresholds.commands.vaults.VaultCommand;
import dev.zontreck.thresholds.implementation.vault.VaultContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

// This packet is only ever sent from the client to the server when requesting to open vaults using the EaseOfUse Buttons
public class OpenVaultC2SPacket {
    private int vault=0;
    private boolean change = false; // This is set to true when going previous or next.
    private int changeDir = 0; // This is only in the packet when change is true. This is either a 1 or a -1.
    public OpenVaultC2SPacket(int vaultNum, boolean change, int changeDir)
    {
        this.vault = vaultNum;
        this.change = change;
        this.changeDir=changeDir;
    }

    public OpenVaultC2SPacket(FriendlyByteBuf buf)
    {
        this.change = buf.readBoolean();
        if(this.change)
            this.changeDir=buf.readInt();
        else 
            this.vault = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBoolean(change);
        if(change) buf.writeInt(changeDir);
        else buf.writeInt(vault);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(()->{
            // On server now
            ServerPlayer player = ctx.getSender();

            if(change){
                if(VaultContainer.VAULT_REGISTRY.containsKey(player.getUUID())){
                    VaultContainer cont = VaultContainer.VAULT_REGISTRY.get(player.getUUID());
                    vault = cont.VAULT_NUMBER + changeDir;
                }
            }

            if(vault < 0)vault=0;


            VaultCommand.doOpen(player, vault);
        });

        return true;
    }

}
