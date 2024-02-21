package dev.zontreck.otemod.mixins;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@Mixin(DyeColor.class)
public abstract class DyeColorMixin {
    @Shadow
    @Final
    @Mutable
    public static DyeColor[] $VALUES;
    private static final DyeColor DARK_RED = addVariant("DARK_RED", "dark_red", 128000001, MapColor.COLOR_BLACK, 128000001, 128000001);


    @Shadow
    @Final
    @Mutable
    private static final DyeColor[] BY_ID = Arrays.stream($VALUES).sorted(Comparator.comparingInt(DyeColor::getId)).toArray((p_41067_) -> {
        return new DyeColor[p_41067_];
    });

    @Shadow
    @Final
    @Mutable
    private static final Int2ObjectOpenHashMap<DyeColor> BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap<>(Arrays.stream($VALUES).collect(Collectors.toMap((p_41064_) -> {
        return p_41064_.getFireworkColor();
    }, (p_41056_) -> {
        return p_41056_;
    })));

    @Shadow @Final private int id;

    @Invoker("<init>")
    public static DyeColor invokeInit(String internalName, int internalId, int id, String p_41047_, int p_41048_, MapColor p_41049_, int p_41050_, int p_41051_) {
        throw new AssertionError();
    }

    private static DyeColor addVariant(String internalName, String p_41047_, int p_41048_, MapColor p_41049_, int p_41050_, int p_41051_) {
        ArrayList<DyeColor> variants = new ArrayList<DyeColor>(Arrays.asList(DyeColorMixin.$VALUES));
        DyeColor color = DyeColorMixin.invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, variants.get(variants.size() - 1).ordinal() + 1, p_41047_, p_41048_, p_41049_, p_41050_, p_41051_);
        variants.add(color);
        DyeColorMixin.$VALUES = variants.toArray(new DyeColor[0]);
        return color;
    }
}
