package fr.caranouga.expeditech.datagen.providers.compat.tinker;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.tools.data.material.MaterialDataProvider;

public class ModMaterialDataProvider extends MaterialDataProvider {
    public ModMaterialDataProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        addMaterial(TinkerCompatMaterials.caranite, 2, ORDER_GENERAL, false, 0xE20000);
    }
}
