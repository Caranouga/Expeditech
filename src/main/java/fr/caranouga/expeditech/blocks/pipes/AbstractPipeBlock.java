package fr.caranouga.expeditech.blocks.pipes;

import fr.caranouga.expeditech.utils.VoxelUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public abstract class AbstractPipeBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");
    public static final BooleanProperty UP    = BooleanProperty.create("up");
    public static final BooleanProperty DOWN  = BooleanProperty.create("down");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AbstractPipeBlock() {
        /*super(AbstractBlock.Properties.of(Material.METAL)
                .strength(5.0F, 6.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops());*/
        super(AbstractBlock.Properties.of(Material.METAL));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false)
        );
    }

    // region BlockState
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        IWorld world = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();

        BlockState state = this.defaultBlockState();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            boolean canConnect = canConnectTo(world, neighborPos, direction);
            state = state.setValue(getProperty(direction), canConnect);
        }

        return state;
    }

    // endregion


    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if(pState.getValue(WATERLOGGED)){
            pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        boolean canConnect = canConnectTo(pLevel, pFacingPos, pFacing);
        return pState.setValue(getProperty(pFacing), canConnect);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return getTileEntityType(state, world).create();
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    /*@Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return getShape(state);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return getShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(state);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return getShape(state);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return getShape(state);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }*/

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return getShape(pState);
    }


    public static final VoxelShape SHAPE_NORTH = Block.box(5D, 5D, 5D, 11D, 11D, 0D);
    public static final VoxelShape SHAPE_SOUTH = Block.box(5D, 5D, 11D, 11D, 11D, 16D);
    public static final VoxelShape SHAPE_EAST = Block.box(11D, 5D, 5D, 16D, 11D, 11D);
    public static final VoxelShape SHAPE_WEST = Block.box(5D, 5D, 5D, 0D, 11D, 11D);
    public static final VoxelShape SHAPE_UP = Block.box(6.5d, 9.5d, 6.5d, 9.5d, 16d, 9.5d);
    public static final VoxelShape SHAPE_DOWN = Block.box(5D, 5D, 5D, 11D, 0D, 11D);
    public static final VoxelShape SHAPE_CORE = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);

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

    private boolean canConnectTo(IWorld world, BlockPos pos, Direction direction) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        return canConnect(tileEntity, world, pos, direction);
    }

    private BooleanProperty getProperty(Direction direction) {
        switch (direction) {
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST:  return EAST;
            case WEST:  return WEST;
            case UP:    return UP;
            case DOWN:  return DOWN;
            default:    throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }

    protected abstract TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world);
    protected abstract boolean canConnect(TileEntity tileEntity, IWorld world, BlockPos pos, Direction direction);
}
