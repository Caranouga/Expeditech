package fr.caranouga.expeditech.blocks.pipes.energy;

import fr.caranouga.expeditech.blocks.pipes.AbstractPipeBlock;
import fr.caranouga.expeditech.tiles.pipe.energy.IronEnergyPipeTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.energy.CapabilityEnergy;

public class IronEnergyPipe extends AbstractPipeBlock {
    @Override
    protected TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world) {
        return new IronEnergyPipeTile().getType();
    }

    @Override
    protected boolean canConnect(TileEntity tileEntity, IWorld world, BlockPos pos, Direction direction) {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).isPresent();
    }
}
