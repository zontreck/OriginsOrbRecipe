package dev.zontreck.otemod.integrations;

import dev.zontreck.otemod.OTEMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEI implements IModPlugin
{

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(OTEMod.MOD_ID, "jei_plugin");
    }
    
}
