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
        
        // 10/05/2022 - Thinking ahead here to future proof it so i can do things in threads safely
        // By adding this task onto the main server thread, any thread can call the TeleportActioner and it will be actioned on the main thread without needing to repeat the process of sending this to the server thread.
        player.server.execute(new Runnable(){
            public void run(){

                MobEffectInstance inst = new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, true, true);
                MobEffectInstance regen = new MobEffectInstance(MobEffects.REGENERATION, 200, 2, true, true);

                // 10-05-2022   - Adjusted to 100 on duration due to a small issue where it would sometimes stop levitation prior to the teleport taking effect.
                // Small tradeoff is the player now levitates slightly longer at the destination. This is acceptable. Compensated by increasing regen strength by 1
                MobEffectInstance invul = new MobEffectInstance(MobEffects.LEVITATION, 100, 1, true, true);
        
                
                player.addEffect(inst);
                player.addEffect(regen);
                player.addEffect(invul); // ensure the player can't fall into lava in the short time we are not in control (if the player was silly enough to make a home above lava!!!)
                
            }
        });
    }
}
