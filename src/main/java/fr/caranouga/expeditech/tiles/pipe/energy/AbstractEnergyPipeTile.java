package fr.caranouga.expeditech.tiles.pipe.energy;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.tiles.pipe.AbstractPipeTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class AbstractEnergyPipeTile extends AbstractPipeTile<IEnergyStorage, CustomEnergyStorage> {
    public AbstractEnergyPipeTile(TileEntityType tileEntityType) {
        super(tileEntityType, "energy");
    }

    @Override
    protected Capability<IEnergyStorage> getCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    protected void neighborToPipeTransfer(IEnergyStorage neighborStorage, IEnergyStorage pipeStorage) {
        if (neighborStorage.canExtract() && pipeStorage.canReceive()) {
            int extractable = neighborStorage.extractEnergy(getMaxExtract(neighborStorage), true);
            int received = pipeStorage.receiveEnergy(extractable, true);

            if (received > 0) {
                Expeditech.LOGGER.debug("Transferring energy from neighbor to pipe: {} energy", received);
                neighborStorage.extractEnergy(received, false);
                pipeStorage.receiveEnergy(received, false);
            }
        }
    }

    @Override
    protected void pipeToNeighborTransfer(IEnergyStorage pipeStorage, IEnergyStorage neighborStorage) {
        if (pipeStorage.canExtract() && neighborStorage.canReceive()) {
            int extractable = pipeStorage.extractEnergy(getMaxExtract(pipeStorage), true);
            int receivable = neighborStorage.receiveEnergy(extractable, true);

            if (receivable > 0) {
                pipeStorage.extractEnergy(receivable, false);
                neighborStorage.receiveEnergy(receivable, false);
            }
        }
    }

    private int getMaxExtract(IEnergyStorage storage){
        if(storage instanceof CustomEnergyStorage) {
            return ((CustomEnergyStorage) storage).getMaxExtract();
        }
        return this.capabilityStorage.getMaxExtract();
    }
}
