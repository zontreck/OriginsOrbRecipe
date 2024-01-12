package dev.zontreck.otemod.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BorrowedProtectionEnchantment extends Enchantment
{
    protected BorrowedProtectionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public boolean isCurse() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }


    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }
}
