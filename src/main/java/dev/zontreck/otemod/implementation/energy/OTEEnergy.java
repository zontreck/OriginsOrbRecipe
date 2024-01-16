package dev.zontreck.otemod.implementation.energy;

import net.minecraftforge.energy.EnergyStorage;

public abstract class OTEEnergy extends EnergyStorage
{

    public OTEEnergy(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractEnergy(int max, boolean simulate)
    {
        int ex = super.extractEnergy(max, simulate);
        if(ex != 0){
            onChanged();
        }

        return ex;
    }

    @Override
    public int receiveEnergy(int max, boolean simulate)
    {
        int rcv = super.receiveEnergy(max, simulate);
        if(rcv!=0){
            onChanged();
        }

        return rcv;
    }

    public int setEnergy(int energy){
        this.energy=energy;
        onChanged();
        return energy;
    }

    public abstract void onChanged();
    
}
