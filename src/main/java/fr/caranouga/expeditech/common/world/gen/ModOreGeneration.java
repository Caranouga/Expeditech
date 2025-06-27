package fr.caranouga.expeditech.common.world.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.Dimension;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;

public class ModOreGeneration {
    public static void generateOres(final BiomeLoadingEvent e){
        spawnOreInAllBiomes(OreType.CARANITE, e, Dimension.OVERWORLD.toString());
    }

    private static OreFeatureConfig getOverworldFeatureConfig(OreType ore) {
        return new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());
    }

    private static OreFeatureConfig getNetherFeatureConfig(OreType ore) {
        return new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK,
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());
    }

    private static OreFeatureConfig getEndFeatureConfig(OreType ore) {
        return new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE),
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());
    }

    private static ConfiguredFeature<?, ?> makeOreFeature(OreType ore, String dimensionToSpawn) {
        OreFeatureConfig config = null;

        if(dimensionToSpawn.equals(Dimension.OVERWORLD.toString())) {
            config = getOverworldFeatureConfig(ore);
        } else if(dimensionToSpawn.equals(Dimension.NETHER.toString())) {
            config = getNetherFeatureConfig(ore);
        } else if(dimensionToSpawn.equals(Dimension.END.toString())) {
            config = getEndFeatureConfig(ore);
        }

        ConfiguredPlacement<TopSolidRangeConfig> placement = Placement.RANGE.configured(new TopSolidRangeConfig(ore.getMinHeight(), ore.getMinHeight(), ore.getMaxHeight()));

        return registerOreFeature(ore, config, placement);
    }

    private static void spawnOreInOverworldInGivenBiomes(OreType type, final BiomeLoadingEvent e, Biome... biomes){
        OreFeatureConfig config = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                type.getBlock().get().defaultBlockState(), type.getMaxVeinSize());

        ConfiguredPlacement<TopSolidRangeConfig> placement = Placement.RANGE.configured(new TopSolidRangeConfig(type.getMinHeight(), type.getMinHeight(), type.getMaxHeight()));

        ConfiguredFeature<?, ?> feature = registerOreFeature(type, config, placement);

        if(Arrays.stream(biomes).anyMatch(b -> b.getRegistryName().equals(e.getName()))){
            e.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
        }
    }

    private static void spawnOreInOverworldInAllBiomes(OreType type, final BiomeLoadingEvent e){
        OreFeatureConfig config = new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                type.getBlock().get().defaultBlockState(), type.getMaxVeinSize());

        ConfiguredPlacement<TopSolidRangeConfig> placement = Placement.RANGE.configured(new TopSolidRangeConfig(type.getMinHeight(), type.getMinHeight(), type.getMaxHeight()));

        ConfiguredFeature<?, ?> feature = registerOreFeature(type, config, placement);

        e.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
    }

    private static void spawnOreInSpecificBiome(OreType type, final BiomeLoadingEvent e, Biome biome, String dimension) {
        if (e.getName().toString().contains(biome.getRegistryName().toString())) {
            e.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, makeOreFeature(type, dimension));
        }
    }

    private static void spawnOreInAllBiomes(OreType type, final BiomeLoadingEvent e, String dimension) {
        e.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, makeOreFeature(type, dimension));
    }



    private static ConfiguredFeature<?, ?> registerOreFeature(OreType ore, OreFeatureConfig config, ConfiguredPlacement<?> placement) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ore.getBlock().get().getRegistryName(),
                Feature.ORE.configured(config).decorated(placement).squared().count(ore.getVeinsPerChunk()));
    }
}
