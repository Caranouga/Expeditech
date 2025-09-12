package fr.caranouga.expeditech.common.content.blocks.mb;

import fr.caranouga.expeditech.common.content.tiles.mb.SlaveMbTile;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SlaveMbBlock extends Block {
    public SlaveMbBlock() {
        super(
                // TODO: When the two functions below are implemented, .strength can be removed
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

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        Block originalBlock = getOriginalBlock(world, pos);
        if (originalBlock != null) return originalBlock.getPickBlock(state, target, world, pos, player);

        return super.getPickBlock(state, target, world, pos, player);
    }

    @Nullable
    private SlaveMbTile getSlaveTile(IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof SlaveMbTile) {
            return (SlaveMbTile) tile;
        }
        return null;
    }

    @Nullable
    private Block getOriginalBlock(IBlockReader world, BlockPos pos){
        SlaveMbTile tile = getSlaveTile(world, pos);
        if(tile != null){
            BlockState state = tile.getOriginalBlockState();
            if(state != null) return state.getBlock();
            return null;
        }

        return null;
    }

    /*
    // TODO: Find a way to make this work
    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return super.getHarvestTool(state);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return super.getHarvestLevel(state);
    }
    */

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK; // Prevents the block from being pushed by pistons
    }
}
