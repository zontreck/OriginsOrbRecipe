package dev.zontreck.thresholds.implementation.vault;

import dev.zontreck.thresholds.database.OTEDatastore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StarterProvider extends OTEDatastore
{
    public enum VaultAccessStrategy
    {
        OPEN,
        CREATE,
        DENY
    }

    public static final Path FILE_TREE_PATH = of("starter");

    public static VaultAccessStrategy check()
    {
        if(!FILE_TREE_PATH.toFile().exists())
        {
            try {
                Files.createDirectory(FILE_TREE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path starterVaultPath = FILE_TREE_PATH.resolve("starter.nbt");
        File vaultFile = starterVaultPath.toFile();

        if(vaultFile.exists())
        {
            return VaultAccessStrategy.OPEN;
        }else {
            return VaultAccessStrategy.CREATE;
        }
        
    }

    public static Starter getStarter() throws NoMoreVaultException
    {
        VaultAccessStrategy strat = check();
        if(strat == VaultAccessStrategy.CREATE || strat == VaultAccessStrategy.OPEN)
        {
            Path userVault = FILE_TREE_PATH.resolve("starter.nbt");
            Starter v = new Starter(userVault.toFile());
            return v;
        }else return null;
    }

    public static boolean exists()
    {
        Path v = FILE_TREE_PATH.resolve("starter.nbt");
        return v.toFile().exists();
    }
}
