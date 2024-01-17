package dev.zontreck.otemod.integrations;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_CATEGORY_OTEMOD = "key.category.otemod";
    public static final String KEY_OPEN_VAULT = "key.otemod.open_vault";

    public static final KeyMapping OPEN_VAULT = createKeyMapping(KEY_OPEN_VAULT, InputConstants.KEY_V, KEY_CATEGORY_OTEMOD);

    private static KeyMapping createKeyMapping(String name, int keycode, String category){
        final KeyMapping key = new KeyMapping(name, keycode, category);
        return key;
    }

}
