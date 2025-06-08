package fr.caranouga.expeditech.tiles.pipes;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.grid.AbstractGrid;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public abstract class AbstractPipeTile<C> extends TileEntity implements ITickableTileEntity {
    private AbstractGrid<C> grid;

    public AbstractPipeTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(grid == null) {
            grid = getGrid().rebuildFrom(this);
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
        /*TileEntity neighbor = level.getBlockEntity(changedPos);
        for (Direction dir : Direction.values()) {
            if (neighbor != null && neighbor.getCapability(getCapability(), dir).isPresent()) {
                rebuildGrid();
                return;
            }
        }*/
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

    protected abstract AbstractGrid<C> getGrid();
    protected abstract Capability<C> getCapability();
}
