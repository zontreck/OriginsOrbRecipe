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

import java.util.concurrent.atomic.AtomicInteger;

public class NightVisionEnchantment extends Enchantment
{

    public NightVisionEnchantment(EquipmentSlot... slots)
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

    public static AtomicInteger TICKS = new AtomicInteger(0);

    public static void runEntityTick(ServerPlayer sp)
    {
        if(ServerUtilities.isClient()) return;

        if(TICKS.getAndIncrement() >= (2*20))
        {
            TICKS.set(0);



            if(ServerConfig.general.debug)
            {
                OTEMod.LOGGER.info("> NVision Enchantment Tick <");
            }


            ItemStack feet = sp.getItemBySlot(EquipmentSlot.HEAD);

            boolean hasNV = false;

            if(ItemUtils.getEnchantmentLevel(ModEnchantments.NIGHT_VISION_ENCHANT.get(), feet)>0)hasNV=true;

            if(hasNV)
            {
                MobEffectInstance inst = new MobEffectInstance(MobEffects.NIGHT_VISION, 60*20, 4, false, false, true);

                sp.addEffect(inst);
            }
        }


    }

}
