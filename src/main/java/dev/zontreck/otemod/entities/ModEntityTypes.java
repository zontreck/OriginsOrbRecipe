package dev.zontreck.otemod.entities;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.entities.monsters.PossumEntity;
import dev.zontreck.otemod.items.ThrownMobCaptureBall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OTEMod.MOD_ID);

    //public static final RegistryObject<EntityType<? extends PossumEntity>> POSSUM = ENTITIES.register("possum", ()-> EntityType.Builder.of(PossumEntity::new, MobCategory.CREATURE).sized(1.5f, 0.5f).build(new ResourceLocation(OTEMod.MOD_ID, "possum").toString()));


    private static EntityType registerEntity(EntityType.Builder builder, String entityName) {
        return builder.build(entityName);
    }
    public static void register(IEventBus bus){
        ENTITIES.register(bus);
    }
}
