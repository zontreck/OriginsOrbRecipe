package dev.zontreck.otemod.enchantments;

import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.otemod.OTEMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.SoulSpeedEnchantment;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
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
                if(ev.getEntity().level().isClientSide)return;


                ServerPlayer sp = (ServerPlayer)ev.getEntity();
                recheck(sp);
            }
        }

        private static void recheck(ServerPlayer sp)
        {
            if(sp.gameMode.isCreative())return; // Don't mess with the creative mode attributes

            ItemStack feet = sp.getItemBySlot(EquipmentSlot.FEET);

            boolean hasFlight = false;

            if(ItemUtils.getEnchantmentLevel(ModEnchantments.FLIGHT_ENCHANTMENT.get(), feet)>0)hasFlight=true;

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

        @SubscribeEvent
        public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent ev)
        {
            if(ev.getEntity().level().isClientSide)return;

            recheck((ServerPlayer)ev.getEntity());
        }
    }

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
}
