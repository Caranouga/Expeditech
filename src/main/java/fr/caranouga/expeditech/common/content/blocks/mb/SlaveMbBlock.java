package fr.caranouga.expeditech.common.content.blocks.mb;

import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SlaveMbBlock extends Block {
    public SlaveMbBlock() {
        super(
                Properties.of(Material.BARRIER)
                        .strength(-1.0F, 3600000.0F)
                        .noDrops()
                        .isValidSpawn(ModBlocks::never)
        );
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.MB_SLAVE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    // TODO: Find a way to make this work
    /*@Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return
    }*/

    /*@Override
    public int getHarvestLevel(BlockState state) {
        return super.getHarvestLevel(state);
    }*/

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK; // Prevents the block from being pushed by pistons
    }
}
