package fr.caranouga.expeditech.datagen.providers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.utils.LootTypeEntry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends LootTableProvider {
    public ModBlockLootTableProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((key, value) -> {
            LootTableManager.validate(validationtracker, key, value);
        });
    }

    public static class ModBlockLootTables extends BlockLootTables {
        @Override
        protected void addTables() {
            ModBlocks.BLOCKS_ENTRIES.forEach((block, entry) -> {
                Expeditech.LOGGER.debug("Adding loot table for block: {}, Entry: {}", block.getId(), entry);
                LootTypeEntry lootTypeEntry = entry.getLootType();
                switch (lootTypeEntry.getLootType()){
                    case DROP_SELF: {
                        dropSelf(block.get());
                        break;
                    }
                    case ORE_DROP: {
                        this.add(block.get(), blockLoot -> createOreDrop(block.get(), lootTypeEntry.getDrop().get()));
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unknown loot type: " + lootTypeEntry.getLootType());
                    }
                }
            });
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    .collect(Collectors.toList());
        }
    }
}
