package dev.zontreck.otemod.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;

public class DeathMessages {
    private static final List<String> messages;
    private static final List<String> messages_falling;
    
    static{
        messages=new ArrayList<>();
        messages_falling = new ArrayList<>();
        messages.add("!Dark_Red!On their [0] death, [1]!Dark_Red! was reduced to a jittering flesh pile");
        messages.add("!Dark_Red![1]!Dark_Red! experienced their [0] death, while running away in fear from [2]");
        messages.add("!Dark_Red![1] was eaten alive by [2]");
        messages.add("!Dark_Red!For their [0] death, [1]!Dark_Red! got a little bit too careless!");

    }

    public static String getRandomDeathMessage(Profile playerWhoDied, DamageSource source)
    {
        Random rng = new Random();
        int msg = rng.nextInt(0, messages.size()-1);
        String sourceName="";
        if(source.isCreativePlayer())
        {
            sourceName = "A godlike player";
        }
        if(source.is(DamageTypes.FALL))
        {
            msg = rng.nextInt(0,messages_falling.size()-1);
            return ChatHelpers.macroize(messages_falling.get(msg), String.valueOf(playerWhoDied.deaths), playerWhoDied.name_color + playerWhoDied.nickname);
        }

        if(source.getEntity() != null)
        {
            if(sourceName.equals(""))
            {
                sourceName = source.getEntity().getName().getString();
            }
        } else sourceName = "an imaginary zombie!";

        return ChatHelpers.macroize(messages.get(msg), String.valueOf(playerWhoDied.deaths), playerWhoDied.name_color + playerWhoDied.nickname, sourceName);
    }
}
