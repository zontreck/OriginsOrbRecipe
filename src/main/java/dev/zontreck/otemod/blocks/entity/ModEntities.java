package dev.zontreck.otemod.blocks.entity;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.ModBlocks;
import dev.zontreck.otemod.implementation.vault.VaultMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OTEMod.MOD_ID);

    public static final RegistryObject <BlockEntityType <ItemScrubberBlockEntity>> ITEM_SCRUBBER = ENTITIES.register("item_scrubber", ()-> BlockEntityType.Builder.of(ItemScrubberBlockEntity::new, ModBlocks.ITEM_SCRUBBER.get()).build(null));


    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}
