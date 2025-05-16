package fr.caranouga.expeditech.datagen.providers.compat.tinker;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.data.material.MaterialStatsDataProvider;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import static slimeknights.tconstruct.library.utils.HarvestLevels.IRON;

public class ModMaterialStatsProvider extends MaterialStatsDataProvider {
    public ModMaterialStatsProvider(DataGenerator gen, AbstractMaterialDataProvider materials) {
        super(gen, materials);
    }

    @Override
    protected void addMaterialStats() {
        addMaterialStats(TinkerCompatMaterials.caranite,
                new HeadMaterialStats(250, 6f, IRON, 2f),
                HandleMaterialStats.DEFAULT.withDurability(1.10f),
                ExtraMaterialStats.DEFAULT);
    }
}
