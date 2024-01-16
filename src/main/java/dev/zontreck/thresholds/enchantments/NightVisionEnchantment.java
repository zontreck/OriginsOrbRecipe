package dev.zontreck.thresholds.enchantments;

import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.configs.ThresholdsServerConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = ThresholdsMod.MOD_ID)
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
    @SubscribeEvent
    public static void onEnchantmentTick(TickEvent.PlayerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT) return;

        if(TICKS.getAndIncrement() >= (2*20))
        {
            TICKS.set(0);



            if(ThresholdsServerConfig.DEBUG.get())
            {
                ThresholdsMod.LOGGER.info("> NVision Enchantment Tick <");
            }

            if(event.phase == TickEvent.Phase.END)
            {

                ServerPlayer sp = (ServerPlayer) event.player;

                ItemStack feet = sp.getItemBySlot(EquipmentSlot.HEAD);

                boolean hasNV = false;

                if(ItemUtils.getEnchantmentLevel(ModEnchantments.NIGHT_VISION_ENCHANT.get(), feet)>0)hasNV=true;

                if(hasNV)
                {
                    MobEffectInstance inst = new MobEffectInstance(MobEffects.NIGHT_VISION, 60*20, 4, false, false, true);

                    event.player.addEffect(inst);
                }
            }
        }


    }

}
