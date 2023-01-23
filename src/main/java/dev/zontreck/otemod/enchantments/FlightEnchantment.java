package dev.zontreck.otemod.enchantments;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class FlightEnchantment extends Enchantment
{
    @Mod.EventBusSubscriber(modid = OTEMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler{
        @SubscribeEvent
        public static void onLivingUpdate(LivingEquipmentChangeEvent ev)
        {
            if(ev.getEntity() instanceof Player)
            {
                if(ev.getEntity().level.isClientSide)return;

                ServerPlayer sp = (ServerPlayer)ev.getEntity();
                ItemStack feet = sp.getItemBySlot(EquipmentSlot.FEET);
                ItemStack legs = sp.getItemBySlot(EquipmentSlot.LEGS);

                boolean hasFlight = false;

                if(feet.getEnchantmentLevel(ModEnchantments.FLIGHT_ENCHANTMENT.get())>0)hasFlight=true;
                if(legs.getEnchantmentLevel(ModEnchantments.FLIGHT_ENCHANTMENT.get())>0)hasFlight=true;

                Abilities playerAbilities = sp.getAbilities();
                if(playerAbilities.mayfly == false)
                {
                    if(hasFlight){
                        playerAbilities.mayfly=true;
                        sp.onUpdateAbilities();
                    }
                }else {
                    if(!hasFlight){

                        playerAbilities.mayfly=false;
                        playerAbilities.flying=false;

                        sp.onUpdateAbilities();
                    }
                }
            }
        }
    }

    public FlightEnchantment()
    {
        super(Rarity.RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[] {EquipmentSlot.FEET, EquipmentSlot.LEGS});

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
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return false;
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

    // Not a bug. Flight is meant to be a permanent upgrade to a item. It is considered a curse due to unstable behavior that can randomly happen if the enchantment level is now maxxed out.
    @Override
    public boolean isCurse()
    {
        return true;
    }
}
