package fr.caranouga.expeditech.common.content.tiles.pipes.energy;

import fr.caranouga.expeditech.common.grid.AbstractGrid;
import fr.caranouga.expeditech.common.grid.EnergyGrid;
import fr.caranouga.expeditech.common.content.tiles.pipes.AbstractPipeTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class AbstractEnergyPipeTile extends AbstractPipeTile<IEnergyStorage> {
    public AbstractEnergyPipeTile(TileEntityType<?> tileEntityType, int maxTransfer) {
        super(tileEntityType, maxTransfer);
    }

    @Override
    protected AbstractGrid<IEnergyStorage> getGrid() {
        return new EnergyGrid();
    }

    @Override
    protected Capability<IEnergyStorage> getCapability() {
        return CapabilityEnergy.ENERGY;
    }
}
