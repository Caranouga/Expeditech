package fr.caranouga.expeditech.tiles.pipe.energy;

import fr.caranouga.expeditech.blocks.EnergyStorages;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.pipe.AbstractPipeTile;
import net.minecraft.util.Direction;

public class IronEnergyPipeTile extends AbstractPipeTile {
    public IronEnergyPipeTile() {
        super(ModTileEntities.IRON_ENERGY_PIPE_TILE.get());
    }

    @Override
    protected CustomEnergyStorage createEnergyStorage() {
        return EnergyStorages.IRON_PIPE.createEnergyStorage();
    }




    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        ...

        setChanged();
    }

    private CustomEnergyStorage getEnergyStorage(Direction direction) {
        if (level == null) {
            return null;
        }

        return level.getBlockEntity(worldPosition.relative(direction), ModTileEntities.IRON_ENERGY_PIPE_TILE.get())
                .map(tile -> ((IronEnergyPipeTile) tile).energyStorage)
                .orElse(null);
    }
}
