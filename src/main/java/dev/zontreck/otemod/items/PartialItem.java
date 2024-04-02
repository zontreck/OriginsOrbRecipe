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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartialItem extends Item
{
    private static final String TAG_UNCRAFT_REMAIN = "remaining";
    private static final String TAG_UNCRAFT_LIST = "Items";

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

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
            if(stack.getTag().contains(ItemStack.TAG_ENCH))
            {
                tooltip.add(ChatHelpers.macro("!Dark_Red!Number of Enchantments remaining: [0]", "!Yellow!" + stack.getTag().getList(ItemStack.TAG_ENCH, Tag.TAG_COMPOUND).size()));
            }
        } else {
            tooltip.add(ChatHelpers.macro("!Dark_Red!This partial item appears to be invalid, and contains no fragments."));
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

    public static ItemStack makePartialItem(ItemStack original, boolean enchantMode, boolean uncraftMode)
    {
        if(enchantMode)
        {
            ItemStack partial = new ItemStack(ModItems.PARTIAL_ITEM.get(), 1);
            CompoundTag tag = new CompoundTag();
            tag.put(ItemStack.TAG_ENCH, original.getTag().get(ItemStack.TAG_ENCH));

            partial.setTag(tag);

            return partial;
        } else return original;
    }
}
