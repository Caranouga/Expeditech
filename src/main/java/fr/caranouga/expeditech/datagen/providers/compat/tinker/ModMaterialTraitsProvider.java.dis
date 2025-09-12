package fr.caranouga.expeditech.datagen.providers.compat.tinker;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.data.material.MaterialStatsDataProvider;
import slimeknights.tconstruct.tools.data.material.MaterialTraitsDataProvider;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import static slimeknights.tconstruct.library.utils.HarvestLevels.IRON;

public class ModMaterialTraitsProvider extends MaterialTraitsDataProvider {
    public ModMaterialTraitsProvider(DataGenerator gen, AbstractMaterialDataProvider materials) {
        super(gen, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addDefaultTraits(TinkerCompatMaterials.caranite, TinkerModifiers.sturdy.get());
    }
}