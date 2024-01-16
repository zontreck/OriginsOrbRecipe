package dev.zontreck.thresholds.events;

import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.libzontreck.util.heads.HeadUtilities;
import dev.zontreck.thresholds.ThresholdsMod;
import dev.zontreck.thresholds.configs.ThresholdsServerConfig;
import dev.zontreck.thresholds.enchantments.MobEggEnchantment;
import dev.zontreck.thresholds.enchantments.ModEnchantments;
import dev.zontreck.thresholds.implementation.DeathMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Mod.EventBusSubscriber(modid= ThresholdsMod.MOD_ID)
public class EventHandler {


    @SubscribeEvent (priority = EventPriority.HIGHEST)
    public static void playerDied(LivingDeathEvent event)
    {
        if(!(event.getEntity() instanceof Player))return;

        Profile profile;
        try {
            profile = Profile.get_profile_of(event.getEntity().getStringUUID());
        } catch (UserProfileNotYetExistsException e) {
            e.printStackTrace();
            return;
        }
        profile.deaths++;
        profile.commit();
        if(!ThresholdsServerConfig.ENABLE_PLAYER_HEAD_DROPS.get())
        {
            return;
        }
        int looting=0;
        //ServerPlayer killedPlayer = (ServerPlayer)ent;
        if(event.getEntity() instanceof Player){
            ServerPlayer pla = profile.player;
            looting = ItemUtils.getEnchantmentLevel(Enchantments.MOB_LOOTING, pla.getMainHandItem());

        }

        // Calculate chance
        double base_chance = ThresholdsServerConfig.CHANCE_OF_PLAYER_HEAD.get();
        base_chance += looting;
        base_chance *= 100;

        Random rng = new Random();
        double num = rng.nextDouble(0,100000);
        if(num <= base_chance)
        {
            ItemStack head = HeadUtilities.get(profile.username, "").setHoverName(ChatHelpers.macro(profile.nickname+"'s Head"));
            LoreContainer lore = new LoreContainer(head);
            LoreEntry entry = new LoreEntry.Builder().bold(true).text(ChatHelpers.macroize("!dark_green!Player: " + profile.name_color+profile.username)).build();
            lore.miscData.loreData.add(entry);

            entry = new LoreEntry.Builder().text(ChatHelpers.macroize("!Dark_Purple!Date: !Dark_Red![0]", Date.from(Instant.now()).toString())).build();
            lore.miscData.loreData.add(entry);

            entry = new LoreEntry.Builder().text(ChatHelpers.macroize("!Dark_Purple!Total Deaths: !Dark_Red![0]", String.valueOf(profile.deaths))).build();
            lore.miscData.loreData.add(entry);
            lore.commitLore();


            event.getEntity().spawnAtLocation(head);
        }

        try {
            ChatHelpers.broadcast(Component.literal(DeathMessages.getRandomDeathMessage(Profile.get_profile_of(event.getEntity().getStringUUID()), event.getSource())), event.getEntity().level().getServer());
        } catch (UserProfileNotYetExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onEntityKilled(LivingDropsEvent ev){
        if(ev.getEntity().level().isClientSide)return;

        Entity killedentity = ev.getEntity();
        Entity ent = ev.getSource().getEntity();
        if(ent instanceof Player)
        {
            ServerPlayer play = (ServerPlayer)ent;
            LivingEntity killed = ev.getEntity();

            ItemStack stack = play.getMainHandItem();
            int levelOfEgging = ItemUtils.getEnchantmentLevel(ModEnchantments.MOB_EGGING_ENCHANTMENT.get(),stack);
            
            if(levelOfEgging==0)return;
            CompoundTag tag = stack.getTag();
            int bias = tag.getInt(MobEggEnchantment.TAG_BIAS);


            if(MobEggEnchantment.givesEgg(levelOfEgging, bias))
            {
                bias=0;
                tag.putInt(MobEggEnchantment.TAG_BIAS, bias);
                // .25% chance
                // Check enchantment level for looting
                int level = ItemUtils.getEnchantmentLevel (Enchantments.MOB_LOOTING,stack);
                if(level==3){
                    ItemStack egg = new ItemStack(ForgeSpawnEggItem.fromEntityType(killed.getType()));
                    ev.getDrops().add(new ItemEntity(killed.level(), killed.getX(), killed.getY(), killed.getZ(), egg));
                    //LoreHandlers.updateItem(stack, ItemStatType.EGGING);
                }
            }else{
                bias += 1;
                tag.putInt(MobEggEnchantment.TAG_BIAS, bias);
            }
        }

    }

    
}
