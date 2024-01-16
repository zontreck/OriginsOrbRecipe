package dev.zontreck.thresholds.items.tags;

import dev.zontreck.libzontreck.chat.ChatColor;

public class ItemStatistics {

    public static String makeText(ItemStatTag tag)
    {
        return makeText(tag.type, tag.value);
    }
    public static String makeText(ItemStatType type, int val)
    {
        String lore = ChatColor.doColors("!White!");
        switch(type)
        {
            case SWORD -> {
                lore += "Mobs Killed: ";
            }
            case PICK -> {
                lore += "Blocks Mined: ";
            }
            case ARMOR -> {
                lore += "Damage Taken: ";
            }
            case SHOVEL -> {
                lore += "Blocks Dug Up: ";
            }
            case SHOVELPATH -> {
                lore += "Paths Made: ";
            }
            case AXE -> {
                lore += "Wood Chopped: ";
            }
            case HOE -> {
                lore += "Blocks Hoed: ";
            }
            case SHEARS -> {
                lore += "Sheep Shaved: ";
            }
            case EGGING -> {
                lore += "Mobs Egged: ";
            }
        }
        lore += ChatColor.doColors("!Green!"+val);
        return lore;
    }
}
