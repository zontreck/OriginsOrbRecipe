package dev.zontreck.otemod.implementation.vault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.database.OTEDatastore;

public class VaultProvider extends OTEDatastore 
{
    public enum VaultAccessStrategy
    {
        OPEN,
        CREATE,
        DENY
    }

    public static final Path FILE_TREE_PATH = of("vaults");

    public static VaultAccessStrategy check(Profile profile, int vaultNum)
    {
        if(!FILE_TREE_PATH.toFile().exists())
        {
            try {
                Files.createDirectory(FILE_TREE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path userVaultPath = FILE_TREE_PATH.resolve(profile.user_id);
        File vaultFile = userVaultPath.resolve(String.valueOf(vaultNum)+".nbt").toFile();


        if(!userVaultPath.toFile().exists()){
            try {
                Files.createDirectory(userVaultPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(vaultFile.exists())
        {
            return VaultAccessStrategy.OPEN;
        }else {
            List<File> files = getListOfFiles(userVaultPath);

            if(isAtMaxVaults(profile, files.size()))
            {
                return VaultAccessStrategy.DENY;
            }else {
                return VaultAccessStrategy.CREATE;
            }
        }
        
    }

    public static int getInUse(Profile prof)
    {
        Path uservaults = FILE_TREE_PATH.resolve(prof.user_id);
        return getListOfFiles(uservaults).size();
    }

    public static boolean isAtMaxVaults(Profile prof, int consumed)
    {
        if(OTEServerConfig.MAX_VAULTS.get()==0){
            if(consumed < prof.available_vaults){
                return false;
            }else return true;
        }
        if(prof.available_vaults >= OTEServerConfig.MAX_VAULTS.get())
        {
            if(consumed<prof.available_vaults)
            {
                return false;
            }
            return true;
        }else{
            if(consumed<prof.available_vaults)return false;
            else return true;
        }
    }

    public static Vault get(Profile profile, int vault) throws NoMoreVaultException
    {
        VaultAccessStrategy strat = check(profile,vault);
        if(strat == VaultAccessStrategy.CREATE || strat == VaultAccessStrategy.OPEN)
        {
            Path userVault = FILE_TREE_PATH.resolve(profile.user_id);
            Vault v = new Vault(vault, userVault.resolve(String.valueOf(vault)+".nbt").toFile(), profile);
            return v;
        }else throw new NoMoreVaultException("Cannot open due to server limits", vault);
    }
}
