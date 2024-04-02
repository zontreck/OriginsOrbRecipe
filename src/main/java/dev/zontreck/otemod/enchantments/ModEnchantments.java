package dev.zontreck.otemod.enchantments;

import dev.zontreck.otemod.OTEMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

    protected static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final DeferredRegister<Enchantment> REGISTERS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, OTEMod.MOD_ID);

    public static final RegistryObject<Enchantment> MOB_EGGING_ENCHANTMENT = REGISTERS.register("mob_egging", ()->new MobEggEnchantment());

    public static final RegistryObject<Enchantment> FLIGHT_ENCHANTMENT = REGISTERS.register("player_flight", ()->new FlightEnchantment(EquipmentSlot.FEET));


    public static final RegistryObject<Enchantment> CONSUMPTION_MENDING = REGISTERS.register("consumption_mending", ()->new ConsumptionMending(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));



    public static final RegistryObject<Enchantment> NIGHT_VISION_ENCHANT = REGISTERS.register("night_vision", ()->new NightVisionEnchantment(EquipmentSlot.HEAD));
    public static final RegistryObject<Enchantment> WATER_BREATHING_ENCHANT = REGISTERS.register("water_breathing", ()->new NightVisionEnchantment(EquipmentSlot.HEAD));

    public static void register(IEventBus bus){
        REGISTERS.register(bus);
    }
}
