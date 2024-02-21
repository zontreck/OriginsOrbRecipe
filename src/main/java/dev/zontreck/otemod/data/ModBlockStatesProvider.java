package dev.zontreck.otemod.data;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.*;
import dev.zontreck.otemod.items.DeprecatedModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStatesProvider extends BlockStateProvider
{
    public ModBlockStatesProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, OTEMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.ETERNIUM_ORE_BLOCK);
        blockWithItem(ModBlocks.VAULT_STEEL_ORE_BLOCK);
        blockWithItem(ModBlocks.NETHER_VAULT_STEEL_ORE_BLOCK);
        blockWithItem(ModBlocks.ETERNIUM_BLOCK);
        blockWithItem(ModBlocks.DEEPSLATE_ETERNIUM_ORE_BLOCK);
        blockWithItem(ModBlocks.COMPRESSED_OBSIDIAN_BLOCK);
        blockWithItem(ModBlocks.LAYERED_COMPRESSED_OBSIDIAN_BLOCK);
        blockWithItem(ModBlocks.POOL_TILE);
        stairBlock(ModBlocks.POOL_TILE_STAIRS, ModBlocks.POOL_TILE);
        slabBlock(ModBlocks.POOL_TILE_SLAB, ModBlocks.POOL_TILE);
        blockWithItem(ModBlocks.POOL_LIGHT);
        blockWithItem(ModBlocks.DIRTY_POOL_TILE);
        stairBlock(ModBlocks.DIRTY_POOL_TILE_STAIRS, ModBlocks.DIRTY_POOL_TILE);
        slabBlock(ModBlocks.DIRTY_POOL_TILE_SLAB, ModBlocks.DIRTY_POOL_TILE);
        blockWithItem(ModBlocks.DIRTY_POOL_LIGHT);
        blockWithItem(ModBlocks.FILTHY_POOL_LIGHT);
        blockWithItem(ModBlocks.DARK_POOL_TILE);
        blockWithItem(ModBlocks.DARK_POOL_LIGHT);
        stairBlock(ModBlocks.DARK_POOL_TILE_STAIRS, ModBlocks.DARK_POOL_TILE);
        slabBlock(ModBlocks.DARK_POOL_TILE_SLAB, ModBlocks.DARK_POOL_TILE);
        blockWithItem(ModBlocks.BLUE_POOL_TILE);
        stairBlock(ModBlocks.BLUE_POOL_TILE_STAIRS, ModBlocks.BLUE_POOL_TILE);
        slabBlock(ModBlocks.BLUE_POOL_TILE_SLAB, ModBlocks.BLUE_POOL_TILE);
        blockWithItem(ModBlocks.BLUE_POOL_LIGHT);
        blockWithItem(ModBlocks.DIRTY_BLUE_POOL_TILE);
        stairBlock(ModBlocks.DIRTY_BLUE_POOL_TILE_STAIRS, ModBlocks.DIRTY_BLUE_POOL_TILE);
        slabBlock(ModBlocks.DIRTY_BLUE_POOL_TILE_SLAB, ModBlocks.DIRTY_BLUE_POOL_TILE);
        blockWithItem(ModBlocks.DIRTY_BLUE_POOL_LIGHT);
        blockWithItem(ModBlocks.FILTHY_BLUE_POOL_LIGHT);
        blockWithItem(ModBlocks.RED_POOL_TILE);
        stairBlock(ModBlocks.RED_POOL_TILE_STAIRS, ModBlocks.RED_POOL_TILE);
        slabBlock(ModBlocks.RED_POOL_TILE_SLAB, ModBlocks.RED_POOL_TILE);
        blockWithItem(ModBlocks.RED_POOL_LIGHT);
        blockWithItem(ModBlocks.DIRTY_RED_POOL_LIGHT);
        blockWithItem(ModBlocks.FILTHY_RED_POOL_LIGHT);
        blockWithItem(ModBlocks.DIRTY_RED_POOL_TILE);
        stairBlock(ModBlocks.DIRTY_RED_POOL_TILE_STAIRS, ModBlocks.DIRTY_RED_POOL_TILE);
        slabBlock(ModBlocks.DIRTY_RED_POOL_TILE_SLAB, ModBlocks.DIRTY_RED_POOL_TILE);
        blockWithItem(ModBlocks.DARK_RED_WOOL);
        carpetBlock(ModBlocks.DARK_RED_CARPET, ModBlocks.DARK_RED_WOOL);


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject)
    {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject, ModelFile model)
    {
        simpleBlockWithItem(blockRegistryObject.get(), model);
    }

    private void stairBlock(RegistryObject<Block> blk, RegistryObject<Block> texture)
    {
        stairsBlock((StairBlock) blk.get(), blockTexture(texture.get()));
        simpleBlockItem(blk.get(), stairsModel(blk.get(), texture.get()));
    }
    private void carpetBlock(RegistryObject<Block> blk, RegistryObject<Block> texture)
    {
        simpleBlockWithItem(blk.get(), carpetModel(blk.get(), texture.get()));
    }

    private String name(Block block) {
        return this.key(block).getPath();
    }
    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
    public ModelFile stairsModel(Block block, Block texture) {
        return this.models().stairs(name(block), blockTexture(texture), blockTexture(texture), blockTexture(texture));
    }
    public ModelFile carpetModel(Block block, Block texture) {
        return this.models().carpet(name(block), blockTexture(texture));
    }
    public ModelFile slabModel(Block block, Block texture) {
        return this.models().slab(name(block), blockTexture(texture), blockTexture(texture), blockTexture(texture));
    }
    private void slabBlock(RegistryObject<Block> blk, RegistryObject<Block> texture)
    {
        slabBlock((SlabBlock) blk.get(), blockTexture(texture.get()), blockTexture(texture.get()));
        simpleBlockItem(blk.get(), slabModel(blk.get(), texture.get()));
    }
}
