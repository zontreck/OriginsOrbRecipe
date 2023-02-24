package dev.zontreck.otemod.implementation.profiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.zontreck.libzontreck.LibZontreck.ForgeEventBus;
import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.database.FileTreeDatastore;
import dev.zontreck.otemod.implementation.events.ProfileCreatedEvent;
import dev.zontreck.otemod.implementation.homes.Homes;
import dev.zontreck.otemod.implementation.homes.HomesProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Profile {
    public String username;
    public String user_id;
    public String prefix;
    public String nickname;
    public String name_color; // ChatColor.X
    public String prefix_color;
    public String chat_color;
    public Boolean flying;
    public int available_vaults;
    public Homes player_homes;

    private File accessor;

    public static final Path BASE = FileTreeDatastore.of("profiles");

    public Profile(String username, String prefix, String nickname, String name_color, String ID, String prefix_color, String chat_color, Boolean isFlying, int vaults, File vaultFile) {
        this.username = username;
        this.prefix = prefix;
        this.nickname = nickname;
        this.name_color = name_color;
        this.user_id = ID;
        this.prefix_color = prefix_color;
        this.chat_color = chat_color;
        this.flying=isFlying;
        this.available_vaults=vaults;
        player_homes = HomesProvider.getHomesForPlayer(ID);


        this.accessor = vaultFile;
    }


    public static Profile get_profile_of(String UUID) throws UserProfileNotYetExistsException
    {
        if(OTEMod.PROFILES.containsKey(UUID)){
            return OTEMod.PROFILES.get(UUID);
        }else {
            // Create or load profile
            Path userProfile = BASE.resolve(UUID);
            if(userProfile.toFile().exists())
            {
                // Load profile data
                File ace = userProfile.resolve("profile.dat").toFile();
                try {
                    Profile actual = load(NbtIo.read(ace), ace);
                    OTEMod.PROFILES.put(UUID, actual);
                    return actual;
                } catch (IOException e) {
                    throw new UserProfileNotYetExistsException(UUID);
                }

            }else {
                // Create directory, then throw a exception so a new profile gets created
                try {
                    Files.createDirectories(userProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                throw new UserProfileNotYetExistsException(UUID);
            }
        }
    }

    private static Profile load(CompoundTag tag, File accessor)
    {
        return new Profile(tag.getString("user"), tag.getString("prefix"), tag.getString("nick"), tag.getString("nickc"), tag.getString("id"), tag.getString("prefixc"), tag.getString("chatc"), tag.getBoolean("flying"), tag.getInt("vaults"), accessor);
    }

    private static void generateNewProfile(ServerPlayer player)
    {
        
        Path userProfile = BASE.resolve(player.getStringUUID());
        if(userProfile.toFile().exists())
        {
            // Load profile data
            File ace = userProfile.resolve("profile.dat").toFile();
            Profile template = new Profile(player.getName().getString(), "Member", player.getDisplayName().getString(), ChatColor.GREEN, player.getStringUUID(), ChatColor.AQUA, ChatColor.WHITE, false, 0, ace);
            template.commit();

            
            OTEMod.bus.post(new ProfileCreatedEvent(template));

            template=null;
            return;
        }else {
            try {
                Files.createDirectories(userProfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            generateNewProfile(player);
        }
    }

    @Override
    public void finalize()
    {
        OTEMod.LOGGER.info("Profile is unloaded for "+username);
    }

    public static void unload(Profile prof)
    {
        OTEMod.PROFILES.remove(prof.user_id);
        prof=null;
    }

    public static Profile factory(ServerPlayer play)
    {
        try {
            return get_profile_of(play.getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            generateNewProfile(play);
            return factory(play);
        }
    }

    public void commit()
    {
        // Save data to FileTree
        CompoundTag serial = new CompoundTag();
        serial.putString("user", username);
        serial.putString("prefix", prefix);
        serial.putString("nick", nickname);
        serial.putString("id", user_id);
        serial.putString("nickc", name_color);
        serial.putString("prefixc", prefix_color);
        serial.putString("chatc", chat_color);
        serial.putBoolean("flying", flying);
        serial.putInt("vaults", available_vaults);



        try {
            NbtIo.write(serial, accessor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
