package dev.zontreck.otemod.commands.teleport;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class TeleportActioner
{
    public static void PerformTeleport(TeleportContainer contain){
        //sub_runnable run = new sub_runnable(contain);
        Thread t = new Thread(new TeleportRunnable(contain));
        t.start();
    }

    public static void ApplyTeleportEffect(ServerPlayer player){
        

        MobEffectInstance inst = new MobEffectInstance(MobEffects.DARKNESS, 200, 1, true, true);
        MobEffectInstance regen = new MobEffectInstance(MobEffects.REGENERATION, 200, 1, true, true);
        MobEffectInstance invul = new MobEffectInstance(MobEffects.LEVITATION, 75, 1, true, true);

        
        player.addEffect(inst);
        player.addEffect(regen);
        player.addEffect(invul); // ensure the player can't fall into lava in the short time we are not in control (if the player was silly enough to make a home above lava!!!)
        
    }
}
