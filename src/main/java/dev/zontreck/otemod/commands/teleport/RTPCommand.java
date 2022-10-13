package dev.zontreck.otemod.commands.teleport;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.containers.Vector3;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RTPCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("rtp").executes(c->rtp(c.getSource(), false)).then(Commands.argument("ignorewater", BoolArgumentType.bool()).executes(c->rtp(c.getSource(), BoolArgumentType.getBool(c, "ignorewater")))));
        
        //executes(c -> doCancel(c.getSource())));
        
        //dispatcher.register(Commands.literal("sethome").then(Commands.argument("nickname", StringArgumentType.string())).executes(command -> {
            //String arg = StringArgumentType.getString(command, "nickname");
            //return setHome(command.getSource(), arg);
        //}));
    }

    public static Vector3 findPosition(ServerLevel lvl, boolean allowWater)
    {
        Vector3 v = new Vector3();
        boolean found_place = false;
        // We do not care how many tries it takes
        
        while (!found_place)
        {
    
            // Take our current position, and send us in a random direction
            Random rng = new Random(Instant.now().getEpochSecond());
            v.y = 300;
            v.x = rng.nextDouble(0xffff);
            v.z = rng.nextDouble(0xffff);

            boolean is_invalid_location = false;
            String block_place="";

            while(v.y!=-30)
            {

                BlockState b = lvl.getBlockState(new BlockPos(v.asMinecraftVector()));
                BlockState b2 = lvl.getBlockState(new BlockPos(v.moveUp().asMinecraftVector()));
                BlockState b3 = lvl.getBlockState(new BlockPos(v.moveDown().asMinecraftVector()));

                if(b.isAir()){
                    if(b2.isAir()){
                        if(!b3.isAir())
                        {
                            // Check names
                            boolean valid=true;
                            List<Block> blackLists = new ArrayList<>();
                            if(!allowWater)
                                blackLists.add(Blocks.WATER);
                            blackLists.add(Blocks.LAVA);

                            block_place = b3.getBlock().getName().getString();
                            OTEMod.LOGGER.info(b3.getBlock().getName().getString());
                            for(Block bx : blackLists)
                            {
                                if(b.is(bx) || b2.is(bx) || b3.is(bx)){
                                    valid=false;
                                    is_invalid_location=true;
                                }
                            }

                            if(valid){

                                found_place = true;
                                break;
                            }

                        }
                    }
                }
                v=v.moveDown();
            }
        }

        return v;
    }

    private static int rtp(CommandSourceStack source, boolean allowWater) {

        /*if(!CommandRegistry.canUse("rtp")) {
            ChatServerOverride.broadcastTo(source.getPlayer().getUUID(), Component.translatable("dev.zontreck.otemod.msgs.command_cooling_down").append(Component.literal(""+CommandRegistry.getRemaining("rtp"))).append(Component.translatable("dev.zontreck.otemod.msgs.command_cooling_down_seconds")), source.getServer());

            // exit
            //return 0; // Removed until the player data registry is implemented
        }
        CommandRegistry.markUsed("rtp");*/
        ServerPlayer pla = source.getPlayer();
        TeleportContainer cont = new TeleportContainer(pla, null, source.getPlayer().getRotationVector(), source.getLevel());
        


        Thread tx = new Thread(new Runnable() {
            public void run(){
                // We can now execute the loop to search for a safe spot!
                Vector3 v = new Vector3();
                // RTP is not designed to be safe really, but we at least want to check if where we are putting the player is air
                
                Vec3 pos = pla.position();
                
                boolean found_place= false;
        
                int tries=0;
                ChatServerOverride.broadcastTo(pla.getUUID(), Component.literal(ChatColor.DARK_GRAY + "["+ChatColor.DARK_GREEN+"OTEMOD"+ChatColor.DARK_GRAY+"] "+ChatColor.GREEN+"Searching for a suitable landing location..."), source.getServer());
                /*
                 * 
                 *
                while(!found_place){  
        
                    // Take our current position, and send us in a random direction
                    Random rng = new Random((long) (pos.x+pos.y+pos.z));
                    v.y = 500;
                    v.x = rng.nextDouble(0xffff);
                    v.z = rng.nextDouble(0xffff);

                    boolean is_invalid_location = false;
                    String block_place="";

                    // Begin to scan for ground
                    while(v.y != -30)
                    {
                        // check block above and below
                        BlockState b = source.getLevel().getBlockState(new BlockPos(v.asMinecraftVector()));
                        BlockState b2 = source.getLevel().getBlockState(new BlockPos(v.moveUp().asMinecraftVector()));
                        BlockState b3 = source.getLevel().getBlockState(new BlockPos(v.moveDown().asMinecraftVector()));
                        //Block bx = b.getBlock();

                        // Check that none of the blocks are water or lava

                        
                        if(b.isAir()){
                            if(b2.isAir()){
                                if(!b3.isAir()){

                                    // Check names
                                    boolean valid=true;
                                    List<String> blackList = new ArrayList<>();
                                    if(!allowWater)
                                        blackList.add("Water");
                                    blackList.add("Lava");

                                    block_place = b3.getBlock().getName().getString();
                                    OTEMod.LOGGER.info(b3.getBlock().getName().getString());
                                    if(blackList.contains(b.getBlock().getName().getString())){
                                        valid=false;
                                        is_invalid_location=true;
                                    }
                                    if(blackList.contains(b2.getBlock().getName().getString())){
                                        valid=false;
                                        is_invalid_location=true;
                                    }
                                    if(blackList.contains(b3.getBlock().getName().getString())){
                                        valid=false;
                                        is_invalid_location=true;
                                    }
                                    
                                    if(valid){

                                        found_place = true;
                                        break;
                                    }
                                }
                            }
                        }
        
                        v =v.moveDown();
        
                    }
                    if(tries>=30)
                    {
                        // Aborting RTP
                        ChatServerOverride.broadcastTo(pla.getUUID(), Component.literal(ChatColor.DARK_RED+"Could not find a suitable location after 30 tries. Giving up on RTP"), source.getServer());
                        return;
                    }
                    tries++;
                    
                    String sAppend = "";
                    if(is_invalid_location){
                        sAppend = block_place + " is not valid";
                    }
                    ChatServerOverride.broadcastToAbove(pla.getUUID(), Component.literal(ChatColor.DARK_PURPLE+"Still searching.... Try ["+String.valueOf(tries)+"/30] "+sAppend), source.getServer());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/
                v=findPosition(source.getLevel(), allowWater);
        
                ChatServerOverride.broadcastTo(pla.getUUID(), Component.literal(ChatColor.DARK_GRAY + "["+ChatColor.DARK_GREEN + "OTEMOD" + ChatColor.DARK_GRAY + "] "+ChatColor.DARK_PURPLE+" A suitable location has been found. Wormhole opening now!"), source.getServer());
        
                // Apply the effect
                TeleportActioner.ApplyTeleportEffect(pla);
                cont.Position=v.asMinecraftVector();
        
                TeleportActioner.PerformTeleport(cont);
        
            }
        });

        tx.start();


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
