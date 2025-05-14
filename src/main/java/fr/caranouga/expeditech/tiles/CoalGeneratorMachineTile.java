package fr.caranouga.expeditech.tiles;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CoalGeneratorMachineTile extends TileEntity implements ITickableTileEntity {
    public CoalGeneratorMachineTile() {
        super(ModTileEntities.COAL_GENERATOR_TILE.get());
    }

    @Override
    public void tick() {
        Expeditech.LOGGER.debug("GeneratorMachineTile tick");
    }
}
