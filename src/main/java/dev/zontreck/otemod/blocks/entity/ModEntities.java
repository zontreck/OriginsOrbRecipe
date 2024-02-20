package dev.zontreck.otemod.blocks.entity;

import dev.zontreck.otemod.OTEMod;
import dev.zontreck.otemod.blocks.CompressionChamberBlock;
import dev.zontreck.otemod.blocks.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OTEMod.MOD_ID);

    public static final RegistryObject <BlockEntityType <ItemScrubberBlockEntity>> ITEM_SCRUBBER = ENTITIES.register("item_scrubber", ()-> BlockEntityType.Builder.of(ItemScrubberBlockEntity::new, ModBlocks.ITEM_SCRUBBER.get()).build(null));

    public static final RegistryObject <BlockEntityType <MagicalScrubberBlockEntity>> MAGICAL_SCRUBBER = ENTITIES.register("magical_scrubber", ()-> BlockEntityType.Builder.of(MagicalScrubberBlockEntity::new, ModBlocks.MAGICAL_SCRUBBER.get()).build(null));

    //public static final ResourceLocation PARALLAX_BLOCK = new ResourceLocation(OTEMod.MOD_ID, "parallax_block");

    public static final RegistryObject<BlockEntityType<CompressionChamberBlockEntity>> COMPRESSION_CHAMBER = ENTITIES.register("compression_chamber", ()->BlockEntityType.Builder.of(CompressionChamberBlockEntity::new, ModBlocks.COMPRESSION_CHAMBER.get()).build(null));

    //public static final RegistryObject <BlockEntityType <ParallaxWindowEntity>> PARALLAX_WINDOW_ENTITY = ENTITIES.register("parallax_window", ()->BlockEntityType.Builder.of(ParallaxWindowEntity::new, ModBlocks.LIMINAL_WINDOW.get()).build(null));


    public static void register(IEventBus eventBus)
    {
        ENTITIES.register(eventBus);
    }
}
