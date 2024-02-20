package dev.zontreck.otemod.data;

import dev.zontreck.otemod.data.loot.ModBlockLootTablesProvider;
import dev.zontreck.otemod.data.loot.ModDeprecatedBlockLootTablesProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.Set;

public class ModLootTablesProvider
{
    public static LootTableProvider create(PackOutput output)
    {
        return new LootTableProvider(output, Set.of(), List.of(
            new LootTableProvider.SubProviderEntry(ModBlockLootTablesProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ModDeprecatedBlockLootTablesProvider::new, LootContextParamSets.BLOCK)
        ));
    }
}
