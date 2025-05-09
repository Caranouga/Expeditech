package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(DataGenerator generator, ExistingFileHelper exFileHelper) {
        super(generator, Expeditech.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(entry -> {
            if(entry.get() instanceof BlockItem){
                withExistingParent(entry.get().getRegistryName().getPath(), modLoc("block/" + entry.get().getRegistryName().getPath()));

                return;
            }

            ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
            builder(itemGenerated, entry.get().getRegistryName().getPath());
        });
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
