package dev.zontreck.otemod.configs;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;

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
        Abilities playerAbilities = play.getAbilities();
        playerAbilities.flying=Flying;
        playerAbilities.mayfly=FlyEnabled;

        play.onUpdateAbilities();
    }
}