package fr.caranouga.expeditech.blocks;

import fr.caranouga.expeditech.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.CoalGeneratorMachineTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CoalGeneratorMachine extends AbstractMachineBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CoalGeneratorMachine() {
        super("coal_generator");

        // Set the default state of the block
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH));
    }

    // region BlockState
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, FACING);
    }
    // endregion

    @Override
    protected TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world) {
        return ModTileEntities.COAL_GENERATOR_TILE.get();
    }

    @Override
    protected Container getContainer(int i, World pLevel, BlockPos pPos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CoalGeneratorContainer(i, pLevel, pPos, playerInventory, playerEntity);
    }

    @Override
    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()){
            TileEntity tile = pLevel.getBlockEntity(pPos);
            if(tile instanceof CoalGeneratorMachineTile){
                ((CoalGeneratorMachineTile) tile).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
}