package dev.zontreck.otemod.events;

import dev.zontreck.libzontreck.events.TeleportEvent;
import dev.zontreck.libzontreck.lore.LoreContainer;
import dev.zontreck.libzontreck.lore.LoreEntry;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.libzontreck.util.ItemUtils;
import dev.zontreck.libzontreck.util.heads.HeadUtilities;
import dev.zontreck.libzontreck.vectors.Vector3;
import dev.zontreck.libzontreck.vectors.WorldPosition;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.configs.snbt.ServerConfig;
import dev.zontreck.otemod.enchantments.MobEggEnchantment;
import dev.zontreck.otemod.enchantments.ModEnchantments;
import dev.zontreck.otemod.implementation.DeathMessages;
import dev.zontreck.otemod.implementation.InventoryBackup;
import dev.zontreck.otemod.implementation.Messages;
import dev.zontreck.otemod.items.tags.ItemStatType;
import dev.zontreck.otemod.registry.ModDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class EventHandler {


    @SubscribeEvent (priority = EventPriority.HIGHEST)
    public void playerDied(LivingDeathEvent event)
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
        if(!ServerConfig.drops.enablePlayerHeadChance)
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
        double base_chance = (ServerConfig.drops.playerHeadChance * 100);
        base_chance += looting;

        Random rng = new Random();
        int num = rng.nextInt(100) + 1;
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

        if(ServerConfig.general.enableDeathMessages)
        {

            try {
                ChatHelpers.broadcast(Component.literal(DeathMessages.getRandomDeathMessage(Profile.get_profile_of(event.getEntity().getStringUUID()), event.getSource())), event.getEntity().level().getServer());
            } catch (UserProfileNotYetExistsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerFallOutOfWorld(LivingHurtEvent ev)
    {
        if(ev.getEntity() instanceof Player player)
        {
            ResourceLocation loc = player.level().dimension().location();

            if(ev.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) && (loc.getNamespace().equals(OTEMod.MOD_ID) || loc.getNamespace().equals("minecraft")))
            {
                // Teleport the player to Thresholds
                WorldPosition pos = new WorldPosition(new Vector3(), ModDimensions.THRESHOLD_DIM());
                ServerPlayer sp = (ServerPlayer) ev.getEntity();
                if(!MinecraftForge.EVENT_BUS.post(new TeleportEvent(pos, sp)))
                    sp.teleportTo(pos.getActualDimension(), 0, 365, 0, 0,0);


            }

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
                    ItemStack egg = new ItemStack(
                            ForgeSpawnEggItem.fromEntityType(killed.getType())
                    );


                    ev.getDrops().add(new ItemEntity(killed.level(), killed.getX(), killed.getY(), killed.getZ(), egg));
                    LoreHandlers.updateItem(stack, ItemStatType.EGGING);
                }
            }else{
                bias += 1;
                tag.putInt(MobEggEnchantment.TAG_BIAS, bias);
            }
        }

    }

    private static final ResourceLocation THRESHOLDS_BIMENSION = new ResourceLocation(OTEMod.MOD_ID, "threshold");

    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent ev)
    {
        if(ev.getEntity().level().isClientSide) return;

        ServerPlayer player = (ServerPlayer) ev.getEntity();

        if(ev.getTo().location().equals(ModDimensions.BUILDER))
        {
            // Store the player's inventory
            changedGameMode(player, InventoryBackup.GameMode.fromGameType(player.gameMode.getGameModeForPlayer()), InventoryBackup.GameMode.Builder);


            player.setGameMode(GameType.CREATIVE);
        }

        if(ev.getFrom().location().equals(ModDimensions.BUILDER))
        {
            // Restore the player's inventory
            InventoryBackup bkp = new InventoryBackup(player, InventoryBackup.GameMode.Builder);
            bkp.restore();

            player.setGameMode(bkp.getFormerGameMode().toMinecraft());

            //changedGameMode(player, InventoryBackup.GameMode.Builder, bkp.getFormerGameMode());
        }
    }


    @SubscribeEvent
    public void onGameModeChanged(PlayerEvent.PlayerChangeGameModeEvent event)
    {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        InventoryBackup.GameMode from = InventoryBackup.GameMode.fromGameType(event.getCurrentGameMode());
        InventoryBackup.GameMode to = InventoryBackup.GameMode.fromGameType(event.getNewGameMode());

        changedGameMode(player, from, to);
    }

    private void changedGameMode(ServerPlayer player, InventoryBackup.GameMode from, InventoryBackup.GameMode to) {

        InventoryBackup backup = new InventoryBackup(player, from);
        InventoryBackup restore = new InventoryBackup(player, to);
        String dim = WorldPosition.getDim(player.serverLevel());

        if(dim.equalsIgnoreCase(ModDimensions.BUILDER_DIM()))
        {
            if(to.equals(InventoryBackup.GameMode.Creative))
                return; // Don't do a double backup
        }
        restore.setFormerGameMode(from);

        restore.restore();
        backup.save();

        if(to.equals(InventoryBackup.GameMode.Creative))
        {
            player.getInventory().clearContent();
        } else if(to.equals(InventoryBackup.GameMode.Builder))
        {
            player.getInventory().clearContent();
        }
        restore.apply();
        restore.save(); // Save the former gamemode for possible restore!

        ChatHelpers.broadcastTo(player, ChatHelpers.macro(Messages.OTE_PREFIX + " !Dark_Green!Your inventory has been saved for [0], and your [1] inventory has been restored", from.getName(), to.getName()), player.server);
    }
}
