package fr.caranouga.expeditech.blocks;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.tiles.AbstractMachineTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class AbstractMachineBlock extends Block {
    private final String id;

    protected static final DirectionProperty FACING = HorizontalBlock.FACING;

    public AbstractMachineBlock(String id) {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(5.0F, 6.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops());
        this.id = id;

        registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    // region BlockState
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
    // endregion

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
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if(!pLevel.isClientSide()) {
            TileEntity tileEntity = pLevel.getBlockEntity(pPos);

            if(tileEntity instanceof AbstractMachineTile){
                INamedContainerProvider containerProvider = createContainerProvider(pLevel, pPos);

                NetworkHooks.openGui((ServerPlayerEntity) pPlayer, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World pLevel, BlockPos pPos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen." + Expeditech.MODID + "." + id);
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return getContainer(i, pLevel, pPos, playerInventory, playerEntity);
            }
        };
    }

    protected abstract TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world);
    protected abstract Container getContainer(int i, World pLevel, BlockPos pPos, PlayerInventory playerInventory, PlayerEntity playerEntity);
}
