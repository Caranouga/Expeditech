package fr.caranouga.expeditech.common.content.tiles.mb;
/*
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SlaveMbTile extends TileEntity {
    private MasterMbTile masterTile;

    public SlaveMbTile() {
        super(ModTileEntities.MB_SLAVE.get());
    }

    public void setMaster(BlockPos masterPos, World masterWorld) {
        TileEntity tile = masterWorld.getBlockEntity(masterPos);
        if (tile instanceof MasterMbTile) {
            this.masterTile = (MasterMbTile) tile;
        } else {
            throw new IllegalArgumentException("The provided master position does not contain a MasterMbTile.");
        }
    }

    public void broken(){
        if (masterTile != null) {
            masterTile.slaveBroken(this);
        }
    }

    @Override
    public void setRemoved() {
        broken();
        super.setRemoved();
    }
}
*/

import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SlaveMbTile extends TileEntity {
    private AbstractMultiblockTile masterTile;

    public SlaveMbTile() {
        super(ModTileEntities.MB_SLAVE.get());
    }

    public void setMaster(BlockPos masterPos, World masterWorld) {
        TileEntity tile = masterWorld.getBlockEntity(masterPos);
        if (tile instanceof AbstractMultiblockTile) {
            this.masterTile = (AbstractMultiblockTile) tile;
        } else {
            throw new IllegalArgumentException("The provided master position does not contain a MasterMbTile.");
        }
    }

    public void broken(){
        if (masterTile != null) {
            masterTile.slaveBroken(this);
        }
    }

    @Override
    public void setRemoved() {
        broken();
        super.setRemoved();
    }
}