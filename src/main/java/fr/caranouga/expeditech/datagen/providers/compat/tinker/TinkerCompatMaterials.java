package fr.caranouga.expeditech.datagen.providers.compat.tinker;

import fr.caranouga.expeditech.Expeditech;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public final class TinkerCompatMaterials {
    // tier 2
    public static final MaterialId caranite = id("caranite");

    /**
     * Creates a new material ID
     * @param name  ID name
     * @return  Material ID object
     */
    private static MaterialId id(String name) {
        return new MaterialId(Expeditech.MODID, name);
    }
}
