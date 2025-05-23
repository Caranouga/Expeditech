package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.machines.AbstractMachineBlock;
import fr.caranouga.expeditech.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Expeditech.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.BLOCKS.getEntries().forEach(block -> {
            // TODO: This is a temporary fix to avoid crashing when generating blockstates for non-machine blocks
            if(!(block.get() instanceof AbstractMachineBlock)) simpleBlock(block.get());
        });

        registerMachineBlock(ModBlocks.COAL_GENERATOR.get());
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
}
