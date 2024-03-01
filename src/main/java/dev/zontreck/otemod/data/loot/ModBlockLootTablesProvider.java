package dev.zontreck.otemod.data.loot;

import dev.zontreck.otemod.blocks.*;
import dev.zontreck.otemod.items.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

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
        createSlabItemTable(ModBlocks.POOL_TILE_SLAB);
        dropSelf(ModBlocks.POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_POOL_TILE_STAIRS.get());
        createSlabItemTable(ModBlocks.DIRTY_POOL_TILE_SLAB);
        dropSelf(ModBlocks.DIRTY_POOL_LIGHT.get());
        dropSelf(ModBlocks.FILTHY_POOL_LIGHT.get());
        dropSelf(ModBlocks.DARK_POOL_TILE.get());
        dropSelf(ModBlocks.DARK_POOL_LIGHT.get());
        dropSelf(ModBlocks.DARK_POOL_TILE_STAIRS.get());
        createSlabItemTable(ModBlocks.DARK_POOL_TILE_SLAB);
        dropSelf(ModBlocks.BLUE_POOL_TILE.get());
        dropSelf(ModBlocks.BLUE_POOL_TILE_STAIRS.get());
        createSlabItemTable(ModBlocks.BLUE_POOL_TILE_SLAB);
        dropSelf(ModBlocks.BLUE_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_TILE_STAIRS.get());
        createSlabItemTable(ModBlocks.DIRTY_BLUE_POOL_TILE_SLAB);
        dropSelf(ModBlocks.DIRTY_BLUE_POOL_LIGHT.get());
        dropSelf(ModBlocks.FILTHY_BLUE_POOL_LIGHT.get());
        dropSelf(ModBlocks.RED_POOL_TILE.get());
        dropSelf(ModBlocks.RED_POOL_TILE_STAIRS.get());
        createSlabItemTable(ModBlocks.RED_POOL_TILE_SLAB);
        dropSelf(ModBlocks.RED_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_LIGHT.get());
        dropSelf(ModBlocks.FILTHY_RED_POOL_LIGHT.get());
        dropSelf(ModBlocks.DIRTY_RED_POOL_TILE_STAIRS.get());
        createSlabItemTable(ModBlocks.DIRTY_RED_POOL_TILE_SLAB);
        dropSelf(ModBlocks.DARK_RED_WOOL.get());
        dropSelf(ModBlocks.DARK_RED_CARPET.get());
        dropSelf(ModBlocks.GREEN_POOL_TILE.get());
        dropSelf(ModBlocks.GREEN_POOL_LIGHT.get());
        createSlabItemTable(ModBlocks.GREEN_POOL_TILE_SLAB);
        dropSelf(ModBlocks.GREEN_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_TILE.get());
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_LIGHT.get());
        createSlabItemTable(ModBlocks.DIRTY_GREEN_POOL_TILE_SLAB);
        dropSelf(ModBlocks.DIRTY_GREEN_POOL_TILE_STAIRS.get());
        dropSelf(ModBlocks.FILTHY_GREEN_POOL_LIGHT.get());
        dropSelf(ModBlocks.UNCRAFTER.get());

        /*
        Engineer's Decor Blocks
         */
        dropSelf(ModBlocks.CLINKER_BRICK_BLOCK.get());
        dropSelf(ModBlocks.CLINKER_BRICK_RECESSED.get());
        dropSelf(ModBlocks.CLINKER_BRICK_VERTICALLY_SLIT.get());
        createSlabItemTable(ModBlocks.CLINKER_BRICK_SLAB);
        dropSelf(ModBlocks.CLINKER_BRICK_STAIRS.get());
        dropSelf(ModBlocks.CLINKER_BRICK_STAINED_BLOCK.get());
        createSlabItemTable(ModBlocks.CLINKER_BRICK_STAINED_SLAB);
        dropSelf(ModBlocks.CLINKER_BRICK_STAINED_STAIRS.get());
        dropSelf(ModBlocks.CLINKER_BRICK_SASTOR_CORNER_BLOCK.get());
        dropSelf(ModBlocks.CLINKER_BRICK_WALL.get());

        dropSelf(ModBlocks.SLAG_BRICK_BLOCK.get());
        createSlabItemTable(ModBlocks.SLAG_BRICK_SLAB);
        dropSelf(ModBlocks.SLAG_BRICK_WALL.get());
        dropSelf(ModBlocks.SLAG_BRICK_STAIRS.get());

        dropSelf(ModBlocks.REBAR_CONCRETE_BLOCK.get());
        createSlabItemTable(ModBlocks.REBAR_CONCRETE_SLAB);
        dropSelf(ModBlocks.REBAR_CONCRETE_STAIRS.get());
        dropSelf(ModBlocks.REBAR_CONCRETE_WALL.get());

        dropSelf(ModBlocks.REBAR_CONCRETE_TILE_BLOCK.get());
        createSlabItemTable(ModBlocks.REBAR_CONCRETE_TILE_SLAB);
        dropSelf(ModBlocks.REBAR_CONCRETE_TILE_STAIRS.get());

        dropSelf(ModBlocks.PANZER_GLASS_BLOCK.get());
        createSlabItemTable(ModBlocks.PANZER_GLASS_SLAB);

        dropSelf(ModBlocks.OLD_INDUSTRIAL_WOOD_PLANKS.get());
        createSlabItemTable(ModBlocks.OLD_INDUSTRIAL_WOOD_SLAB);
        dropSelf(ModBlocks.OLD_INDUSTRIAL_WOOD_STAIRS.get());
        createDoorTable(ModBlocks.OLD_INDUSTRIAL_WOOD_DOOR);

        dropSelf(ModBlocks.STEEL_TABLE.get());
        dropSelf(ModBlocks.STEEL_CATWALK.get());
        dropSelf(ModBlocks.STEEL_RAILING.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS_LR.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS_RR.get());
        dropSelf(ModBlocks.STEEL_CATWALK_STAIRS_DR.get());
        dropSelf(ModBlocks.STEEL_GRATING.get());

        dropSelf(ModBlocks.STEEL_GRATING_TOP.get());
        dropSelf(ModBlocks.STEEL_CATWALK_TOP.get());



    }

    private void createDoorTable(RegistryObject<Block> blk)
    {
        var loot = createDoorTable(blk.get());

        add(blk.get(), loot);
    }

    private void createSlabItemTable(RegistryObject<Block> slab)
    {
        var loot = createSlabItemTable(slab.get());
        add(slab.get(), loot);
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
