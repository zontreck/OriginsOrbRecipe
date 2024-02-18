package dev.zontreck.otemod.registry;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.resources.ResourceLocation;

public class ModDimensions
{
    public static final ResourceLocation BUILDER = new ResourceLocation(OTEMod.MOD_ID, "builder");
    public static final ResourceLocation THRESHOLD = new ResourceLocation(OTEMod.MOD_ID, "threshold");
    public static String BUILDER_DIM()
    {
        return BUILDER.getNamespace() + ":" + BUILDER.getPath();
    }
    public static String THRESHOLD_DIM()
    {
        return THRESHOLD.getNamespace() + ":" + THRESHOLD.getPath();
    }
}
