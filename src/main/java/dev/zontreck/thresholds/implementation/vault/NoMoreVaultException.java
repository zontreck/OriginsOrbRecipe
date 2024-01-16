package dev.zontreck.thresholds.implementation.vault;

/**
 * You cannot create or open the vault due to server settings
 */
public class NoMoreVaultException extends Exception
{
    public int vault;
    public NoMoreVaultException(String msg, int vaultNum)
    {
        super(msg);
        vault=vaultNum;
    }
    
}
