package fr.caranouga.expeditech.common.content.blocks.mb;

import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

public class MasterMbBlock extends AbstractMasterMultiblockBlock {
    @Override
    protected TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world) {
        return ModTileEntities.MB_MASTER.get();
    }
}