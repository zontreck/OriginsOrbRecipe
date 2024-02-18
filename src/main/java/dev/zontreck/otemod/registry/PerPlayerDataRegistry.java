package dev.zontreck.otemod.registry;

import dev.zontreck.libzontreck.events.ProfileUnloadedEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PerPlayerDataRegistry {
    // The idea here is to make a registry unique to a player for mod data
    // This will allow separating handling of functions, like cooldowns
    private static Map<UUID, CompoundTag> cache = new HashMap<>();

    public static void put(UUID ID, String nick, Tag tag)
    {
        if(cache.containsKey(ID))
        {
            CompoundTag xTag = cache.get(ID);
            xTag.put(nick, tag);
        }else {
            CompoundTag xTag = new CompoundTag();
            xTag.put(nick,tag);

            cache.put(ID, xTag);
        }
    }

    public static Tag get(UUID ID, String nick)
    {
        if(cache.containsKey(ID))
        {
            CompoundTag tag = cache.get(ID);
            if(tag.contains(nick)) return tag.get(nick);
        }

        return null;
    }


    @SubscribeEvent
    public static void onProfileUnload(ProfileUnloadedEvent ev)
    {
        if(cache.containsKey(UUID.fromString(ev.user_id)))
        {
            cache.remove(UUID.fromString(ev.user_id));
        }
    }
}
