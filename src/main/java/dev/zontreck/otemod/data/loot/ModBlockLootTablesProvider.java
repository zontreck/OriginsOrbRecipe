package dev.zontreck.otemod.data.loot;

import dev.zontreck.otemod.blocks.*;
import dev.zontreck.otemod.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;

import static dev.zontreck.otemod.blocks.ModBlocks.BLOCKS;

public class ModBlockLootTablesProvider extends BlockLootSubProvider
{
    public ModBlockLootTablesProvider()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        add(ModBlocks.ETERNIUM_ORE_BLOCK.get(), (block)-> createOreDrop(ModBlocks.ETERNIUM_ORE_BLOCK.get(), ModItems.ETERNIUM_RAW_ORE.get()));
        add(ModBlocks.VAULT_STEEL_ORE_BLOCK.get(), (block)-> createOreDrop(ModBlocks.VAULT_STEEL_ORE_BLOCK.get(), ModItems.VAULT_RAW_ORE.get()));
        add(ModBlocks.NETHER_VAULT_STEEL_ORE_BLOCK.get(), (block)-> createOreDrop(ModBlocks.NETHER_VAULT_STEEL_ORE_BLOCK.get(), ModItems.VAULT_RAW_ORE.get()));
        add(ModBlocks.DEEPSLATE_ETERNIUM_ORE_BLOCK.get(), (block)-> createOreDrop(ModBlocks.DEEPSLATE_ETERNIUM_ORE_BLOCK.get(), ModItems.ETERNIUM_RAW_ORE.get()));


        dropSelf(ModBlocks.ETERNIUM_BLOCK.get());
        dropSelf(ModBlocks.ITEM_SCRUBBER.get());
        dropSelf(ModBlocks.MAGICAL_SCRUBBER.get());
        dropSelf(ModBlocks.STABLE_SINGULARITY.get());
        dropSelf(ModBlocks.COMPRESSION_CHAMBER.get());
        dropSelf(ModBlocks.COMPRESSED_OBSIDIAN_BLOCK.get());
        dropSelf(ModBlocks.LAYERED_COMPRESSED_OBSIDIAN_BLOCK.get());
        dropSelf(ModBlocks.VOID.get());
        dropSelf(ModBlocks.WHITEOUT.get());
        dropSelf(ModBlocks.BLOOD_RED.get());
        dropSelf(ModBlocks.RED_TILE.get());
        dropSelf(ModBlocks.RED_STAIRS.get());
        dropSelf(ModBlocks.RED_TILE_BR.get());
        dropSelf(ModBlocks.RED_TILE_TO_WALL.get());
        dropSelf(ModBlocks.RED_WALL_V1.get());
        dropSelf(ModBlocks.RED_WALL_V2.get());
        dropSelf(ModBlocks.CYAN.get());
        dropSelf(ModBlocks.CYAN_TILE.get());
        dropSelf(ModBlocks.CYAN_STAIRS.get());
        dropSelf(ModBlocks.CYAN_TILE_BR.get());
        dropSelf(ModBlocks.CYAN_TILE_TO_WALL.get());
        dropSelf(ModBlocks.CYAN_WALL_V1.get());
        dropSelf(ModBlocks.CYAN_WALL_V2.get());
        dropSelf(ModBlocks.POOL_TILE.get());
        dropSelf(ModBlocks.POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.DIRTY_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.DIRTY_POOL_LIGHT.get());
        dropSelf(ModBlocks.FILTHY_POOL_LIGHT.get());
        dropSelf(ModBlocks.DARK_POOL_TILE.get());
        dropSelf(ModBlocks.DARK_POOL_LIGHT.get());
        dropSelf(ModBlocks.DARK_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.DARK_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.BLUE_POOL_TILE.get());
        dropSelf(ModBlocks.BLUE_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.BLUE_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.BLUE_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_LIGHT.get());
        dropSelf(ModBlocks.FILTHY_BLUE_POOL_LIGHT.get());
        dropSelf(ModBlocks.RED_POOL_TILE.get());
        dropSelf(ModBlocks.RED_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.RED_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.RED_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_LIGHT.get());
        dropSelf(ModBlocks.FILTHY_RED_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.DARK_RED_WOOL.get());
        dropSelf(ModBlocks.DARK_RED_CARPET.get());
        dropSelf(ModBlocks.GREEN_POOL_TILE.get());
        dropSelf(ModBlocks.GREEN_POOL_LIGHT.get());
        dropSelf(ModBlocks.GREEN_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.GREEN_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_TILE_SLAB.get());
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.FILTHY_GREEN_POOL_LIGHT.get());



    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


    protected LootTable.Builder createCopperOreDrops(Block block, Item rawOre) {
        return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder) this.applyExplosionDecay(block, LootItem.lootTableItem(rawOre).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createOreDrop(Block block, Item rawOre) {
        return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)this.applyExplosionDecay(block, LootItem.lootTableItem(rawOre).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
}
