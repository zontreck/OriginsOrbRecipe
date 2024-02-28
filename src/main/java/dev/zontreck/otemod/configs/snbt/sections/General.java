package dev.zontreck.otemod.configs.snbt.sections;

import net.minecraft.nbt.CompoundTag;

public class General
{
    public static final String TAG_NAME = "general";
    public static final String TAG_DEBUG = "debug";
    public static final String TAG_STARTER_KIT = "givesStarterKitOnChange";
    public static final String TAG_ALLOW_BUILDER_DIM = "allowNonOppedBuilders";
    public static final String TAG_MAX_VAULTS = "maxVaults";
    public static final String TAG_INFINITE_VAULTS = "infiniteVaults";
    public static final String TAG_ITEM_DESPAWN_TIMER = "itemDespawnTimer";
    public static final String TAG_DEATH_MESSAGES = "enableDeathMessages";




    public boolean debug = false;
    public boolean givesStarterKitOnChanged = false;
    public boolean allowBuilder = false;
    public int maxVaults = 27;
    public boolean infiniteVaults = false;
    public int itemDespawnTimer = 2;
    public boolean enableDeathMessages = true;


    public CompoundTag save()
    {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_DEBUG, debug);
        tag.putBoolean(TAG_STARTER_KIT, givesStarterKitOnChanged);
        tag.putBoolean(TAG_ALLOW_BUILDER_DIM, allowBuilder);
        tag.putInt(TAG_MAX_VAULTS, maxVaults);
        tag.putBoolean(TAG_INFINITE_VAULTS, infiniteVaults);
        tag.putInt(TAG_ITEM_DESPAWN_TIMER, itemDespawnTimer);
        tag.putBoolean(TAG_DEATH_MESSAGES, enableDeathMessages);


        return tag;
    }

    public static General load(CompoundTag tag)
    {
        General general = new General();
        if(tag.contains(TAG_DEBUG))
            general.debug = tag.getBoolean(TAG_DEBUG);

        if(tag.contains(TAG_STARTER_KIT))
            general.givesStarterKitOnChanged = tag.getBoolean(TAG_STARTER_KIT);

        if(tag.contains(TAG_ALLOW_BUILDER_DIM))
            general.allowBuilder = tag.getBoolean(TAG_ALLOW_BUILDER_DIM);

        if(tag.contains(TAG_MAX_VAULTS))
            general.maxVaults = tag.getInt(TAG_MAX_VAULTS);

        if(tag.contains(TAG_INFINITE_VAULTS))
            general.infiniteVaults = tag.getBoolean(TAG_INFINITE_VAULTS);

        if(tag.contains(TAG_ITEM_DESPAWN_TIMER))
            general.itemDespawnTimer = tag.getInt(TAG_ITEM_DESPAWN_TIMER);

        if(tag.contains(TAG_DEATH_MESSAGES))
            general.enableDeathMessages = tag.getBoolean(TAG_DEATH_MESSAGES);



        return general;
    }
}
