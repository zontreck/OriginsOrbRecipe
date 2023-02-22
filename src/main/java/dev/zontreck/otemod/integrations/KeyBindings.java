package dev.zontreck.otemod.integrations;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class KeyBindings {
    public static final String KEY_CATEGORY_OTEMOD = "key.category.otemod";
    public static final String KEY_OPEN_VAULT = "key.otemod.open_vault";

    public static final KeyMapping OPEN_VAULT = registerKeyMapping(KEY_OPEN_VAULT, InputConstants.KEY_V | InputConstants.KEY_LSHIFT, KEY_CATEGORY_OTEMOD);

    private static KeyMapping registerKeyMapping(String name, int keycode, String category){
        final KeyMapping key = new KeyMapping(name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
