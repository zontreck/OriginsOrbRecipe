package dev.zontreck.otemod.data;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.atomic.AtomicReference;

public class ModBlockStatesProvider extends BlockStateProvider {
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

        blockWithItem(ModBlocks.GREEN_POOL_TILE);
        stairBlock(ModBlocks.GREEN_POOL_TILE_STAIRS, ModBlocks.GREEN_POOL_TILE);
        slabBlock(ModBlocks.GREEN_POOL_TILE_SLAB, ModBlocks.GREEN_POOL_TILE);
        blockWithItem(ModBlocks.GREEN_POOL_LIGHT);
        blockWithItem(ModBlocks.DIRTY_GREEN_POOL_LIGHT);
        blockWithItem(ModBlocks.FILTHY_GREEN_POOL_LIGHT);
        blockWithItem(ModBlocks.DIRTY_GREEN_POOL_TILE);
        stairBlock(ModBlocks.DIRTY_GREEN_POOL_TILE_STAIRS, ModBlocks.DIRTY_GREEN_POOL_TILE);
        slabBlock(ModBlocks.DIRTY_GREEN_POOL_TILE_SLAB, ModBlocks.DIRTY_GREEN_POOL_TILE);

        ResourceLocation[] clinkerBlock = new ResourceLocation[]{
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture3"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture4"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture5"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture6"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_texture7")
        };
        ResourceLocation[] clinkerStainedBlock = new ResourceLocation[]{
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture3"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture4"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture5"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture6"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/clinker_brick/clinker_brick_stained_texture7")
        };

        ResourceLocation[] slagBricks = new ResourceLocation[]{
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture3"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture4"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture5"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture6"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/slag_brick/slag_brick_texture7")
        };

        ResourceLocation[] rebarConcrete = new ResourceLocation[] {
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture3"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture4"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture5"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture6"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_texture7")
        };

        ResourceLocation[] rebarConcreteTile = new ResourceLocation[] {
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture3"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture4"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture5"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture6"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/concrete/rebar_concrete_tile_texture7")
        };

        ResourceLocation[] panzerglass = new ResourceLocation[]{
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/glass/panzerglass_block_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/glass/panzerglass_block_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/glass/panzerglass_block_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/glass/panzerglass_block_texture3")
        };

        ResourceLocation[] oldIndustrialWood = new ResourceLocation[]{
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/material/industrial_planks_texture0"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/material/industrial_planks_texture1"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/material/industrial_planks_texture2"),
                new ResourceLocation(OTEMod.MOD_ID, "engineersdecor/material/industrial_planks_texture3"),
        };

    }

    private void blockWithExistingModel(RegistryObject<Block> blk, String model, boolean rotatable)
    {
        ResourceLocation modelLoc = new ResourceLocation(OTEMod.MOD_ID, model);
        ModelFile mFile = models().withExistingParent(name(blk.get()), modelLoc);

        if(!rotatable)
            simpleBlock(blk.get(), mFile);
        else horizontalBlock(blk.get(), mFile);

        simpleBlockItem(blk.get(), mFile);
    }

    private void doorBlock(RegistryObject<Block> blk, ResourceLocation textureTop, ResourceLocation textureBottom)
    {
        doorBlockWithRenderType((DoorBlock) blk.get(), textureBottom, textureTop, new ResourceLocation("translucent"));

        simpleBlockItem(blk.get(), models().doorBottomLeft(name(blk.get()), textureBottom, textureTop));
    }

    private void wallBlock(RegistryObject<Block> blk, ResourceLocation texture)
    {
        wallBlock((WallBlock) blk.get(), texture);
        var wallInv = models().wallInventory(name(blk.get()) + "_inventory", texture);

        simpleBlockItem(blk.get(), wallInv);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject, ModelFile model) {
        simpleBlockWithItem(blockRegistryObject.get(), model);
    }

    private void stairBlock(RegistryObject<Block> blk, RegistryObject<Block> texture) {
        stairsBlock((StairBlock) blk.get(), blockTexture(texture.get()));
        simpleBlockItem(blk.get(), stairsModel(blk.get(), texture.get()));
    }

    private void carpetBlock(RegistryObject<Block> blk, RegistryObject<Block> texture) {
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

    private void slabBlock(RegistryObject<Block> blk, RegistryObject<Block> texture) {
        slabBlock((SlabBlock) blk.get(), blockTexture(texture.get()), blockTexture(texture.get()));
        simpleBlockItem(blk.get(), slabModel(blk.get(), texture.get()));
    }

    private void customSlabBlock(RegistryObject<Block> blockId, ResourceLocation... variations) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockId.get());


        AtomicReference<ModelFile> model0 = new AtomicReference<>();

        builder.forAllStates((state)->{
            ConfiguredModel[] models = new ConfiguredModel[variations.length];


            String appendName = "";
            SlabType type = state.getValue(SlabBlock.TYPE);

            if(type == SlabType.BOTTOM)
                appendName = "_bottom";
            else if(type == SlabType.TOP)
                appendName = "_top";
            else if(type == SlabType.DOUBLE)
                appendName = "_double";

            for (int i = 0; i < variations.length; i++) {
                ResourceLocation texture = variations[i];
                ResourceLocation rss = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());
                ModelFile model = null;
                if(type == SlabType.TOP)
                    model = models().slabTop(name(blockId.get()) + "_model" + i + appendName, rss, rss, rss);
                else if(type == SlabType.BOTTOM)
                    model = models().slab(name(blockId.get()) + "_model" + i + appendName, rss, rss, rss);
                else if(type == SlabType.DOUBLE)
                    model = models().cubeAll(name(blockId.get()) + "_model" + i + appendName, rss);


                ConfiguredModel[] cfgModel = ConfiguredModel.builder().modelFile(model).build();

                if(i==0 && model0.get()==null && type == SlabType.BOTTOM) model0.set(model);

                models[i] = cfgModel[0];
                //builder.partialState().addModels(cfgModel);
            }
            return models;
        });


        simpleBlockItem(blockId.get(), model0.get());
    }

    private void customStairBlock(RegistryObject<Block> blockId, ResourceLocation... variations) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockId.get());
        ResourceLocation blockDefault = blockTexture(blockId.get());


        AtomicReference<ModelFile> model0 = new AtomicReference<>();

        builder.forAllStates((state)->{
            ConfiguredModel[] models = new ConfiguredModel[variations.length];
            Direction facing = (Direction)state.getValue(StairBlock.FACING);
            Half half = (Half)state.getValue(StairBlock.HALF);
            StairsShape shape = (StairsShape)state.getValue(StairBlock.SHAPE);
            int yRot = (int)facing.getClockWise().toYRot();
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270;
            }

            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90;
            }

            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP;

            String modelName = (shape == StairsShape.STRAIGHT) ? "" : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT) ? "_outer":"_inner";
            boolean straight = (shape == StairsShape.STRAIGHT);
            boolean inner = (shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT);


            for (int i = 0; i < variations.length; i++) {
                ResourceLocation texture = variations[i];
                ResourceLocation rss = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());
                ModelFile cubeModel = null;
                if(straight)
                    cubeModel = models().stairs(
                            blockId.getId().getPath() + "_model"+i + modelName, // Model name
                            rss, rss, rss // Texture location
                    );

                if(inner)
                    cubeModel = models().stairsInner(blockId.getId().getPath()+"_model"+i + modelName, rss, rss, rss);
                else if(!inner && !straight)
                    cubeModel = models().stairsOuter(blockId.getId().getPath() + "_model"+i+modelName, rss, rss, rss);

                ConfiguredModel[] cfgModel = ConfiguredModel.builder().modelFile(cubeModel).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();

                if(i==0 && model0.get()==null) model0.set(cubeModel);

                models[i] = cfgModel[0];
                //builder.partialState().addModels(cfgModel);
            }

            return models;
        });


        simpleBlockItem(blockId.get(), model0.get());
    }
    private void customTransparentSlabBlock(RegistryObject<Block> blockId, ResourceLocation... variations) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockId.get());


        AtomicReference<ModelFile> model0 = new AtomicReference<>();

        builder.forAllStates((state)->{
            ConfiguredModel[] models = new ConfiguredModel[variations.length];


            String appendName = "";
            SlabType type = state.getValue(SlabBlock.TYPE);

            if(type == SlabType.BOTTOM)
                appendName = "_bottom";
            else if(type == SlabType.TOP)
                appendName = "_top";
            else if(type == SlabType.DOUBLE)
                appendName = "_double";

            for (int i = 0; i < variations.length; i++) {
                ResourceLocation texture = variations[i];
                ResourceLocation rss = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());
                ModelFile model = null;
                if(type == SlabType.TOP)
                    model = models().slabTop(name(blockId.get()) + "_model" + i + appendName, rss, rss, rss).renderType(new ResourceLocation("translucent"));
                else if(type == SlabType.BOTTOM)
                    model = models().slab(name(blockId.get()) + "_model" + i + appendName, rss, rss, rss).renderType(new ResourceLocation("translucent"));
                else if(type == SlabType.DOUBLE)
                    model = models().cubeAll(name(blockId.get()) + "_model" + i + appendName, rss).renderType(new ResourceLocation("translucent"));


                ConfiguredModel[] cfgModel = ConfiguredModel.builder().modelFile(model).build();

                if(i==0 && model0.get()==null && type == SlabType.BOTTOM) model0.set(model);

                models[i] = cfgModel[0];
                //builder.partialState().addModels(cfgModel);
            }
            return models;
        });


        simpleBlockItem(blockId.get(), model0.get());
    }

    private void customTransparentStairBlock(RegistryObject<Block> blockId, ResourceLocation... variations) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockId.get());
        ResourceLocation blockDefault = blockTexture(blockId.get());


        AtomicReference<ModelFile> model0 = new AtomicReference<>();

        builder.forAllStates((state)->{
            ConfiguredModel[] models = new ConfiguredModel[variations.length];
            Direction facing = (Direction)state.getValue(StairBlock.FACING);
            Half half = (Half)state.getValue(StairBlock.HALF);
            StairsShape shape = (StairsShape)state.getValue(StairBlock.SHAPE);
            int yRot = (int)facing.getClockWise().toYRot();
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270;
            }

            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90;
            }

            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP;

            String modelName = (shape == StairsShape.STRAIGHT) ? "" : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT) ? "_outer":"_inner";
            boolean straight = (shape == StairsShape.STRAIGHT);
            boolean inner = (shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT);


            for (int i = 0; i < variations.length; i++) {
                ResourceLocation texture = variations[i];
                ResourceLocation rss = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());
                ModelFile cubeModel = null;
                if(straight)
                    cubeModel = models().stairs(
                            blockId.getId().getPath() + "_model"+i + modelName, // Model name
                            rss, rss, rss // Texture location
                    ).renderType(new ResourceLocation("translucent"));

                if(inner)
                    cubeModel = models().stairsInner(blockId.getId().getPath()+"_model"+i + modelName, rss, rss, rss).renderType(new ResourceLocation("translucent"));
                else if(!inner && !straight)
                    cubeModel = models().stairsOuter(blockId.getId().getPath() + "_model"+i+modelName, rss, rss, rss).renderType(new ResourceLocation("translucent"));

                ConfiguredModel[] cfgModel = ConfiguredModel.builder().modelFile(cubeModel).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();

                if(i==0 && model0.get()==null) model0.set(cubeModel);

                models[i] = cfgModel[0];
                //builder.partialState().addModels(cfgModel);
            }

            return models;
        });


        simpleBlockItem(blockId.get(), model0.get());
    }

    public void variantCubeBlock(RegistryObject<Block> blockId, ResourceLocation... variations) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockId.get());
        ResourceLocation blockDefault = blockTexture(blockId.get());

        ModelFile model0 = null;
        for (int i = 0; i < variations.length; i++) {
            ResourceLocation texture = variations[i];
            ResourceLocation rss = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());
            ModelFile cubeModel = models().cubeAll(
                    blockId.getId().getPath() + "_model"+i, // Model name
                    rss // Texture location
            );
            var cfgModel = ConfiguredModel.builder().modelFile(cubeModel).build();
            if(i==0)model0 = cubeModel;
            builder.partialState().addModels(cfgModel);
        }



        simpleBlockItem(blockId.get(), model0);
    }

    public void variantTransparentCubeBlock(RegistryObject<Block> blockId, ResourceLocation inventory, ResourceLocation... variations) {
        VariantBlockStateBuilder builder = getVariantBuilder(blockId.get());
        ResourceLocation blockDefault = blockTexture(blockId.get());

        ModelFile model0 = models().cubeAll(name(blockId.get()) + "_inventory", new ResourceLocation(inventory.getNamespace(), "block/" + inventory.getPath())).renderType(new ResourceLocation("translucent"));


        for (int i = 0; i < variations.length; i++) {
            ResourceLocation texture = variations[i];
            ResourceLocation rss = new ResourceLocation(texture.getNamespace(), "block/" + texture.getPath());

            ModelFile cubeModel = models().cubeAll(
                    blockId.getId().getPath() + "_model"+i, // Model name
                    rss // Texture location
            ).renderType(new ResourceLocation("translucent"));
            var cfgModel = ConfiguredModel.builder().modelFile(cubeModel).build();
            //if(i==0)model0 = cubeModel;
            builder.partialState().addModels(cfgModel);
        }




        simpleBlockItem(blockId.get(), model0);
    }
}
