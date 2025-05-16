package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Expeditech.MODID, exFileHelper);
    }
    @Override
    protected void registerModels() {
        generateMachineBlock(ModBlocks.COAL_GENERATOR.get());
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
}
