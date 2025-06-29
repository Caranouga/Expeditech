package fr.caranouga.expeditech.datagen.providers.tags;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.registry.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static net.minecraftforge.common.Tags.Items.*;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, pBlockTagsProvider, Expeditech.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        addTag(GEMS,
                ModItems.CARANITE.get()
        );
        addTag(INGOTS,
                ModItems.COPPER_INGOT.get()
        );
        addTag(NUGGETS,
                ModItems.CARANITE_NUGGET.get(),
                ModItems.COPPER_NUGGET.get()
        );

        this.copy(Tags.Blocks.STORAGE_BLOCKS, STORAGE_BLOCKS);
        this.copy(Tags.Blocks.ORES, ORES);
    }

    private void addTag(Tags.IOptionalNamedTag<Item> tag, Item... items) {
        this.tag(tag).add(items);
    }
}
