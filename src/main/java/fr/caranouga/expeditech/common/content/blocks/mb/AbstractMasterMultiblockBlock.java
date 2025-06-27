package fr.caranouga.expeditech.common.content.blocks.mb;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public abstract class AbstractMasterMultiblockBlock extends Block {
    public AbstractMasterMultiblockBlock() {
        super(AbstractBlock.Properties.of(Material.METAL));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return getTileEntityType(state, world).create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    protected abstract TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world);
}
