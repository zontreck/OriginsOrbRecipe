package dev.zontreck.otemod.implementation.homes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import dev.zontreck.otemod.implementation.profiles.Profile;
import net.minecraft.nbt.NbtIo;

public class HomesProvider {
    /**
     * DO NOT USE. Internal use only.
     * @see Profile#player_homes
     * @param player
     * @return
     */
    public static Homes getHomesForPlayer(String player)
    {
        Path homesFile = Profile.BASE.resolve(player).resolve("homes.nbt");

        Homes homes = new Homes(player);
        if(homesFile.toFile().exists())
        {
            try {
                homes=Homes.deserialize(NbtIo.read(homesFile.toFile()));
                homes.playerID=player;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return homes;
    }

    public static void commitHomes(Homes playerHomes)
    {

        Path homesFile = Profile.BASE.resolve(playerHomes.playerID).resolve("homes.nbt");

        try {
            NbtIo.write(playerHomes.serialize(), homesFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
