package fr.caranouga.expeditech.blocks.pipes.energy;

import fr.caranouga.expeditech.blocks.pipes.AbstractPipeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

public class IronEnergyPipe extends AbstractPipeBlock {
    @Override
    protected TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world) {
        return null;
    }
}
