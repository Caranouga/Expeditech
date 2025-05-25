package fr.caranouga.expeditech.blocks.pipes;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public abstract class AbstractPipeBlock extends Block {
    // Patch le fait que les pipes soient des blocs solides (ex: on voit a travers eux)

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");
    public static final BooleanProperty UP    = BooleanProperty.create("up");
    public static final BooleanProperty DOWN  = BooleanProperty.create("down");

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
                .setValue(DOWN, false));
    }

    // region BlockState
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
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

    private static final VoxelShape SHAPE_CORE = Block.box(6.5, 6.5, 6.5, 9.5, 9.5, 9.5);
    private static final VoxelShape SHAPE_NORTH = Block.box(6.5, 6.5, 5.5, 8.5, 8.5, 0);


    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
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
