package dev.zontreck.otemod.commands.teleport;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.PlayerFlyCache;
import dev.zontreck.otemod.implementation.DelayedExecutorService;

public class TeleportRunnable implements Runnable
{
    
    public final TeleportContainer Action;
    public TeleportRunnable(TeleportContainer cont){
        Action = cont;
    }

    @Override
    public void run() {
        final PlayerFlyCache c = PlayerFlyCache.cachePlayer(Action.PlayerInst);
        Action.PlayerInst.teleportTo(Action.Dimension, Action.Position.x, Action.Position.y, Action.Position.z, Action.Rotation.y, Action.Rotation.x);

        OTEMod.delayedExecutor.schedule(new Runnable(){
            public PlayerFlyCache cached = c;
            public TeleportContainer container=Action;
            @Override
            public void run()
            {
                c.Assert(container.PlayerInst);
                container.PlayerInst.setPos(container.Position);
                container.PlayerInst.giveExperiencePoints(1);
            }
        }, 1);
    }
}
