package fr.caranouga.expeditech.blocks.pipes.energy;
/*
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.pipes.AbstractEnergyPipeTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IronEnergyPipe extends Block {
    public IronEnergyPipe() {
        super(AbstractBlock.Properties.of(Material.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.ENERGY_PIPE.get().create();
    }

    @Override
    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);

        if (!pLevel.isClientSide) {
            TileEntity te = pLevel.getBlockEntity(pPos);
            if (te instanceof AbstractEnergyPipeTile) {
                ((AbstractEnergyPipeTile) te).onNeighborChanged(pFromPos);
            }
        }
    }
}
*/

import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.pipes.AbstractPipeTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

public class IronEnergyPipe extends AbstractEnergyPipeBlock {
    @Override
    protected TileEntityType<? extends AbstractPipeTile<?>> getTileEntityType(BlockState state, IBlockReader world) {
        return ModTileEntities.IRON_ENERGY_PIPE_TILE.get();
    }
}