package fr.caranouga.expeditech.tiles.pipes;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.grid.EnergyGrid;
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.machines.AbstractEnergyMachineTile;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class AbstractEnergyPipeTile extends TileEntity implements ITickableTileEntity {
    public EnergyGrid grid;
    private boolean needsRebuild = true;

    public AbstractEnergyPipeTile() {
        super(ModTileEntities.ENERGY_PIPE.get());
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(grid == null || needsRebuild) {
            Expeditech.LOGGER.debug("Rebuilding energy grid for {}", getBlockPos());
            grid = EnergyGrid.rebuildFrom(this);
            needsRebuild = false;
        }else{
            Expeditech.LOGGER.debug("Ticking energy grid for {}", getBlockPos());
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



    public void markGridForRebuild() {
        needsRebuild = true;
    }

    public List<TileEntity> getAdjacentConnections(){
        List<TileEntity> connections = new ArrayList<>();
        if(level == null) return connections;

        for (Direction dir : Direction.values()) {
            BlockPos adjacentPos = getBlockPos().relative(dir);
            TileEntity neighbour = level.getBlockEntity(adjacentPos);
            if (neighbour != null) {
                connections.add(neighbour);
            }
        }
        return connections;
    }
}
