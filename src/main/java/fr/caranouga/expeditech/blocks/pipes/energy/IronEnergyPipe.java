package fr.caranouga.expeditech.blocks.pipes.energy;

import fr.caranouga.expeditech.blocks.pipes.AbstractPipeBlock;
import fr.caranouga.expeditech.tiles.pipe.old.energy.IronEnergyPipeTile;
import fr.caranouga.expeditech.utils.VoxelUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
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

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return getShape(pState);
    }


    public static final VoxelShape SHAPE_NORTH = Block.box(6.5, 6.5, 0, 9.5, 9.5, 6.5);
    public static final VoxelShape SHAPE_SOUTH = Block.box(6.5, 6.5, 9.5, 9.5, 9.5, 16);
    public static final VoxelShape SHAPE_EAST  = Block.box(9.5, 6.5, 6.5, 16, 9.5, 9.5);
    public static final VoxelShape SHAPE_WEST  = Block.box(0, 6.5, 6.5, 6.5, 9.5, 9.5);
    public static final VoxelShape SHAPE_UP    = Block.box(6.5, 9.5, 6.5, 9.5, 16, 9.5);
    public static final VoxelShape SHAPE_DOWN  = Block.box(6.5, 0, 6.5, 9.5, 6.5, 9.5);
    public static final VoxelShape SHAPE_CORE  = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);

    private VoxelShape getShape(BlockState state) {
        VoxelShape shape = SHAPE_CORE;

        if (state.getValue(UP)) {
            shape = VoxelUtils.combine(shape, SHAPE_UP);
        }
        if (state.getValue(DOWN)) {
            shape = VoxelUtils.combine(shape, SHAPE_DOWN);
        }
        if (state.getValue(SOUTH)) {
            shape = VoxelUtils.combine(shape, SHAPE_SOUTH);
        }
        if (state.getValue(NORTH)) {
            shape = VoxelUtils.combine(shape, SHAPE_NORTH);
        }
        if (state.getValue(EAST)) {
            shape = VoxelUtils.combine(shape, SHAPE_EAST);
        }
        if (state.getValue(WEST)) {
            shape = VoxelUtils.combine(shape, SHAPE_WEST);
        }

        return shape;
    }
}
