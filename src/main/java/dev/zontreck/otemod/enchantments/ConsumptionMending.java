package dev.zontreck.otemod.enchantments;

import dev.zontreck.libzontreck.util.ItemUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsumptionMending extends Enchantment
{
    protected ConsumptionMending(EquipmentSlot... slots) {
        super(Rarity.RARE, EnchantmentCategory.BREAKABLE, slots);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 25 + (pLevel-1);
    }

    @Override
    public int getMaxCost(int pLevel) {
        return pLevel * 23;
    }

    private static List<ItemStack> append(ServerPlayer player, List<ItemStack> items)
    {
        List<ItemStack> enchanted = new ArrayList<>();

        for(ItemStack stack : player.getInventory().items)
        {
            if(ItemUtils.getEnchantmentLevel(ModEnchantments.CONSUMPTION_MENDING.get(), stack) > 0)
            {
                enchanted.add(stack);
            }
        }

        return enchanted;
    }

    public static void onEntityTick(ServerPlayer player)
    {
        // Check what items have this enchantment
        // If any, check for like-items that lack the enchant.

        List<ItemStack> enchanted = new ArrayList<>();
        enchanted.addAll(append(player, player.getInventory().items));
        enchanted.addAll(append(player, player.getInventory().armor));

        List<ItemStack> procList = new ArrayList<>();
        procList.addAll(append(player, player.getInventory().offhand));
        procList.addAll(append(player, player.getInventory().items));

        for(ItemStack stack : enchanted)
        {
            for(ItemStack item : player.getInventory().items)
            {
                // Is this a like item, and does it have the enchant?
                boolean eligible = false;
                if(!(ItemUtils.getEnchantmentLevel(ModEnchantments.CONSUMPTION_MENDING.get(), item)>0))
                {
                    eligible=true;
                    if(!item.isDamageableItem())
                    {
                        eligible=false;
                    }
                }

                if(stack.getDamageValue()==0)
                {
                    eligible=false;
                }

                if(eligible)
                {
                    // Let's eat
                    int iDamage = stack.getDamageValue();
                    int iMax = stack.getMaxDamage();

                    int iNeeds = iDamage;

                    // The stack we are inspecting:
                    int nDamage = item.getDamageValue();
                    int nMax = item.getMaxDamage();

                    int iRemain = nMax - nDamage;

                    if(iRemain == iNeeds)
                    {
                        nDamage = nMax;
                        iDamage=0;
                    } else {
                        if(iRemain > iNeeds)
                        {
                            iDamage -= iNeeds;
                            nDamage += iNeeds;
                        } else {
                            iDamage -= iRemain;
                            nDamage = nMax;
                        }
                    }

                    if(nDamage == nMax){
                        // Check for curses on the item
                        if(item.isEnchanted())
                        {
                            Map<Enchantment, Integer> enchantments = ItemUtils.getEnchantments(item);
                            for(Map.Entry<Enchantment,Integer> entry : enchantments.entrySet())
                            {
                                Enchantment id = entry.getKey();
                                int dice = player.getRandom().nextInt(0,20);

                                if(id.isCurse() && ((dice >= 13) && (dice <= 18)))
                                {
                                    stack.enchant(id, entry.getValue());
                                    player.getLevel().playSound(null, player.getOnPos(), SoundEvents.ANVIL_USE, SoundSource.NEUTRAL,1, player.getRandom().nextFloat(0,1));
                                }
                            }
                        }

                        item.shrink(1);
                        player.getLevel().playSound(null, player.getOnPos(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL,1, player.getRandom().nextFloat(0,1));
                    }
                    else item.setDamageValue(nDamage);

                    stack.setDamageValue(iDamage);
                }
            }
        }
    }
}
