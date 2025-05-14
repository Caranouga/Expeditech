package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Expeditech.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.BLOCKS.getEntries().forEach(block -> simpleBlock(block.get()));

        this.createMachine(ModBlocks.COAL_GENERATOR.get());
    }

    private void createMachine(Block block) {
        // Create a furnace-like block (with side, front, on/off)

        getVariantBuilder(block)
                .forAllStates(state -> {
                    boolean on = state.getValue(BlockStateProperties.LIT);
                    ResourceLocation texture = modLoc("block/" + block.getRegistryName().getPath() + (on ? "_on" : "_off"));
                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(texture))
                            .build();
                });
    }
}
