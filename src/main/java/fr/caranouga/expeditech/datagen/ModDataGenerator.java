package fr.caranouga.expeditech.datagen;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.datagen.providers.ModAdvancementsProvider;
import fr.caranouga.expeditech.datagen.providers.ModBlockLootTableProvider;
import fr.caranouga.expeditech.datagen.providers.ModItemModelsProvider;
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

        generator.addProvider(new ModItemModelsProvider(generator, existingFileHelper));
        generator.addProvider(new ModBlockLootTableProvider(generator));
        generator.addProvider(new ModAdvancementsProvider(generator, existingFileHelper));
    }

}
