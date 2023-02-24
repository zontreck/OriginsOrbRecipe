package dev.zontreck.otemod.implementation.homes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.implementation.events.HomeCreatedEvent;
import dev.zontreck.otemod.implementation.events.HomeDeletedEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class Homes {
    private Map<String, Home> homes = new HashMap<>();

    public String playerID;
    public Homes(String playerID)
    {
        this.playerID=playerID;
    }

    public int count()
    {
        return homes.size();
    }

    public List<Home> getList()
    {
        return new ArrayList<>(homes.values());
    }

    public Home get(String name) throws NoSuchHomeException
    {
        if(homes.containsKey(name))
            return homes.get(name);
        else throw new NoSuchHomeException();
    }

    public void delete(String name) throws NoSuchHomeException
    {
        Home home = homes.get(name);
        HomeDeletedEvent e = new HomeDeletedEvent(home);
        homes.remove(name);
        
        OTEMod.bus.post(e);
        HomesProvider.commitHomes(this);
    }

    public void add(Home toAdd)
    {
        HomeCreatedEvent hce = new HomeCreatedEvent(toAdd);
        OTEMod.bus.post(hce);

        homes.put(toAdd.homeName, toAdd);

        HomesProvider.commitHomes(this);
    }

    public static Homes deserialize(CompoundTag tag)
    {
        Homes ret = new Homes(null);
        ListTag theHomes = tag.getList("homes", Tag.TAG_COMPOUND);
        for (Tag tag2 : theHomes) {
            Home h = new Home((CompoundTag)tag2);
            ret.homes.put(h.homeName, h);
        }

        return ret;
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        ListTag lst = new ListTag();
        for (Map.Entry<String,Home> entry : homes.entrySet()) {
            lst.add(entry.getValue().serialize());
        }
        tag.put("homes", lst);
        return tag;
    }
    
}
