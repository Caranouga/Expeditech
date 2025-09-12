package fr.caranouga.expeditech.common.content.blocks.pipes;

import fr.caranouga.expeditech.common.content.tiles.pipes.AbstractPipeTile;
import fr.caranouga.expeditech.common.utils.VoxelUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractPipeBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty NORTH       = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH       = BooleanProperty.create("south");
    public static final BooleanProperty EAST        = BooleanProperty.create("east");
    public static final BooleanProperty WEST        = BooleanProperty.create("west");
    public static final BooleanProperty UP          = BooleanProperty.create("up");
    public static final BooleanProperty DOWN        = BooleanProperty.create("down");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AbstractPipeBlock() {
        // TODO: Completer les properties du bloc
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

        if(canConnect || pState.getValue(getProperty(pFacing))){
            TileEntity tileEntity = pLevel.getBlockEntity(pCurrentPos);
            if (tileEntity instanceof AbstractPipeTile) {
                ((AbstractPipeTile<?>) tileEntity).onNeighborChanged();
            }
        }

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

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return getShape(pState);
    }

    @Override
    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);

        /*if (!pLevel.isClientSide) {
            TileEntity te = pLevel.getBlockEntity(pPos);
            if (te instanceof AbstractPipeTile<?>) {
                ((AbstractPipeTile<?>) te).onNeighborChanged(pFromPos);
            }
        }*/
    }

    private static final VoxelShape SHAPE_CORE = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
    private static final VoxelShape SHAPE_NORTH = Block.box(6.5, 6.5, 0, 9.5, 9.5, 6.5);
    private static final VoxelShape SHAPE_EAST = Block.box(9.5, 6.5, 6.5, 16, 9.5, 9.5);
    private static final VoxelShape SHAPE_SOUTH = Block.box(6.5, 6.5, 9.5, 9.5, 9.5, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 6.5, 6.5, 6.5, 9.5, 9.5);
    private static final VoxelShape SHAPE_UP = Block.box(6.5, 9.5, 6.5, 9.5, 16, 9.5);
    private static final VoxelShape SHAPE_DOWN = Block.box(6.5, 0, 6.5, 9.5, 6.5, 9.5);

    private VoxelShape getShape(BlockState state) {
        VoxelShape shape = SHAPE_CORE;

        //noinspection DuplicatedCode
        if (state.getValue(UP)) {
            shape = VoxelUtils.combine(shape, SHAPE_UP);
        }
        if (state.getValue(DOWN)) {
            shape = VoxelUtils.combine(shape, SHAPE_DOWN);
        }
        if (state.getValue(SOUTH)) {
            shape = VoxelUtils.combine(shape, SHAPE_SOUTH);
        }
        //noinspection DuplicatedCode
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


    /*
    // region BlockState
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return getState(pContext.getLevel(), pContext.getClickedPos());
    }

    private BlockState getState(World level, BlockPos clickedPos) {
        FluidState fluidState = level.getFluidState(clickedPos);
        return this.defaultBlockState()
                .setValue(DOWN, isConnected(level, clickedPos, Direction.DOWN))
                .setValue(UP, isConnected(level, clickedPos, Direction.UP))
                .setValue(NORTH, isConnected(level, clickedPos, Direction.NORTH))
                .setValue(SOUTH, isConnected(level, clickedPos, Direction.SOUTH))
                .setValue(WEST, isConnected(level, clickedPos, Direction.WEST))
                .setValue(EAST, isConnected(level, clickedPos, Direction.EAST))
                .setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if(pState.getValue(WATERLOGGED)){
            pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        boolean canConnect = canConnectTo(pLevel, pFacingPos, pFacing);
        return pState.setValue(getProperty(pFacing), canConnect);
    }

    private boolean canConnectTo(IWorld world, BlockPos pos, Direction direction) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        return canConnect(tileEntity, world, pos, direction);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }
    // endregion

    private boolean isConnected(World world, BlockPos pos, Direction direction) {
        return getConnectedDirections(world, pos).contains(direction);
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

    @Override
    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);

        if (!pLevel.isClientSide) {
            TileEntity te = pLevel.getBlockEntity(pPos);
            if (te instanceof AbstractPipeTile) {
                ((AbstractPipeTile<?>) te).onNeighborChanged(pFromPos);
            }
        }

        BlockState newState = getState(pLevel, pPos);
        if (!pState.getProperties().stream().allMatch(property -> pState.getValue(property).equals(newState.getValue(property)))) {
            pLevel.setBlockAndUpdate(pPos, newState);
        }
    }

    private static final VoxelShape SHAPE_CORE = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
    private static final VoxelShape SHAPE_NORTH = Block.box(6.5, 6.5, 0, 9.5, 9.5, 6.5);
    private static final VoxelShape SHAPE_EAST = Block.box(9.5, 6.5, 6.5, 16, 9.5, 9.5);
    private static final VoxelShape SHAPE_SOUTH = Block.box(6.5, 6.5, 9.5, 9.5, 9.5, 16);
    private static final VoxelShape SHAPE_WEST = Block.box(0, 6.5, 6.5, 6.5, 9.5, 9.5);
    private static final VoxelShape SHAPE_UP = Block.box(6.5, 9.5, 6.5, 9.5, 16, 9.5);
    private static final VoxelShape SHAPE_DOWN = Block.box(6.5, 0, 6.5, 9.5, 6.5, 9.5);
    private static final Map<BooleanProperty, VoxelShape> SHAPES = new HashMap<>();

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return getShape(pState);
    }

    private VoxelShape getShape(BlockState state) {
        VoxelShape shape = SHAPE_CORE;

        shape = getShapeFor(state, DOWN, shape);
        shape = getShapeFor(state, UP, shape);
        shape = getShapeFor(state, NORTH, shape);
        shape = getShapeFor(state, SOUTH, shape);
        shape = getShapeFor(state, WEST, shape);
        shape = getShapeFor(state, EAST, shape);

        return shape;
    }

    private VoxelShape getShapeFor(BlockState state, BooleanProperty prop, VoxelShape shape) {
        if (state.getValue(prop)) {
            return VoxelUtils.combine(SHAPES.get(prop), shape);
        }
        return shape;
    }

    private List<Direction> getConnectedDirections(World world, BlockPos pos) {
        List<Direction> connectedDirections = new ArrayList<>();

        for(Direction direction : Direction.values()) {
            if (isSideConnected(direction, world, pos)) {
                connectedDirections.add(direction);
            }
        }

        return connectedDirections;
    }

    static {
        SHAPES.put(DOWN, SHAPE_DOWN);
        SHAPES.put(UP, SHAPE_UP);
        SHAPES.put(NORTH, SHAPE_NORTH);
        SHAPES.put(SOUTH, SHAPE_SOUTH);
        SHAPES.put(WEST, SHAPE_WEST);
        SHAPES.put(EAST, SHAPE_EAST);

        DIRECTION_PROPERTIES.put(Direction.DOWN, DOWN);
        DIRECTION_PROPERTIES.put(Direction.UP, UP);
        DIRECTION_PROPERTIES.put(Direction.NORTH, NORTH);
        DIRECTION_PROPERTIES.put(Direction.SOUTH, SOUTH);
        DIRECTION_PROPERTIES.put(Direction.WEST, WEST);
        DIRECTION_PROPERTIES.put(Direction.EAST, EAST);
    }

    protected abstract TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world);
    public abstract boolean isSideConnected(Direction direction, World level, BlockPos pos);*/
}
