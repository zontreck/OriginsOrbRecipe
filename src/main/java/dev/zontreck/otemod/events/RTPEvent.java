package dev.zontreck.otemod.events;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class RTPEvent extends Event
{
    public ServerPlayer player;
    public WorldPosition position;

    public RTPEvent(ServerPlayer player, WorldPosition position)
    {
        this.player=player;
        this.position=position;

    }
}
