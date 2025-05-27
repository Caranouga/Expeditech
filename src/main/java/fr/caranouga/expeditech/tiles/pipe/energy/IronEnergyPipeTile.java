package fr.caranouga.expeditech.tiles.pipe.energy;

import fr.caranouga.expeditech.blocks.EnergyStorages;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModTileEntities;

public class IronEnergyPipeTile extends AbstractEnergyPipeTile {
    public IronEnergyPipeTile() {
        super(ModTileEntities.IRON_ENERGY_PIPE_TILE.get());
    }

    @Override
    protected CustomEnergyStorage createCapabilityStorage() {
        return EnergyStorages.IRON_PIPE.createEnergyStorage();
    }
}
