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
    public static final ForgeConfigSpec.ConfigValue<Float> SPAWN_EGG_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<String> HOST_ADDR;
    public static final ForgeConfigSpec.ConfigValue<Integer> PORT;
    public static final ForgeConfigSpec.ConfigValue<String> USERNAME;
    public static final ForgeConfigSpec.ConfigValue<String> PASSWORD;
    public static final ForgeConfigSpec.ConfigValue<String> DATABASE;
    public static final ForgeConfigSpec.ConfigValue<Integer> ITEM_DESPAWN_TIMER;


    public static final ForgeConfigSpec.ConfigValue<Integer> RTP_COOLDOWN;


    public static final ForgeConfigSpec.ConfigValue<Integer> HEALER_TIMER;
    public static final ForgeConfigSpec.BooleanValue DEBUG_HEALER;
    public static final ForgeConfigSpec.ConfigValue<Integer> TIME_BETWEEN_BLOCKS;
    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_TRIES_HEAL;
    public static final ForgeConfigSpec.ConfigValue<List<String>> EXCLUDE_DIMS;


    public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_JOINLEAVE;
    public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_CHATREPLACER;


    static {
        List<ItemStack> defaults = new ArrayList<ItemStack>();
        List<String> defaultExcludeDimensions = new ArrayList<String>();
        defaultExcludeDimensions.add("minecraft:the_nether"); // Excluded to make mining for Ancient Debris easier
        defaultExcludeDimensions.add("minecraft:the_end"); // Excluded due to End Crystals
        
        BUILDER.push("OTE");
        INITIAL_ITEMS_TO_GIVE_ON_FIRST_JOIN = BUILDER.comment("What items, identified by modid:item, to give to a brand new user on the server").define("New Player Gear", defaults);
        SPAWN_EGG_CHANCE = BUILDER.comment("What is the chance for a spawn egg to drop from a mob when looting 3 is used? Default: 0.25").define("spawn_egg_chance", 0.25F);
        BUILDER.pop();

        BUILDER.push("DATABASE");
        HOST_ADDR = BUILDER.comment("Database Host (MySQL)").define("host", "127.0.0.1");
        PORT = BUILDER.comment("Database Port (MySQL)").define("port", 3306);
        USERNAME = BUILDER.comment("Database Username (MySQL)").define("user", "ote");
        PASSWORD = BUILDER.comment("Database Password (MySQL)").define("password", "password");
        DATABASE = BUILDER.comment("Database Name (MySQL)").define("database", "otemod");
        ITEM_DESPAWN_TIMER = BUILDER.comment("How many times should the item's expire be cancelled. The vanilla expire time is 5 minutes, so this would be ticked down once every 5 minutes.").define("item_extra_lives", (60/5));
        BUILDER.pop();
        BUILDER.push("COMMANDS");

        RTP_COOLDOWN = BUILDER.comment("How many seconds between RTP uses? This can be quite laggy on the server due to the potential that new chunks are getting generated").define("rtp.cooldown", 30); // Default of 30 should be enough

        BUILDER.pop();
        BUILDER.push("ANTIGRIEF").comment("AntiGrief Explosion Healing Events");
        HEALER_TIMER = BUILDER.comment("Time between healing events (In Milliseconds)").define("timer", 250); // Should this be lower?
        DEBUG_HEALER = BUILDER.comment("Whether or not to debug the healer engine.  (Saves as SNBT instead of NBT)").define("debug", false);
        TIME_BETWEEN_BLOCKS = BUILDER.comment("The amount of time between restoring blocks (Maximum). This is in ticks").define("time_between", 25);
        
        MAX_TRIES_HEAL = BUILDER.comment("Maximum amount of retries to restore a block").define("max_tries", 6);

        List<String> defDims = new ArrayList<String>();
        defDims.add("minecraft:the_end");
        defDims.add("minecraft:the_nether");
        defDims.add("otemod:resource");
        defDims.add("otemod:resource_nether");
        EXCLUDE_DIMS = BUILDER.comment("Dimension names (ex. minecraft:overworld) to exclude from the explosion healing events").define("exclude_dimensions", defDims);

        BUILDER.pop();

        BUILDER.push("CHATSERVER");
        USE_CUSTOM_JOINLEAVE = BUILDER.comment("Whether to use the custom join and leave messages").define("join_leave_messages", true);
        USE_CUSTOM_CHATREPLACER = BUILDER.comment("Whether to use the custom chat replacer (If disabled the relevant commands will be removed)").define("chatprettifier", true);

        BUILDER.pop();
        SPEC=BUILDER.build();
    }
}
