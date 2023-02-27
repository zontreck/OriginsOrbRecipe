package dev.zontreck.otemod.commands.teleport;

import com.mojang.brigadier.CommandDispatcher;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class RTPCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("rtp").executes(c->rtp(c.getSource())));
        
        //executes(c -> doCancel(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    private static int rtp(CommandSourceStack source) {

        /*if(!CommandRegistry.canUse("rtp")) {
            ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.translatable("dev.zontreck.otemod.msgs.command_cooling_down").append(Component.literal(""+CommandRegistry.getRemaining("rtp"))).append(Component.translatable("dev.zontreck.otemod.msgs.command_cooling_down_seconds")), source.getServer());

            // exit
            //return 0; // Removed until the player data registry is implemented
        }
        CommandRegistry.markUsed("rtp");*/
        if(!(source.getEntity() instanceof Player)){
            return 1;
        }
        final ServerPlayer pla = (ServerPlayer)source.getEntity();
        
        final TeleportContainer cont = new TeleportContainer(pla, Vec3.ZERO, pla.getRotationVector(), source.getLevel());
        


        Thread tx = new Thread(new Runnable() {
            @Override
            public void run(){
                // We can now execute the loop to search for a safe spot!
                Vector3 v = new Vector3();
                // RTP is not designed to be safe really, but we at least want to check if where we are putting the player is air
                
                Vec3 pos = pla.position();
                
                boolean found_place= false;
                RTPContainer container = RandomPositionFactory.beginRTPSearch(pla, pos, pla.getRotationVector(), pla.getLevel());
                while(!container.complete)
                {
                    if(!OTEMod.ALIVE)
                    {
                        container.aborted=true;
                        container.containingThread.interrupt();
                        return;
                    }
                    if(container.tries>30)
                    {
                        // abort!
                        return;

                    }
                }
                v = container.container.world_pos.Position;
        
                ChatServerOverride.broadcastTo(pla.getUUID(), new TextComponent(ChatColor.DARK_GRAY + "["+ChatColor.DARK_GREEN + "OTEMOD" + ChatColor.DARK_GRAY + "] "+ChatColor.DARK_PURPLE+" A suitable location has been found. Wormhole opening now!"), OTEMod.THE_SERVER);
        
                // Apply the effect
                TeleportActioner.ApplyTeleportEffect(pla);
                cont.Position=v.asMinecraftVector();
        
                TeleportActioner.PerformTeleport(cont);
        
            }
        });

        tx.start();


        return 0;
    }

}
