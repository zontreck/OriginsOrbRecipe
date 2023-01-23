package dev.zontreck.otemod.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MobEggEnchantment extends Enchantment
{

    public MobEggEnchantment()
    {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel()
    {
        return 6;
    }

    @Override
    public int getMinCost(int level)
    {
        return 28 + (level - 1) * 15;
    }

    @Override
    public int getMaxCost(int level)
    {
        return this.getMinCost(level) + 15;
    }


    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return super.canApplyAtEnchantingTable(stack);
    }

    @Override
    public boolean isTreasureOnly(){
        return false;
    }
    @Override
    public boolean isTradeable()
    {
        return true;
    }

    @Override
    public boolean isDiscoverable()
    {
        return false;
    }
    
}
