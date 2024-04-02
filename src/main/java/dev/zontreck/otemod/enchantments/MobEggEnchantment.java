package dev.zontreck.otemod.enchantments;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.snbt.ServerConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Random;

public class MobEggEnchantment extends Enchantment
{
    public static final String TAG_BIAS = "mob_egging_bias";

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

    public static boolean givesEgg(int level, int bias)
    {
        int CHANCE = ServerConfig.drops.mobEggingChance;

        CHANCE += level;
        CHANCE += bias;

        if(ServerConfig.general.debug)
        {
            OTEMod.LOGGER.info("Spawn Egg Chance (" + CHANCE + ")");
        }
        return rollChance(CHANCE);
    }

    public static boolean rollChance(int percent)
    {
        Random rng = new Random();
        int test = rng.nextInt(100) + 1 + (100 - percent);
        if(ServerConfig.general.debug)
        {
            OTEMod.LOGGER.info("Spawn Egg Dice Roll (" + test + " / " + percent + ")");
        }

        return test <= percent;
    }
}
