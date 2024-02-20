package dev.zontreck.otemod.blocks;


import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class RegistryObj
{
    public RegistryObject<Block> block;
    public RegistryObject<? extends Item> item;

    public RegistryObj(RegistryObject<Block> blk, RegistryObject<? extends Item> item)
    {
        this.block=blk;
        this.item=item;
    }
}
