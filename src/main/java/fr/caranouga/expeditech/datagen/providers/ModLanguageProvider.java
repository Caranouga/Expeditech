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
        addItem(ModItems.CARANITE_COAL, "Caranite Coal");

        addBlock(ModBlocks.CARANITE_BLOCK, "Caranite Block");
        addBlock(ModBlocks.CARANITE_ORE, "Caranite Ore");
        addBlock(ModBlocks.COAL_GENERATOR, "Coal Generator");
        addBlock(ModBlocks.IRON_ENERGY_PIPE, "Iron Energy Pipe");

        addItemGroup(ModTabs.EXPEDITECH, "Expeditech");

        addJeiCategory("sanding", "Sanding");
        addJeiCategory("coal_generator", "Coal Generator");

        addJeiTooltip("coal_generator", "progress", "Progress: %st (%ss)");
        addJeiTooltip("coal_generator", "energy", "Energy: %s/%s");

        addCommand("techlevel", "add.levels.success.single", "Added %s tech level to %s");
        addCommand("techlevel", "add.levels.success.multiple", "Added %s tech levels to %s players");
        addCommand("techlevel", "add.xp.success.single", "Added %s tech XP to %s");
        addCommand("techlevel", "add.xp.success.multiple", "Added %s tech XP to %s players");

        addScreen("coal_generator", "Coal Generator");

        addTooltip("coal_generator.progress", "Progress: %s/%s (%s%s)");
        addTooltip("coal_generator.energy", "Energy: %s/%s");

        addKeyCategory("test", "Test Category");

        addKey("test", "Test Key");

        // fr_fr
        switchLocale();
        addItem(ModItems.CARANITE, "Caranite");
        addItem(ModItems.IMPURE_CARANITE, "Caranite Impure");
        addItem(ModItems.CARANITE_DUST, "Poussière de Caranite");
        addItem(ModItems.CARANITE_NUGGET, "Pépite de Caranite");
        addItem(ModItems.SANDING_PAPER, "Papier de Ponçage");
        addItem(ModItems.CARANITE_COAL, "Charbon en Caranite");

        addBlock(ModBlocks.CARANITE_BLOCK, "Bloc de Caranite");
        addBlock(ModBlocks.CARANITE_ORE, "Minerai de Caranite");
        addBlock(ModBlocks.COAL_GENERATOR, "Générateur à Charbon");
        addBlock(ModBlocks.IRON_ENERGY_PIPE, "Câble électrique en fer");

        addItemGroup(ModTabs.EXPEDITECH, "Expeditech");

        addJeiCategory("sanding", "Ponçage");
        addJeiCategory("coal_generator", "Générateur à Charbon");

        addJeiTooltip("coal_generator", "progress", "Progression: %st (%ss)");
        addJeiTooltip("coal_generator", "energy", "Énergie: %s/%s");

        addCommand("techlevel", "add.levels.success.single", "Ajouté %s niveau technologique à %s");
        addCommand("techlevel", "add.levels.success.multiple", "Ajouté %s niveaux technologiques à %s joueurs");
        addCommand("techlevel", "add.xp.success.single", "Ajouté %s XP technologique à %s");
        addCommand("techlevel", "add.xp.success.multiple", "Ajouté %s XP technologique à %s joueurs");

        addScreen("coal_generator", "Générateur à Charbon");

        addTooltip("coal_generator.progress", "Progression: %s/%s (%s%s)");
        addTooltip("coal_generator.energy", "Énergie: %s/%s");

        addKeyCategory("test", "Test Category");

        addKey("test", "Test Key");
    }
}
