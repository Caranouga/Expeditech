package fr.caranouga.expeditech.common.content.tiles.pipes.energy;
/*
import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.grid.EnergyGrid;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

public class IronEnergyPipeTile extends TileEntity implements ITickableTileEntity {
    public EnergyGrid grid;

    public IronEnergyPipeTile() {
        super(ModTileEntities.IRON_ENERGY_PIPE.get());
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(grid == null) {
            grid = EnergyGrid.rebuildFrom(this);
        }else{
            grid.tick();
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(grid != null) {
            grid.invalidate();
        }
    }

    public void onNeighborChanged(BlockPos changedPos) {
        // Optionally check if the block at changedPos has an energy capability:
        TileEntity neighbor = level.getBlockEntity(changedPos);
        for (Direction dir : Direction.values()) {
            if (neighbor != null && neighbor.getCapability(CapabilityEnergy.ENERGY, dir).isPresent()) {
                Expeditech.LOGGER.debug("Detected new energy-compatible neighbor at {}", changedPos);
                rebuildGrid();
                return;
            }
        }
    }

    public void rebuildGrid() {
        if (level == null || level.isClientSide) return;

        if (grid != null) {
            grid.invalidate();
        }

        grid = EnergyGrid.rebuildFrom(this);
    }
}
*/

import fr.caranouga.expeditech.common.registry.ModTileEntities;
import fr.caranouga.expeditech.common.content.tiles.IHasDurability;

public class IronEnergyPipeTile extends AbstractEnergyPipeTile implements IHasDurability {
    public IronEnergyPipeTile() {
        super(ModTileEntities.IRON_ENERGY_PIPE_TILE.get(), 1000);
    }

    @Override
    public int getMaxUses() {
        return 10;
    }
}