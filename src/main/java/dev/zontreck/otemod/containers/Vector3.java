package dev.zontreck.otemod.containers;

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
}
