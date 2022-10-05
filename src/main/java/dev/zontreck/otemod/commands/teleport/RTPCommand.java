package dev.zontreck.otemod.commands.teleport;

import java.util.Random;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatColor;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.containers.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
        ServerPlayer pla = source.getPlayer();
        TeleportContainer cont = new TeleportContainer(pla, null, source.getPlayer().getRotationVector(), source.getLevel());
        
        Vector3 v = new Vector3();
        // RTP is not designed to be safe really, but we at least want to check if where we are putting the player is air
        
        Vec3 pos = pla.position();
        
        boolean found_place= false;

        int tries=0;

        while(!found_place){
            // Take our current position, and send us in a random direction
            Random rng = new Random((long) (pos.x+pos.y+pos.z));
            v.y = 500;
            v.x = rng.nextDouble(0xffff);
            v.z = rng.nextDouble(0xffff);
            
            // Begin to scan for ground
            while(v.y != 0)
            {
                // check block above and below
                BlockState b = source.getLevel().getBlockState(new BlockPos(v.asMinecraftVector()));
                BlockState b2 = source.getLevel().getBlockState(new BlockPos(v.moveUp().asMinecraftVector()));
                BlockState b3 = source.getLevel().getBlockState(new BlockPos(v.moveDown().asMinecraftVector()));
                Block bx = b.getBlock();
                
                if(b.isAir()){
                    if(b2.isAir()){
                        if(!b3.isAir()){
                            found_place = true;
                            break;
                        }
                    }
                }

                v =v.moveDown();

            }
            if(tries>=5)
            {
                // Aborting RTP
                ChatServerOverride.broadcastTo(pla.getUUID(), Component.literal(ChatColor.DARK_RED+"Could not find a suitable location after 5 tries. Giving up on RTP"), source.getServer());
                return 0;
            }
            tries++;
        }

        ChatServerOverride.broadcastTo(pla.getUUID(), Component.literal(ChatColor.DARK_GRAY + "["+ChatColor.DARK_GREEN + "OTEMOD" + ChatColor.DARK_GRAY + "] "+ChatColor.DARK_PURPLE+" A suitable location has been found. Wormhole opening now!"), source.getServer());

        // Apply the effect
        TeleportActioner.ApplyTeleportEffect(pla);
        cont.Position=v.asMinecraftVector();

        TeleportActioner.PerformTeleport(cont);


        return 0;
    }

    private static int doCancel(CommandSourceStack source, String TPID) {
        UUID teleporter = UUID.fromString(TPID);
        for(TeleportContainer cont : OTEMod.TeleportRegistry){
            if(cont.TeleportID.equals(teleporter)){
                // Accepting!

                ServerPlayer from = source.getServer().getPlayerList().getPlayer(cont.FromPlayer);
                ServerPlayer to = source.getServer().getPlayerList().getPlayer(cont.ToPlayer);

                Component comp = Component.literal(ChatColor.DARK_GRAY + "["+ ChatColor.DARK_GREEN+ "OTEMOD" + ChatColor.DARK_GRAY+"] " + ChatColor.DARK_PURPLE+"Teleport request was accepted. Opening wormhole!");

                ChatServerOverride.broadcastTo(cont.FromPlayer, comp, source.getServer());
                ChatServerOverride.broadcastTo(cont.ToPlayer, comp, source.getServer());

                OTEMod.TeleportRegistry.remove(cont);


                cont.PlayerInst = from;
                cont.Position = to.position();
                cont.Rotation = to.getRotationVector();
                cont.Dimension = to.getLevel();

                TeleportActioner.ApplyTeleportEffect(from);
                TeleportActioner.PerformTeleport(cont);
                return 0;
            }
        }

        Component comp = Component.literal(ChatColor.DARK_RED+"The teleport was not found, perhaps the request expired or was already cancelled/denied");

        ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), comp, source.getServer());

        return 0;
    }
}
