package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModItems;
import fr.caranouga.expeditech.common.registry.ModTabs;
import net.minecraft.data.DataGenerator;

public class ModLanguageProvider extends CustomLanguageProvider {
    public ModLanguageProvider(DataGenerator gen) {
        super(gen, "en_us", "fr_fr");
    }

    @Override
    protected void addTranslations() {
        // en_us
        // Caranite
        addItem(ModItems.CARANITE, "Caranite");
        addItem(ModItems.IMPURE_CARANITE, "Impure Caranite");
        addItem(ModItems.CARANITE_DUST, "Caranite Dust");
        addItem(ModItems.CARANITE_NUGGET, "Caranite Nugget");
        addItem(ModItems.CARANITE_COAL, "Caranite Coal");
        // Copper
        addItem(ModItems.COPPER_INGOT, "Copper Ingot");
        addItem(ModItems.COPPER_DUST, "Copper Dust");
        addItem(ModItems.COPPER_NUGGET, "Copper Nugget");
        // Other
        addItem(ModItems.WRENCH, "Wrench");
        addItem(ModItems.SANDING_PAPER, "Sanding Paper");

        // Caranite
        addBlock(ModBlocks.CARANITE_BLOCK, "Caranite Block");
        addBlock(ModBlocks.CARANITE_ORE, "Caranite Ore");
        // Copper
        addBlock(ModBlocks.COPPER_ORE, "Copper Ore");
        addBlock(ModBlocks.COPPER_BLOCK, "Copper Block");
        // Machines
        addBlock(ModBlocks.COAL_GENERATOR, "Coal Generator");
        addBlock(ModBlocks.IRON_ENERGY_PIPE, "Iron Energy Pipe");
        addBlock(ModBlocks.SANDING_MACHINE, "Sanding Machine");

        addItemGroup(ModTabs.EXPEDITECH, "Expeditech");

        addJeiCategory("sanding", "Sanding");
        addJeiCategory("coal_generator", "Coal Generator");

        addJeiTooltip("coal_generator", "progress", "Progress: %st (%ss)");
        addJeiTooltip("coal_generator", "energy", "Energy: %s/%s");

        addCommand("techlevel", "add.levels.success.single", "Added %s tech level to %s");
        addCommand("techlevel", "add.levels.success.multiple", "Added %s tech levels to %s players");
        addCommand("techlevel", "add.xp.success.single", "Added %s tech XP to %s");
        addCommand("techlevel", "add.xp.success.multiple", "Added %s tech XP to %s players");
        addCommand("techlevel", "set.levels.success.single", "Set %s tech level to %s");
        addCommand("techlevel", "set.levels.success.multiple", "Set %s tech levels to %s players");
        addCommand("techlevel", "set.xp.success.single", "Set %s tech XP to %s");
        addCommand("techlevel", "set.xp.success.multiple", "Set %s tech XP to %s players");
        addCommand("techlevel", "get.levels.success", "Tech levels for %s: %s");
        addCommand("techlevel", "get.xp.success", "Tech XP for %s: %s");

        addScreen("coal_generator", "Coal Generator");
        addScreen("sanding_machine", "Sanding Machine");

        addTooltip("coal_generator.progress", "Progress: %s/%s (%s%s)");
        addTooltip("coal_generator.energy", "Energy: %s/%s");
        addTooltip("sanding_machine.progress", "Progress: %s/%s (%s%s)");
        addTooltip("sanding_machine.energy", "Energy: %s/%s");

        addKeyCategory("main", "Expeditech");

        addKey("techlevel", "Tech Level Screen");

        addOther("machine_broken", "A %s has broken at %s, %s, %s");
        add("mb." + Expeditech.MODID + ".error.at", "Expected %s but found %s");

        // fr_fr
        switchLocale();

        // Caranite
        addItem(ModItems.CARANITE, "Caranite");
        addItem(ModItems.IMPURE_CARANITE, "Caranite Impure");
        addItem(ModItems.CARANITE_DUST, "Poussière de Caranite");
        addItem(ModItems.CARANITE_NUGGET, "Pépite de Caranite");
        addItem(ModItems.CARANITE_COAL, "Charbon en Caranite");
        // Copper
        addItem(ModItems.COPPER_INGOT, "Lingot de Cuivre");
        addItem(ModItems.COPPER_DUST, "Poussière de Cuivre");
        addItem(ModItems.COPPER_NUGGET, "Pépite de Cuivre");
        // Other
        addItem(ModItems.WRENCH, "Clé à Molette");
        addItem(ModItems.SANDING_PAPER, "Papier de Ponçage");

        // Caranite
        addBlock(ModBlocks.CARANITE_BLOCK, "Bloc de Caranite");
        addBlock(ModBlocks.CARANITE_ORE, "Minerai de Caranite");
        // Copper
        addBlock(ModBlocks.COPPER_ORE, "Minerai de Cuivre");
        addBlock(ModBlocks.COPPER_BLOCK, "Bloc de Cuivre");
        // Machines
        addBlock(ModBlocks.COAL_GENERATOR, "Générateur à Charbon");
        addBlock(ModBlocks.IRON_ENERGY_PIPE, "Tuyau d'Énergie en Fer");
        addBlock(ModBlocks.SANDING_MACHINE, "Machine de Ponçage");

        addItemGroup(ModTabs.EXPEDITECH, "Expeditech");

        addJeiCategory("sanding", "Ponçage");
        addJeiCategory("coal_generator", "Générateur à Charbon");

        addJeiTooltip("coal_generator", "progress", "Progression: %st (%ss)");
        addJeiTooltip("coal_generator", "energy", "Énergie: %s/%s");

        addCommand("techlevel", "add.levels.success.single", "Ajouté %s niveau technologique à %s");
        addCommand("techlevel", "add.levels.success.multiple", "Ajouté %s niveaux technologiques à %s joueurs");
        addCommand("techlevel", "add.xp.success.single", "Ajouté %s XP technologique à %s");
        addCommand("techlevel", "add.xp.success.multiple", "Ajouté %s XP technologique à %s joueurs");
        addCommand("techlevel", "set.levels.success.single", "Niveau technologique de %s défini à %s");
        addCommand("techlevel", "set.levels.success.multiple", "Niveaux technologiques de %s joueurs définis à %s");
        addCommand("techlevel", "set.xp.success.single", "XP technologique de %s défini à %s");
        addCommand("techlevel", "set.xp.success.multiple", "XP technologique de %s joueurs défini à %s");
        addCommand("techlevel", "get.levels.success", "Niveaux technologiques pour %s: %s");
        addCommand("techlevel", "get.xp.success", "XP technologique pour %s: %s");

        addScreen("coal_generator", "Générateur à Charbon");
        addScreen("sanding_machine", "Machine de Ponçage");

        addTooltip("coal_generator.progress", "Progression: %s/%s (%s%s)");
        addTooltip("coal_generator.energy", "Énergie: %s/%s");
        addTooltip("sanding_machine.progress", "Progression: %s/%s (%s%s)");
        addTooltip("sanding_machine.energy", "Énergie: %s/%s");

        addKeyCategory("main", "Expeditech");

        addKey("techlevel", "Écran de Niveau Technologique");

        addOther("machine_broken", "Un %s s'est cassé en %s, %s, %s");
        add("mb." + Expeditech.MODID + ".error.at", "Attendu %s mais trouvé %s");
    }
}
