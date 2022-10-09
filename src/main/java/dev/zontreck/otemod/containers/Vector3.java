package dev.zontreck.otemod.containers;

import com.ibm.icu.impl.InvalidFormatException;

import net.minecraft.world.phys.Vec3;

public class Vector3 
{
    public double x;
    public double y;
    public double z;
    
    public Vec3 asMinecraftVector(){
        return new Vec3(x, y, z);
    }

    public Vector3()
    {

    }

    public Vector3(double x, double y, double z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Vector3(Vec3 pos)
    {
        x=pos.x;
        y=pos.y;
        z=pos.z;
    }

    public Vector3(String pos) throws InvalidFormatException
    {
        // This will be serialized most likely from the ToString method
        // Parse
        if(pos.startsWith("<"))
        {
            pos=pos.substring(1, pos.length()-1); // Rip off the ending bracket too
            String[] positions = pos.split(", ");
            if(positions.length!=3)
            {
                positions = pos.split(",");
            }

            if(positions.length!=3)
            {
                throw new InvalidFormatException("Positions must be in the same format provided by ToString() (ex. <1,1,1> or <1, 1, 1>");
            }

            this.x = Double.parseDouble(positions[0]);
            this.y = Double.parseDouble(positions[1]);
            this.z = Double.parseDouble(positions[2]);
            // We are done now
        }
    }

    public Vector3 moveUp()
    {
        Vector3 up = Clone();
        up.y+=1;
        return up;
    }
    public Vector3 moveDown()
    {
        Vector3 up = Clone();
        up.y-=1;
        return up;
    }


    public Vector3 Clone()
    {
        Vector3 n = new Vector3(x, y, z);
        return n;
    }

    @Override
    public String toString()
    {
        return "<"+String.valueOf(x)+", "+String.valueOf(y)+", "+String.valueOf(z)+">";
    }
}
