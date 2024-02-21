package dev.zontreck.otemod.implementation;

import com.google.common.collect.ImmutableSet;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.ModBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModDyes
{
    public static List<DyeColor> DYES = new ArrayList<>();

    public static DyeColor DARK_RED;

    public static void InitColors() {
        DARK_RED = DyeColor.byName("dark_red", DyeColor.WHITE);
        if(DARK_RED == DyeColor.WHITE) DARK_RED = DyeColor.byName("DARK_RED", DyeColor.BLACK);
        if(DARK_RED == DyeColor.BLACK) {
            OTEMod.LOGGER.error("Dye colors cannot be obtained properly: " + DARK_RED + " -   " + DyeColor.byName("dark_red", DyeColor.WHITE));
        }
        DYES.add(DARK_RED);
    }

    public static void UpdateBlockEntities()
    {
        Set<Block> shulkerSet = BlockEntityType.SHULKER_BOX.validBlocks;
        List<Block> shulkerList = new ArrayList<>();
        for(Block shulker : shulkerSet)
        {
            shulkerList.add(shulker);
        }
        shulkerList.add(ModBlocks.DARK_RED_SHULKER_BOX.get());
        BlockEntityType.SHULKER_BOX.validBlocks = ImmutableSet.copyOf(shulkerList);


        Set<Block> bedSet = BlockEntityType.BED.validBlocks;
        List<Block> bedList = new ArrayList<>();
        for (Block bed : bedSet) {
            bedList.add(bed);
        }
        bedList.add(ModBlocks.DARK_RED_BED.get());

        BlockEntityType.BED.validBlocks = ImmutableSet.copyOf(bedList);
    }

}
