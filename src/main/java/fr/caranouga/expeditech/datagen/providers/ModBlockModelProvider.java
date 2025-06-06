package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.machines.AbstractMachineBlock;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.utils.BlockStateType;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Expeditech.MODID, exFileHelper);
    }
    @Override
    protected void registerModels() {
        //generateMachineBlock(ModBlocks.COAL_GENERATOR.get());

        ModBlocks.BLOCKS_ENTRIES.forEach((block, entry) -> {
            BlockStateType blockStateType = entry.getBlockStateType();
            Block blockInstance = block.get();
            switch (blockStateType){
                case SKIP:
                case CUBE_ALL: {
                    break;
                }

                case MACHINE_BLOCK: {
                    if (blockInstance instanceof AbstractMachineBlock) {
                        generateMachineBlock(blockInstance);
                    } else {
                        throw new RuntimeException("Block " + blockInstance.getRegistryName() + " is not an instance of AbstractMachineBlock but is registered as a MACHINE_BLOCK");
                    }
                    break;
                }

                case PIPE_BLOCK: {
                    /*if (blockInstance instanceof AbstractPipeBlock) {
                        generatePipeBlock(blockInstance);
                    } else {
                        throw new RuntimeException("Block " + blockInstance.getRegistryName() + " is not an instance of AbstractMachineBlock but is registered as a PIPE_BLOCK");
                    }*/
                    break;
                }
                default: {
                    throw new RuntimeException("Unknown block state type: " + blockStateType);
                }
            }
        });
    }

    private void generateMachineBlock(Block block){
        String name = block.getRegistryName().getPath();
        withExistingParent(name, mcLoc("block/orientable"))
                .texture("front", modLocation("block/" + name + "_front"))
                .texture("side", modLocation("block/" + name + "_side"))
                .texture("top", modLocation("block/" + name + "_top"));

        withExistingParent(name + "_on", mcLoc("block/orientable"))
                .texture("front", modLocation("block/" + name + "_front_on"))
                .texture("side", modLocation("block/" + name + "_side"))
                .texture("top", modLocation("block/" + name + "_top"));
    }

    private void generatePipeBlock(Block block) {
        String name = block.getRegistryName().getPath();
        withExistingParent(name + "_core", modLocation("block/pipe_core"))
                .texture("0", modLocation("block/" + name + "_core"));
        withExistingParent(name + "_connection", modLocation("block/pipe_connection"))
                .texture("0", modLocation("block/" + name + "_connection"));
    }
}
