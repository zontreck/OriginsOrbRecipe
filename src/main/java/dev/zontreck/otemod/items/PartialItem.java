package dev.zontreck.otemod.items;

import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PartialItem extends Item
{
    private static final String TAG_UNCRAFT_REMAIN = "remaining";
    private static final String TAG_UNCRAFT_LIST = "Items";


    public PartialItem() {
        super (new Properties().fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        tooltip.add(ChatHelpers.macro("!Yellow!This is a partially deconstructed item."));

        if(stack.getTag()!= null)
        {
            if(stack.getTag().contains(TAG_UNCRAFT_REMAIN))
            {
                tooltip.add(ChatHelpers.macro("!Dark_Red!Number of uncraft steps remaining: [0]", "!Yellow!" + stack.getTag().getInt(TAG_UNCRAFT_REMAIN)));
            }
        } else {
            tooltip.add(ChatHelpers.macro("!Dark_Red!This partial item appears to be invalid, and contains no item fragments."));
        }
    }

    public static List<Item> getRemainingIngredients(ItemStack stack)
    {
        List<Item> itx = new ArrayList<>();
        if(stack.getTag()!=null)
        {
            if(stack.getTag().contains(TAG_UNCRAFT_LIST))
            {
                ListTag lst = stack.getTag().getList(TAG_UNCRAFT_LIST, ListTag.TAG_STRING);

                for (Tag tag :
                        lst) {
                    StringTag st = (StringTag)tag;
                    itx.add(deserializeItemType(st.getAsString()));
                }

            }

        }

        return itx;
    }

    private static Item deserializeItemType(String item)
    {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
    }
}
