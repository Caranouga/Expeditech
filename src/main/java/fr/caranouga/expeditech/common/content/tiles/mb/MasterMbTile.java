package fr.caranouga.expeditech.common.content.tiles.mb;

import fr.caranouga.expeditech.common.multiblock.MultiblockShape;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class MasterMbTile extends AbstractMultiblockTile{
    public MasterMbTile() {
        super(ModTileEntities.MB_MASTER.get());
    }

    @Nonnull
    @Override
    protected MultiblockShape getShape() {
        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState thisState = ModBlocks.MB_MASTER.get().defaultBlockState();
        return new MultiblockShape(new BlockPos(-1, -1, 0),
                new BlockState[][] {
                        {stone, stone, stone},
                        {stone, stone, stone},
                        {stone, stone, stone}
                },
                new BlockState[][] {
                        {stone, thisState, stone},
                        {stone, stone, stone},
                        {stone, stone, stone}
                },
                new BlockState[][] {
                        {stone, stone, stone},
                        {stone, stone, stone},
                        {stone, stone, stone}
                });
    }

    @Override
    protected void formedTick() {
    }

    @Override
    protected void unformedTick() {
    }
}