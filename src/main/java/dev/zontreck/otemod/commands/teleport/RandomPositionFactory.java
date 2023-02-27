package dev.zontreck.otemod.commands.teleport;

import dev.zontreck.otemod.implementation.DelayedExecutorService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * The factory system used to start searching for a random teleport position
 */
public class RandomPositionFactory {

    public static RTPContainer beginRTPSearch(ServerPlayer player, Vec3 pos, Vec2 rot, ServerLevel level)
    {
        RTPContainer contain= new RTPContainer(player, pos, rot, level);
        Thread tx = new Thread(new RandomPositionLocator(contain));
        tx.setName("RTPTask-"+String.valueOf(DelayedExecutorService.getNext()));
        tx.start();

        return contain;
    }
}
