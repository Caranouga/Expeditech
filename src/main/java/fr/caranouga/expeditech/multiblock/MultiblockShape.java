package fr.caranouga.expeditech.multiblock;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MultiblockShape {
    private final BlockState[][][] layers;
    private final BlockPos masterRelative;

    public static final Direction[] DIRECTIONS = new Direction[]{
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    public MultiblockShape(BlockPos masterRelative, BlockState[][]... layers) {
        this.masterRelative = masterRelative;
        this.layers = layers;
    }

    /**
     * Tests if the multiblock structure matches the expected block states in the world.
     *
     * @param direction  The direction in which the multiblock is oriented.
     * @param world      The world in which to check the block states.
     * @param masterPos  The position of the master block of the multiblock structure.
     * @return           Returns a Map of BlockPos where mismatches were found.
     */
    public Map<BlockPos, ITextComponent> test(Direction direction, World world, BlockPos masterPos) {
        Map<BlockPos, ITextComponent> mismatches = new HashMap<>();

        BlockPos startPos = offset(masterPos, direction, masterRelative);

        for (int y = 0; y < layers.length; y++) {
            for (int z = 0; z < layers[y].length; z++) {
                for (int x = 0; x < layers[y][z].length; x++) {
                    BlockPos pos = offset(startPos, direction, x, y, z);
                    BlockState expectedState = layers[y][z][x];
                    BlockState actualState = world.getBlockState(pos);

                    if (!actualState.is(expectedState.getBlock())) {
                        TranslationTextComponent message = new TranslationTextComponent("mb." + Expeditech.MODID + ".error.at", expectedState.getBlock().getName(), actualState.getBlock().getName());
                        mismatches.put(pos, message);
                    }
                }
            }
        }

        return mismatches;
    }

    public boolean isMasterAtGoodPos(BlockPos testPos){
        for(Direction dir : DIRECTIONS){
            BlockPos offsetPos = offset(testPos, dir, masterRelative);
            if(offsetPos.equals(testPos)) return true;
        }

        return false;
    }

    private BlockPos offset(BlockPos pos, Direction direction, int xOffset, int yOffset, int zOffset) {
        switch (direction){
            case NORTH: {
                return pos.offset(xOffset, yOffset, zOffset);
            }
            case SOUTH: {
                return pos.offset(-xOffset, yOffset, -zOffset);
            }
            case WEST: {
                return pos.offset(zOffset, yOffset, -xOffset);
            }
            case EAST: {
                return pos.offset(-zOffset, yOffset, xOffset);
            }
            default: {
                Expeditech.LOGGER.error("Invalid direction for offset: {}", direction);
                return pos; // Return the original position if the direction is invalid
            }
        }
    }

    private BlockPos offset(BlockPos pos, Direction direction, BlockPos offset) {
        return offset(pos, direction, offset.getX(), offset.getY(), offset.getZ());
    }
}
