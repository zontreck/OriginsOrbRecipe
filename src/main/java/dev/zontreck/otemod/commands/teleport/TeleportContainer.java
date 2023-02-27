package dev.zontreck.otemod.commands.teleport;

import java.time.Instant;
import java.util.UUID;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class TeleportContainer implements Comparable{
    public UUID FromPlayer;
    public UUID ToPlayer;
    public long StartedAt;
    public UUID TeleportID;
    public WorldPosition world_pos;

    /*
     * The following variables are only used when actioning the teleport itself, and should only be initialized once the teleport is about to engage
     */
    public ServerPlayer PlayerInst;
    public Vec3 Position;
    public Vec2 Rotation;
    public ServerLevel Dimension;
    
    
    public boolean has_expired(){
        if(Instant.now().getEpochSecond() > (StartedAt + (60)))
        {
            return true;
        }else return false;
    }

    public TeleportContainer (UUID From, UUID To)
    {
        FromPlayer = From;
        ToPlayer=To;
        TeleportID = UUID.randomUUID();

        StartedAt = Instant.now().getEpochSecond();
    }

    public TeleportContainer(ServerPlayer f_p, Vec3 f_pos, Vec2 f_rot, ServerLevel f_dim) {
        SetTeleportDestination(f_p, f_pos, f_rot, f_dim);
        world_pos = new WorldPosition(new Vector3(f_pos), f_dim);
    }

    private void SetTeleportDestination(ServerPlayer f_p, Vec3 f_pos, Vec2 f_rot, ServerLevel f_dim) {
        PlayerInst = f_p;
        Position = f_pos;
        Rotation = f_rot;
        Dimension = f_dim;
        world_pos = new WorldPosition(new Vector3(f_pos), f_dim);
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof TeleportContainer){
            TeleportContainer cont = (TeleportContainer)o;
            if(cont.FromPlayer != FromPlayer){
                return -1;
            }else {
                if(cont.ToPlayer != ToPlayer)return 1;
                else return 0;
            }
        }else return -1;
    }
}
