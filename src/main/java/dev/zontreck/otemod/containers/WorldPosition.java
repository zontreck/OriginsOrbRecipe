package dev.zontreck.otemod.containers;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.exceptions.InvalidDeserialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class WorldPosition
{
    
    public Vector3 Position;
    public String Dimension;

    public WorldPosition(CompoundTag tag) throws InvalidDeserialization
    {
        Position = new Vector3(tag.getString("Position"));
        Dimension = tag.getString("Dimension");

    }

    public WorldPosition(Vector3 pos, String dim)
    {
        Position=pos;
        Dimension=dim;
    }

    public WorldPosition(Vector3 pos, ServerLevel lvl)
    {
        Position=pos;
        Dimension = lvl.dimension().location().getNamespace() + ":"+lvl.dimension().location().getPath();
    }

    @Override
    public String toString()
    {
        return NbtUtils.structureToSnbt(serialize());
    }

    public CompoundTag serialize()
    {
        CompoundTag tag = new CompoundTag();
        
        tag.putString("Position", Position.toString());
        tag.putString("Dimension", Dimension);

        return tag;
    }

    

    public ServerLevel getActualDimension()
    {
        
        String dim = Dimension;
        String[] dims = dim.split(":");

        ResourceLocation rl = new ResourceLocation(dims[0], dims[1]);
        ServerLevel dimL  = null;
        for (ServerLevel lServerLevel : OTEMod.THE_SERVER.getAllLevels()) {
            ResourceLocation XL = lServerLevel.dimension().location();

            if(XL.getNamespace().equals(rl.getNamespace())){
                if(XL.getPath().equals(rl.getPath())){
                    dimL = lServerLevel;
                }
            }
        }

        if(dimL == null)
        {
            OTEMod.LOGGER.error("DIMENSION COULD NOT BE FOUND : "+Dimension);
            return null;
        }

        return dimL;
    }
}
