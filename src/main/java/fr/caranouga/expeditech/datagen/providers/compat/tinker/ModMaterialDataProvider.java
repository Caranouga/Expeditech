package fr.caranouga.expeditech.datagen.providers.compat.tinker;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.tools.data.material.MaterialDataProvider;

public class ModMaterialDataProvider extends MaterialDataProvider {
    public ModMaterialDataProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        addMaterial(TinkerCompatMaterials.caranite, 2, ORDER_GENERAL, false, getColor("E20000"));
    }

    private int getColor(String hex){
        return Integer.parseInt(hex.replace("#", ""), 16);
    }

    private int getColor(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }
}
