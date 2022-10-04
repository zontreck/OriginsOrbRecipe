package dev.zontreck.otemod.configs;

import net.minecraft.server.level.ServerPlayer;

public class PlayerFlyCache
{
    public boolean FlyEnabled;
    public boolean Flying;
    public static PlayerFlyCache cachePlayer(ServerPlayer play){
        PlayerFlyCache cache = new PlayerFlyCache();
        cache.FlyEnabled = play.getAbilities().mayfly;
        cache.Flying = play.getAbilities().flying;

        play.onUpdateAbilities();

        return cache;
    }

    public void Assert(ServerPlayer play){

    }
}