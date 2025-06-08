package fr.caranouga.expeditech.tiles.pipes.energy;

import fr.caranouga.expeditech.grid.AbstractGrid;
import fr.caranouga.expeditech.grid.EnergyGrid;
import fr.caranouga.expeditech.tiles.pipes.AbstractPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEnergyPipeTile extends AbstractPipeTile<IEnergyStorage> {
    public AbstractEnergyPipeTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
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
