package fr.caranouga.expeditech.common.grid;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyGrid extends AbstractGrid<IEnergyStorage> {
    @Override
    protected AbstractGrid<IEnergyStorage> getGrid() {
        return new EnergyGrid();
    }

    @Override
    protected Capability<IEnergyStorage> getCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    protected boolean isProducer(IEnergyStorage storage) {
        return storage.canExtract();
    }

    @Override
    protected boolean isConsumer(IEnergyStorage storage) {
        return storage.canReceive();
    }

    @Override
    protected int extractFrom(IEnergyStorage producer, int amount, boolean simulate) {
        return producer.extractEnergy(amount, simulate);
    }

    @Override
    protected int receiveTo(IEnergyStorage consumer, int amount) {
        return consumer.receiveEnergy(amount, false);
    }
}