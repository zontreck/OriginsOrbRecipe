package dev.zontreck.otemod.data.loot;

import dev.zontreck.otemod.blocks.DeprecatedModBlocks;
import dev.zontreck.otemod.blocks.ModBlocks;
import dev.zontreck.otemod.items.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModDeprecatedBlockLootTablesProvider extends BlockLootSubProvider
{
    public ModDeprecatedBlockLootTablesProvider()
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        /*
        DEPRECATED BLOCKS
         */

        dropSelf(DeprecatedModBlocks.ILUSIUM_ORE_BLOCK.get());
        dropSelf(DeprecatedModBlocks.DEEPSLATE_ILUSIUM_ORE_BLOCK.get());
        dropSelf(DeprecatedModBlocks.ILUSIUM_BLOCK.get());
        dropSelf(DeprecatedModBlocks.CLEAR_GLASS_BLOCK.get());
        dropSelf(DeprecatedModBlocks.LIMINAL_TILES.get());
        dropSelf(DeprecatedModBlocks.LIMINAL_TILE_STAIRS.get());
        dropSelf(DeprecatedModBlocks.LIMINAL_TILE_SLAB.get());
        dropSelf(DeprecatedModBlocks.LIMINAL_WINDOW.get());
        dropSelf(DeprecatedModBlocks.LIME.get());
        dropSelf(DeprecatedModBlocks.LIME_TILE.get());
        dropSelf(DeprecatedModBlocks.LIME_STAIRS.get());
        dropSelf(DeprecatedModBlocks.LIME_TILE_BR.get());
        dropSelf(DeprecatedModBlocks.LIME_TILE_TO_WALL.get());
        dropSelf(DeprecatedModBlocks.LIME_WALL_V1.get());
        dropSelf(DeprecatedModBlocks.LIME_WALL_V2.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DeprecatedModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


    protected LootTable.Builder createCopperOreDrops(Block block, Item rawOre) {
        return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder) this.applyExplosionDecay(block, LootItem.lootTableItem(rawOre).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createOreDrop(Block block, Item rawOre) {
        return createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)this.applyExplosionDecay(block, LootItem.lootTableItem(rawOre).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
}
