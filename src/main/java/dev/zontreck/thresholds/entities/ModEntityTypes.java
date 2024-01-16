package dev.zontreck.thresholds.entities;

import net.minecraftforge.eventbus.api.IEventBus;

public class ModEntityTypes {
    //public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OTEMod.MOD_ID);

    //public static final RegistryObject<EntityType<? extends PossumEntity>> POSSUM = ENTITIES.register("possum", ()-> EntityType.Builder.of(PossumEntity::new, MobCategory.CREATURE).sized(1.5f, 0.5f).build(new ResourceLocation(OTEMod.MOD_ID, "possum").toString()));


    
    public static void register(IEventBus bus){
        //ENTITIES.register(bus);
    }
}
