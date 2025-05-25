package fr.caranouga.expeditech.tiles.pipe.energy;

import fr.caranouga.expeditech.blocks.EnergyStorages;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.pipe.AbstractPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

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
        if (level == null || level.isClientSide) return;

        for (Direction direction : Direction.values()) {
            TileEntity neighborTile = level.getBlockEntity(worldPosition.relative(direction));
            if (neighborTile == null) {
                continue;
            }

            LazyOptional<IEnergyStorage> neighborCap = neighborTile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
            if (!neighborCap.isPresent()) {
                continue;
            }

            IEnergyStorage neighborStorage = neighborCap.orElse(null);
            IEnergyStorage pipeStorage = this.energyStorage;

            if (neighborStorage == null) {
                continue;
            }

            // Pull from neighbor to pipe
            if (neighborStorage.canExtract() && pipeStorage.canReceive()) {
                int extractable = neighborStorage.extractEnergy(getMaxExtract(neighborStorage), true);
                int received = pipeStorage.receiveEnergy(extractable, true);

                if (received > 0) {
                    neighborStorage.extractEnergy(received, false);
                    pipeStorage.receiveEnergy(received, false);
                }
            }

            // Push from pipe to neighbor
            if (pipeStorage.canExtract() && neighborStorage.canReceive()) {
                int extractable = pipeStorage.extractEnergy(getMaxExtract(pipeStorage), true);
                int receivable = neighborStorage.receiveEnergy(extractable, true);

                if (receivable > 0) {
                    pipeStorage.extractEnergy(receivable, false);
                    neighborStorage.receiveEnergy(receivable, false);
                }
            }
        }

        setChanged();
    }

    /*@Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        for (Direction direction : Direction.values()) {
            TileEntity neighborTile = level.getBlockEntity(worldPosition.relative(direction));
            if (neighborTile == null) continue;

            LazyOptional<IEnergyStorage> neighborCap = neighborTile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
            if (!neighborCap.isPresent()) continue;

            IEnergyStorage neighborStorage = neighborCap.orElseThrow(() -> new IllegalStateException("Neighbor energy storage is not present but should be! (" + direction + ")"));
            IEnergyStorage thisStorage = this.energyStorage;

            if(neighborStorage.canExtract() && thisStorage.canReceive()) {
                int extracted = neighborStorage.extractEnergy(getMaxExtract(neighborStorage), true);
                int received = thisStorage.receiveEnergy(extracted, true);

                neighborStorage.extractEnergy(received, false);
                thisStorage.receiveEnergy(received, false);
            }

            if(neighborStorage.canReceive() && thisStorage.canExtract()) {
                int extracted = thisStorage.extractEnergy(getMaxExtract(thisStorage), true);
                int received = neighborStorage.receiveEnergy(extracted, true);

                thisStorage.extractEnergy(received, false);
                neighborStorage.receiveEnergy(received, false);
            }
        }

        setChanged();
    }*/

    private int getMaxExtract(IEnergyStorage storage){
        if(storage instanceof CustomEnergyStorage) {
            return ((CustomEnergyStorage) storage).getMaxExtract();
        }
        return this.energyStorage.getMaxExtract();
    }
}
