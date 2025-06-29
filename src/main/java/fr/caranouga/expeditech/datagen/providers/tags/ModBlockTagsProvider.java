package fr.caranouga.expeditech.datagen.providers.tags;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static net.minecraftforge.common.Tags.Blocks.ORES;
import static net.minecraftforge.common.Tags.Blocks.STORAGE_BLOCKS;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Expeditech.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        addTag(STORAGE_BLOCKS,
                ModBlocks.CARANITE_BLOCK.get(),
                ModBlocks.COPPER_BLOCK.get()
        );
        addTag(ORES,
                ModBlocks.CARANITE_ORE.get(),
                ModBlocks.COPPER_ORE.get()
        );
    }

    private void addTag(Tags.IOptionalNamedTag<Block> tag, Block... blocks) {
        this.tag(tag).add(blocks);
    }
}
