package dev.zontreck.shapedaionresources.configs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

public class SARServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<ItemStack>> INITIAL_ITEMS_TO_GIVE_ON_FIRST_JOIN;
    

    static {
        List<ItemStack> defaults = new ArrayList<ItemStack>();
        
        BUILDER.push("Configuration for Shaped Aion Cubes Resources");
        INITIAL_ITEMS_TO_GIVE_ON_FIRST_JOIN = BUILDER.comment("What items, identified by modid:item, to give to a brand new user on the server").define("New Player Gear", defaults);

        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
