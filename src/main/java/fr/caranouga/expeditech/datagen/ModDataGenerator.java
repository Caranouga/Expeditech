package fr.caranouga.expeditech.datagen;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.datagen.providers.*;
import fr.caranouga.expeditech.datagen.providers.compat.tinker.ModMaterialDataProvider;
import fr.caranouga.expeditech.datagen.providers.compat.tinker.ModMaterialRecipeProvider;
import fr.caranouga.expeditech.datagen.providers.compat.tinker.ModMaterialStatsProvider;
import fr.caranouga.expeditech.datagen.providers.compat.tinker.ModMaterialTraitsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Expeditech.MODID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new ModBlockLootTableProvider(generator));
        generator.addProvider(new ModAdvancementsProvider(generator, existingFileHelper));
        generator.addProvider(new ModBlockModelProvider(generator, existingFileHelper));
        generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(new ModItemModelsProvider(generator, existingFileHelper));
        generator.addProvider(new ModLanguageProvider(generator));
        generator.addProvider(new ModRecipeProvider(generator));

        // Tinkers' Construct compatibility
        ModMaterialDataProvider materialDataProvider = new ModMaterialDataProvider(generator);
        generator.addProvider(materialDataProvider);
        generator.addProvider(new ModMaterialStatsProvider(generator, materialDataProvider));
        generator.addProvider(new ModMaterialTraitsProvider(generator, materialDataProvider));
        generator.addProvider(new ModMaterialRecipeProvider(generator));
    }

}
