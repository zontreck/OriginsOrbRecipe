package dev.zontreck.otemod.commands.teleport;

import dev.zontreck.otemod.configs.PlayerFlyCache;

public class TeleportRunnable implements Runnable
{
    
    public final TeleportContainer Action;
    public TeleportRunnable(TeleportContainer cont){
        Action = cont;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        PlayerFlyCache c = PlayerFlyCache.cachePlayer(Action.PlayerInst);
        Action.PlayerInst.teleportTo(Action.Dimension, Action.Position.x, Action.Position.y, Action.Position.z, Action.Rotation.y, Action.Rotation.x);

        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.Assert(Action.PlayerInst);

        Action.PlayerInst.setPos(Action.Position);
    }
}
