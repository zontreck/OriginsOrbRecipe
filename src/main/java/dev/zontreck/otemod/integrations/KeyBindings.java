package dev.zontreck.otemod.integrations;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {
    public static final String KEY_CATEGORY_OTEMOD = "key.category.otemod";
    public static final String KEY_OPEN_VAULT = "key.otemod.open_vault";

    public static final KeyMapping OPEN_VAULT = new KeyMapping(KEY_OPEN_VAULT, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_OTEMOD);
}
