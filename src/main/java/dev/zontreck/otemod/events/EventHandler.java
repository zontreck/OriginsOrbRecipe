package dev.zontreck.otemod.events;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.enchantments.MobEggEnchantment;
import dev.zontreck.otemod.enchantments.ModEnchantments;
import dev.zontreck.otemod.items.tags.ItemStatType;
import dev.zontreck.otemod.items.tags.ItemStatistics;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber(modid=OTEMod.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    /*
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addOresToBiomes(final BiomeLoadingEvent ev){
        //ShapedAionResources.LOGGER.info("Biome loading event called. Registering aion ores");
        OreGenerator.generateOres(ev);
    }*/

    @SubscribeEvent
    public void onEntityKilled(LivingDropsEvent ev){
        if(ev.getEntity().level.isClientSide)return;

        Entity ent = ev.getSource().getEntity();
        if(ent instanceof Player)
        {
            ServerPlayer play = (ServerPlayer)ent;
            LivingEntity killed = ev.getEntity();

            ItemStack stack = play.getMainHandItem();
            int levelOfEgging = stack.getEnchantmentLevel(ModEnchantments.MOB_EGGING_ENCHANTMENT.get());
            
            if(levelOfEgging==0)return;
            CompoundTag tag = stack.getTag();
            int bias = tag.getInt(MobEggEnchantment.TAG_BIAS);


            if(MobEggEnchantment.givesEgg(levelOfEgging, bias))
            {
                bias=0;
                tag.putInt(MobEggEnchantment.TAG_BIAS, bias);
                // .25% chance
                // Check enchantment level for looting
                int level = stack.getEnchantmentLevel(Enchantments.MOB_LOOTING);
                if(level==3){
                    ItemStack egg = new ItemStack(ForgeSpawnEggItem.fromEntityType(killed.getType()));
                    ev.getDrops().add(new ItemEntity(killed.level, killed.getX(), killed.getY(), killed.getZ(), egg));
                    LoreHandlers.updateItem(stack, ItemStatType.EGGING);
                }
            }else{
                bias += 1;
                tag.putInt(MobEggEnchantment.TAG_BIAS, bias);
            }
        }
    }
}
