package dev.zontreck.otemod.registry;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.resources.ResourceLocation;

public class ModDimensions
{
    public static final ResourceLocation BUILDER = new ResourceLocation(OTEMod.MOD_ID, "builder");
    public static String BUILDER_DIM()
    {
        return BUILDER.getNamespace() + ":" + BUILDER.getPath();
    }
}
