package dev.zontreck.otemod.configs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

public class OTEServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<ItemStack>> INITIAL_ITEMS_TO_GIVE_ON_FIRST_JOIN;

    public static final ForgeConfigSpec.ConfigValue<String> HOST_ADDR;
    public static final ForgeConfigSpec.ConfigValue<Integer> PORT;
    public static final ForgeConfigSpec.ConfigValue<String> USERNAME;
    public static final ForgeConfigSpec.ConfigValue<String> PASSWORD;
    public static final ForgeConfigSpec.ConfigValue<String> DATABASE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ITEM_DESPAWN_TIMER;

    static {
        List<ItemStack> defaults = new ArrayList<ItemStack>();
        
        BUILDER.push("OTE");
        INITIAL_ITEMS_TO_GIVE_ON_FIRST_JOIN = BUILDER.comment("What items, identified by modid:item, to give to a brand new user on the server").define("New Player Gear", defaults);
        HOST_ADDR = BUILDER.comment("Database Host (MySQL)").define("host", "127.0.0.1");
        PORT = BUILDER.comment("Database Port (MySQL)").define("port", 3306);
        USERNAME = BUILDER.comment("Database Username (MySQL)").define("user", "ote");
        PASSWORD = BUILDER.comment("Database Password (MySQL)").define("password", "password");
        DATABASE = BUILDER.comment("Database Name (MySQL)").define("database", "otemod");
        ITEM_DESPAWN_TIMER = BUILDER.comment("How many times should the item's expire be cancelled. The vanilla expire time is 5 minutes, so this would be ticked down once every 5 minutes.").define("item_extra_lives", (60/5));


        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
