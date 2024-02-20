package dev.zontreck.otemod.data;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockModelsProvider extends BlockModelProvider
{
    public ModBlockModelsProvider(PackOutput output, ExistingFileHelper helper)
    {
        super(output, OTEMod.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {

        // Model file paths
        String cubeModel = "block/cube";
        String sideTexture = "#side";
        String frontTexture = "#front";

        // Model Builder for your block
        cubeAll("rotatable", modLoc(cubeModel))
                .texture("particle", modLoc(sideTexture))
                .texture("down", modLoc(sideTexture))
                .texture("up", modLoc(sideTexture))
                .texture("north", modLoc(frontTexture))
                .texture("east", modLoc(sideTexture))
                .texture("south", modLoc(sideTexture))
                .texture("west", modLoc(sideTexture));
    }
}
