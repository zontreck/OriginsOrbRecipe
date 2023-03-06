package dev.zontreck.otemod.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.world.entity.Entity;

public class DeathMessages {
    private static final List<String> messages;
    
    static{
        messages=new ArrayList<>();
        messages.add("!Dark_Red!On their [0] death, [1] was reduced to a jittering flesh pile");
        messages.add("!Dark_Red![1] experienced their [0] death, while running away in fear from [2]");
        messages.add("!Dark_Red![1] was eaten alive by [2]");
        messages.add("!Dark_Red!For their [0] death, [1] got a little bit too careless!");
    }

    public static String getRandomDeathMessage(Profile playerWhoDied, Entity runningFrom)
    {
        Random rng = new Random();
        int msg = rng.nextInt(0, messages.size()-1);

        return ChatHelpers.macroize(messages.get(msg), String.valueOf(playerWhoDied.deaths), playerWhoDied.name_color + playerWhoDied.nickname, runningFrom.getName().getContents());
    }
}
