package dev.zontreck.otemod.enchantments;

import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.libzontreck.util.ServerUtilities;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.snbt.ServerConfig;
import dev.zontreck.otemod.effects.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.concurrent.atomic.AtomicInteger;

public class FlightEnchantment extends Enchantment
{

    public FlightEnchantment(EquipmentSlot... slots)
    {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, slots);
    }
    
    @Override
    public int getMaxLevel()
    {
        return 1;
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
        return true;
    }


    public static AtomicInteger TICKS = new AtomicInteger(0);

    public static void runEntityTick(ServerPlayer sp)
    {
        if(ServerUtilities.isClient()) return;

        if(TICKS.getAndIncrement() >= 20)
        {
            TICKS.set(0);



            if(ServerConfig.general.debug)
            {
                OTEMod.LOGGER.info("> Flight Enchantment Tick <");
            }

            ItemStack feet = sp.getItemBySlot(EquipmentSlot.FEET);

            boolean hasFlight = false;

            if(ItemUtils.getEnchantmentLevel(ModEnchantments.FLIGHT_ENCHANTMENT.get(), feet)>0)hasFlight=true;

            if(hasFlight)
            {
                MobEffectInstance inst = new MobEffectInstance(ModEffects.FLIGHT.get(), 60*20, 0, false, false, true);
                MobEffectInstance existing = sp.getEffect(ModEffects.FLIGHT.get());

                if(existing != null)
                {
                    if(existing.getDuration() <= (30 * 20))
                    {
                        sp.addEffect(inst);
                        return;
                    }else return;
                }

                sp.addEffect(inst);
            }
        }


    }

}
