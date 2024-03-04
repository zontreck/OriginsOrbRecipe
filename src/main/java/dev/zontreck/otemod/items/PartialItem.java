package dev.zontreck.otemod.items;

import dev.zontreck.libzontreck.util.ChatHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartialItem extends Item
{
    private static final String TAG_UNCRAFT_REMAIN = "remaining";

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
}
