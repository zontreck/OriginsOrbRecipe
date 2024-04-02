package dev.zontreck.otemod.enchantments;

import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.snbt.ServerConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WaterBreathingEnchantment extends Enchantment
{

    public WaterBreathingEnchantment(EquipmentSlot... slots)
    {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, slots);
    }
    
    @Override
    public int getMaxLevel()
    {
        return 1;
    }
    
    @Override
    public boolean isTreasureOnly(){
        return true;
    }
    @Override
    public boolean isTradeable()
    {
        return true;
    }

    // Not a bug. Flight is meant to be a permanent upgrade to a item. It is considered a curse due to unstable behavior. Flight will eat up durability and forge energy
    // Flight should NOT be able to be removed via the grindstone
    @Override
    public boolean isCurse()
    {
        return false;
    }

    public static void runEntityTick(ServerPlayer sp)
    {
        if(ServerUtilities.isClient()) return;


        if(ServerConfig.general.debug)
        {
            OTEMod.LOGGER.info("> WBreath Enchantment Tick <");
        }


        ItemStack feet = sp.getItemBySlot(EquipmentSlot.HEAD);

        boolean hasEnchantment = false;

        if(ItemUtils.getEnchantmentLevel(ModEnchantments.WATER_BREATHING_ENCHANT.get(), feet)>0)hasEnchantment=true;

        if(hasEnchantment)
        {
            MobEffectInstance inst = new MobEffectInstance(MobEffects.WATER_BREATHING, 60*20, 4, false, false, true);

            MobEffectInstance existing = sp.getEffect(MobEffects.WATER_BREATHING);
            if(existing != null)
            {
                if(existing.getDuration() <= (30*20))
                {
                    sp.addEffect(inst);
                    return;
                }else return;
            }

            sp.addEffect(inst);
        }


    }

}
