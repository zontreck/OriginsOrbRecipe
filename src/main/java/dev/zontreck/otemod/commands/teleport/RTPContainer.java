package dev.zontreck.otemod.commands.teleport;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class RTPContainer {
    public TeleportContainer container;
    public int tries = -1;
    public boolean complete = false;
    public boolean aborted=false;
    public Thread containingThread;
    private Types heightMapType;
    private int SearchDirection;

    public static final List<Block> BLACKLIST;

    static {
        List<Block> tmp = new ArrayList<>();
        tmp.add(Blocks.LAVA);
        tmp.add(Blocks.BEDROCK);
        tmp.add(Blocks.WATER);

        BLACKLIST = tmp;
    }

    public void moveDown() {
        container.world_pos.Position = container.world_pos.Position.moveDown();
        container.Position = container.world_pos.Position.asMinecraftVector();
    }

    public void moveUp() {
        container.world_pos.Position = container.world_pos.Position.moveUp();
        container.Position = container.world_pos.Position.asMinecraftVector();
    }

    public void move()
    {
        if(SearchDirection==1){
            moveUp();
        }else if(SearchDirection==0)
        {
            moveDown();
        }
    }
    public void moveOpposite()
    {
        if(SearchDirection==1){
            moveDown();
        }else if(SearchDirection==0)
        {
            moveUp();
        }
    }

    public void newPosition() {
        if(!OTEMod.ALIVE)return;
        containingThread=Thread.currentThread();
        Random rng = new Random(Instant.now().getEpochSecond());
        Vector3 pos = new Vector3(rng.nextDouble(0xFFFF), 0, rng.nextDouble(0xFFFF));
        BlockPos bpos = pos.asBlockPos();
        container.Dimension.getChunk(bpos.getX() >> 4, bpos.getZ() >> 4, ChunkStatus.HEIGHTMAPS);
        pos = new Vector3(
                container.Dimension.getHeightmapPos(heightMapType, pos.asBlockPos()));
        while (!container.Dimension.getWorldBorder().isWithinBounds(pos.asBlockPos())) {
            pos = new Vector3(rng.nextDouble(0xffff), 0, rng.nextDouble(0xffff));
            bpos = pos.asBlockPos();
            container.Dimension.getChunk(bpos.getX() >> 4, bpos.getZ() >> 4, ChunkStatus.HEIGHTMAPS);
            pos = new Vector3(
                    container.Dimension.getHeightmapPos(heightMapType, pos.asBlockPos()));
        }

        container.world_pos.Position = pos;
        container.Position = container.world_pos.Position.asMinecraftVector();

        if (pos.y < -60) {
            newPosition();
            return;
        }

        if (pos.y >= container.Dimension.getLogicalHeight()) {
            spiralPositions(pos);
        }

        tries++;
    }

    private void spiralPositions(Vector3 position)
    {
        for(BlockPos pos : BlockPos.spiralAround(new BlockPos(position.x, container.Dimension.getSeaLevel(), position.z), 16, Direction.WEST, Direction.NORTH)){
            if(isSafe(pos)){
                // Set the new position
                container.world_pos.Position = new Vector3(pos);
                container.Position = container.world_pos.Position.asMinecraftVector();
                return;
            }
        }
    }

    protected RTPContainer(ServerPlayer player, Vec3 pos, Vec2 rot, ServerLevel level) {
        container = new TeleportContainer(player, pos, rot, level);
        if(container.Dimension.dimensionType().hasCeiling())
        {
            heightMapType = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
            SearchDirection=-1;
        }else {
            heightMapType = Types.MOTION_BLOCKING_NO_LEAVES;
            SearchDirection=1;
        }
        newPosition();
    }

    public boolean isSafe(BlockPos blockPos) {
        containingThread=Thread.currentThread();
        BlockState b = container.Dimension.getBlockState(blockPos);
        BlockState b2 = container.Dimension.getBlockState(blockPos.above());
        BlockState b3 = container.Dimension.getBlockState(blockPos.below());

        if (b.isAir() && b2.isAir()) {
            if (!b3.isAir()) {
                if (BLACKLIST.contains(b3.getBlock())) {
                    return false;
                } else
                    return true;
            } else
                return false;
        } else
            return false;

    }
}
