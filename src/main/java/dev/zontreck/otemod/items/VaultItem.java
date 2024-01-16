package dev.zontreck.otemod.items;


import dev.zontreck.libzontreck.chat.ChatColor;
import dev.zontreck.libzontreck.profiles.Profile;
import dev.zontreck.libzontreck.profiles.UserProfileNotYetExistsException;
import dev.zontreck.libzontreck.util.ChatHelpers;
import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.chat.ChatServerOverride;
import dev.zontreck.otemod.configs.OTEServerConfig;
import dev.zontreck.otemod.events.LoreHandlers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.level.Level;

public class VaultItem extends Item
{

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public Item asItem()
    {
        return this;
    }


    public VaultItem(Properties pProperties) {
        super(pProperties);
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        OTEMod.LOGGER.info("Vault item is being used");
        if(!pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND)
        {
            ItemStack is = pPlayer.getItemInHand(pUsedHand);

            Profile p;
            try {
                p = Profile.get_profile_of(pPlayer.getStringUUID());
                if(OTEServerConfig.MAX_VAULTS.get()>0)
                {
                    if(p.available_vaults >= OTEServerConfig.MAX_VAULTS.get())
                    {
                        ChatHelpers.broadcastTo(pPlayer.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Red!You cannot activate anymore vaults due to the maximum set by the server admin"), OTEMod.THE_SERVER);
                        return InteractionResultHolder.pass(is);
                    } else {
                        p.available_vaults++;
                        p.commit();
                        ChatHelpers.broadcastTo(pPlayer.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Green!You now have "+String.valueOf(p.available_vaults)+" available vaults"), OTEMod.THE_SERVER);
                        pPlayer.setItemInHand(pUsedHand, new ItemStack(Items.AIR));
    
                        return InteractionResultHolder.consume(is);
                    }
                }else {
                    p.available_vaults++;
                    p.commit();
                    ChatHelpers.broadcastTo(pPlayer.getUUID(), ChatHelpers.macro(OTEMod.OTEPrefix+" !Dark_Green!You now have "+String.valueOf(p.available_vaults)+" available vaults"), OTEMod.THE_SERVER);
                    pPlayer.setItemInHand(pUsedHand, new ItemStack(Items.AIR));
    
                    return InteractionResultHolder.consume(is);
                }
            } catch (UserProfileNotYetExistsException e) {
                e.printStackTrace();
                return super.use(pLevel, pPlayer, pUsedHand);
            }
        }
        
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
