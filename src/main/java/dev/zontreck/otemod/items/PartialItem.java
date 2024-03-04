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
    int uncraftSteps = 0;

    public PartialItem() {
        super (new Properties().fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        tooltip.add(ChatHelpers.macro("!Yellow!This is a partially deconstructed item."));
        tooltip.add(ChatHelpers.macro("!Dark_Red!Steps remaining to uncraft: [0]", "!Yellow!" + uncraftSteps));
    }
}
