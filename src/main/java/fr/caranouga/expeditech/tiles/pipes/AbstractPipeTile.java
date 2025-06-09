package fr.caranouga.expeditech.tiles.pipes;

import fr.caranouga.expeditech.grid.AbstractGrid;
import fr.caranouga.expeditech.tiles.ETTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;

public abstract class AbstractPipeTile<C> extends ETTileEntity implements ITickableTileEntity {
    private AbstractGrid<C> grid;
    private boolean isTickLeader = false;

    public AbstractPipeTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType, 10);
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(grid == null) {
            grid = getGrid().rebuildFrom(this);
        }

        if (isTickLeader()) {
            if(grid.tick()) use();
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (grid != null && isTickLeader()) {
            grid.invalidate(); // This will invalidate the whole grid and clear tickLeader
        }
    }

    public void onNeighborChanged() {
        rebuildGrid();
    }

    public void rebuildGrid() {
        if (level == null || level.isClientSide) return;

        if (grid != null) {
            grid.invalidate();
        }

        grid = getGrid().rebuildFrom(this);
    }

    public void setGrid(AbstractGrid<C> grid) {
        this.grid = grid;
    }

    public boolean isTickLeader() {
        return isTickLeader;
    }

    public void setTickLeader(boolean tickLeader) {
        this.isTickLeader = tickLeader;
    }

    protected abstract AbstractGrid<C> getGrid();
    protected abstract Capability<C> getCapability();
}
