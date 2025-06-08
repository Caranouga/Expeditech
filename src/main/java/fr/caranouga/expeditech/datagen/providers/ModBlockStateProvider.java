package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.machines.AbstractMachineBlock;
import fr.caranouga.expeditech.blocks.pipes.AbstractPipeBlock;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.utils.BlockStateType;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Expeditech.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.BLOCKS_ENTRIES.forEach((block, entry) -> {
            BlockStateType blockStateType = entry.getBlockStateType();
            Block blockInstance = block.get();
            switch (blockStateType){
                case SKIP: {
                    break;
                }
                case CUBE_ALL: {
                    simpleBlock(blockInstance);
                    break;
                }

                case MACHINE_BLOCK: {
                    if (blockInstance instanceof AbstractMachineBlock) {
                        registerMachineBlock(blockInstance);
                    } else {
                        throw new RuntimeException("Block " + blockInstance.getRegistryName() + " is not an instance of AbstractMachineBlock but is registered as a MACHINE_BLOCK");
                    }
                    break;
                }

                case PIPE_BLOCK: {
                    if (blockInstance instanceof AbstractPipeBlock) {
                        registerPipeBlock(blockInstance);
                    } else {
                        throw new RuntimeException("Block " + blockInstance.getRegistryName() + " is not an instance of AbstractMachineBlock but is registered as a PIPE_BLOCK");
                    }
                    break;
                }
                default: {
                    throw new RuntimeException("Unknown block state type: " + blockStateType);
                }
            }
        });
    }

    private void registerMachineBlock(Block block) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    boolean powered = state.getValue(BlockStateProperties.POWERED);
                    int rotationY;
                    if (direction == Direction.NORTH) {
                        rotationY = 0;
                    } else if (direction == Direction.EAST) {
                        rotationY = 90;
                    } else if (direction == Direction.SOUTH) {
                        rotationY = 180;
                    } else if (direction == Direction.WEST) {
                        rotationY = 270;
                    } else {
                        rotationY = 0;
                    }

                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLocation("block/" + block.getRegistryName().getPath() + (powered ? "_on" : ""))))
                            .rotationY(rotationY)
                            .build();
                });
    }

    private void registerPipeBlock(Block block){
        ModelFile core = models().getExistingFile(modLocation("block/" + block.getRegistryName().getPath() + "_core"));
        ModelFile part = models().getExistingFile(modLocation("block/" + block.getRegistryName().getPath() + "_connection"));

        /**
         * {
         *   "multipart": [
         *     {
         *       "apply": { "model": "et:block/pipe_core" }
         *     },
         *     {
         *       "when": { "north": "true" },
         *       "apply": { "model": "et:block/pipe_connection", "y": 0 }
         *     },
         *     {
         *       "when": { "south": "true" },
         *       "apply": { "model": "et:block/pipe_connection", "y": 180 }
         *     },
         *     {
         *       "when": { "west": "true" },
         *       "apply": { "model": "et:block/pipe_connection", "y": 270 }
         *     },
         *     {
         *       "when": { "east": "true" },
         *       "apply": { "model": "et:block/pipe_connection", "y": 90 }
         *     },
         *     {
         *       "when": { "up": "true" },
         *       "apply": { "model": "et:block/pipe_connection", "x": 270 }
         *     },
         *     {
         *       "when": { "down": "true" },
         *       "apply": { "model": "et:block/pipe_connection", "x": 90 }
         *     }
         *   ]
         * }
         */


        getMultipartBuilder(block)
                .part().modelFile(core).addModel().end()
                .part().modelFile(part).rotationY(0).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(part).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(part).rotationY(270).addModel().condition(BlockStateProperties.WEST, true).end()
                .part().modelFile(part).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(part).rotationX(270).addModel().condition(BlockStateProperties.UP, true).end()
                .part().modelFile(part).rotationX(90).addModel().condition(BlockStateProperties.DOWN, true).end();
    }
}
