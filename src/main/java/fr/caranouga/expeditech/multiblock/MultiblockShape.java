package fr.caranouga.expeditech.multiblock;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MultiblockShape {
    private final BlockState[][][] layers;
    private final BlockPos masterRelative;


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
        /*switch (direction) {
            case NORTH: {
                BlockPos startPos = masterPos.offset(masterRelative);
                Expeditech.LOGGER.debug("startPos: {}", startPos);
                for (int y = 0; y < layers.length; y++) {
                    for (int z = 0; z < layers[0].length; z++) {
                        for (int x = 0; x < layers[0][0].length; x++) {
                            Expeditech.LOGGER.debug("Checking block at {}, {}, {} ({}, {}, {})",
                                    startPos.getX() + x, startPos.getY() + y, startPos.getZ() + z,
                                    x, y, z);

                            BlockPos pos = startPos.offset(x, y, z);
                            BlockState expectedState = layers[y][z][x];
                            BlockState actualState = world.getBlockState(pos);

                            world.setBlock(pos.offset(0, 10, 0), expectedState, 3); // For debugging, set the block at a higher position

                            if (!actualState.is(expectedState.getBlock())) {
                                Expeditech.LOGGER.debug("Block at {}, {}, {} does not match expected state: {} != {}",
                                        pos.getX(), pos.getY(), pos.getZ(),
                                        actualState, expectedState);
                                StringTextComponent message = new StringTextComponent("Expected " + expectedState + ", but found " + actualState);
                                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));
                                //return false;
                            }
                        }
                    }
                }

                break;
            }
            case WEST: {
                BlockPos startPos = masterPos.offset(masterRelative.getZ(), masterRelative.getY(), -masterRelative.getX());
                Expeditech.LOGGER.debug("startPos: {}", startPos);
                for (int y = 0; y < layers.length; y++) {
                    for (int z = 0; z < layers[0].length; z++) {
                        for (int x = 0; x < layers[0][0].length; x++) {
                            Expeditech.LOGGER.debug("Checking block at {}, {}, {} ({}, {}, {})",
                                    startPos.getX() + x, startPos.getY() + y, startPos.getZ() + z,
                                    x, y, z);

                            BlockPos pos = startPos.offset(z, y, -x);
                            BlockState expectedState = layers[y][z][x];
                            BlockState actualState = world.getBlockState(pos);

                            world.setBlock(pos.offset(0, 10, 0), expectedState, 3); // For debugging, set the block at a higher position

                            if (!actualState.is(expectedState.getBlock())) {
                                Expeditech.LOGGER.debug("Block at {}, {}, {} does not match expected state: {} != {}",
                                        pos.getX(), pos.getY(), pos.getZ(),
                                        actualState, expectedState);
                                StringTextComponent message = new StringTextComponent("Expected " + expectedState + ", but found " + actualState);
                                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));
                                //return false;
                            }
                        }
                    }
                }
            }
            case SOUTH: {
                BlockPos startPos = masterPos.offset(-masterRelative.getX(), masterRelative.getY(), -masterRelative.getZ());
                Expeditech.LOGGER.debug("startPos: {}", startPos);
                for (int y = 0; y < layers.length; y++) {
                    for (int z = 0; z < layers[0].length; z++) {
                        for (int x = 0; x < layers[0][0].length; x++) {
                            Expeditech.LOGGER.debug("Checking block at {}, {}, {} ({}, {}, {})",
                                    startPos.getX() + x, startPos.getY() + y, startPos.getZ() + z,
                                    x, y, z);

                            BlockPos pos = startPos.offset(-x, y, -z);
                            BlockState expectedState = layers[y][z][x];
                            BlockState actualState = world.getBlockState(pos);

                            world.setBlock(pos.offset(0, 10, 0), expectedState, 3); // For debugging, set the block at a higher position

                            if (!actualState.is(expectedState.getBlock())) {
                                Expeditech.LOGGER.debug("Block at {}, {}, {} does not match expected state: {} != {}",
                                        pos.getX(), pos.getY(), pos.getZ(),
                                        actualState, expectedState);
                                StringTextComponent message = new StringTextComponent("Expected " + expectedState + ", but found " + actualState);
                                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));
                                //return false;
                            }
                        }
                    }
                }

                break;
            }
            case EAST: {
                BlockPos startPos = masterPos.offset(-masterRelative.getZ(), masterRelative.getY(), masterRelative.getX());
                Expeditech.LOGGER.debug("startPos: {}", startPos);
                for (int y = 0; y < layers.length; y++) {
                    for (int z = 0; z < layers[0].length; z++) {
                        for (int x = 0; x < layers[0][0].length; x++) {
                            Expeditech.LOGGER.debug("Checking block at {}, {}, {} ({}, {}, {})",
                                    startPos.getX() + x, startPos.getY() + y, startPos.getZ() + z,
                                    x, y, z);

                            BlockPos pos = startPos.offset(-z, y, x);
                            BlockState expectedState = layers[y][z][x];
                            BlockState actualState = world.getBlockState(pos);

                            world.setBlock(pos.offset(0, 10, 0), expectedState, 3); // For debugging, set the block at a higher position

                            if (!actualState.is(expectedState.getBlock())) {
                                Expeditech.LOGGER.debug("Block at {}, {}, {} does not match expected state: {} != {}",
                                        pos.getX(), pos.getY(), pos.getZ(),
                                        actualState, expectedState);
                                StringTextComponent message = new StringTextComponent("Expected " + expectedState + ", but found " + actualState);
                                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));
                                //return false;
                            }
                        }
                    }
                }
            }
        }*/

        Map<BlockPos, ITextComponent> mismatches = new HashMap<>();

        BlockPos startPos = offset(masterPos, direction, masterRelative);
        //Expeditech.LOGGER.debug("startPos: {}", startPos);

        for (int y = 0; y < layers.length; y++) {
            for (int z = 0; z < layers[y].length; z++) {
                for (int x = 0; x < layers[y][z].length; x++) {
                    /*Expeditech.LOGGER.debug("Checking block at {}, {}, {} ({}, {}, {})",
                            startPos.getX() + x, startPos.getY() + y, startPos.getZ() + z,
                            x, y, z);*/

                    BlockPos pos = offset(startPos, direction, x, y, z);
                    BlockState expectedState = layers[y][z][x];
                    BlockState actualState = world.getBlockState(pos);

                    world.setBlock(pos.offset(0, 10, 0), expectedState, 3); // For debugging, set the block at a higher position

                    if (!actualState.is(expectedState.getBlock())) {
                        /*Expeditech.LOGGER.debug("Block at {}, {}, {} does not match expected state: {} != {}",
                                pos.getX(), pos.getY(), pos.getZ(),
                                actualState, expectedState);*/
                        TranslationTextComponent message = new TranslationTextComponent("mb." + Expeditech.MODID + ".error.at", expectedState, actualState);
                        mismatches.put(pos, message);
                    }
                }
            }
        }

        return mismatches;
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
