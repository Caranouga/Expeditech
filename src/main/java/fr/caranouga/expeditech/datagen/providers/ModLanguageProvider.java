package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModItems;
import fr.caranouga.expeditech.registry.ModTabs;
import net.minecraft.data.DataGenerator;

public class ModLanguageProvider extends CustomLanguageProvider {
    public ModLanguageProvider(DataGenerator gen) {
        super(gen, "en_us", "fr_fr");
    }

    @Override
    protected void addTranslations() {
        // en_us
        addItem(ModItems.CARANITE, "Caranite");
        addItem(ModItems.IMPURE_CARANITE, "Impure Caranite");
        addItem(ModItems.CARANITE_DUST, "Caranite Dust");
        addItem(ModItems.CARANITE_NUGGET, "Caranite Nugget");
        addItem(ModItems.SANDING_PAPER, "Sanding Paper");

        addBlock(ModBlocks.CARANITE_BLOCK, "Caranite Block");
        addBlock(ModBlocks.CARANITE_ORE, "Caranite Ore");
        addBlock(ModBlocks.COAL_GENERATOR, "Coal Generator");

        addItemGroup(ModTabs.EXPEDITECH, "Expeditech");

        addJeiCategory("sanding", "Sanding");
        addJeiCategory("coal_generator", "Coal Generator");

        addJeiTooltip("coal_generator", "progress", "Progress: %st (%ss)");
        addJeiTooltip("coal_generator", "energy", "Energy: %s/%s");

        // fr_fr
        switchLocale();
        addItem(ModItems.CARANITE, "Caranite");
        addItem(ModItems.IMPURE_CARANITE, "Caranite Impure");
        addItem(ModItems.CARANITE_DUST, "Poussière de Caranite");
        addItem(ModItems.CARANITE_NUGGET, "Pépite de Caranite");
        addItem(ModItems.SANDING_PAPER, "Papier de Ponçage");

        addBlock(ModBlocks.CARANITE_BLOCK, "Bloc de Caranite");
        addBlock(ModBlocks.CARANITE_ORE, "Minerai de Caranite");
        addBlock(ModBlocks.COAL_GENERATOR, "Générateur à Charbon");

        addItemGroup(ModTabs.EXPEDITECH, "Expeditech");

        addJeiCategory("sanding", "Ponçage");
        addJeiCategory("coal_generator", "Générateur à Charbon");

        addJeiTooltip("coal_generator", "progress", "Progression: %st (%ss)");
        addJeiTooltip("coal_generator", "energy", "Énergie: %s/%s");
    }
}
