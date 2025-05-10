package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Consumer;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> finishedRecipeConsumer) {
        storageBlock(finishedRecipeConsumer, ModBlocks.CARANITE_BLOCK.get(), ModItems.CARANITE.get());
        nuggets(finishedRecipeConsumer, ModItems.CARANITE.get(), ModItems.CARANITE_NUGGET.get());
    }

    private void storageBlock(Consumer<IFinishedRecipe> finishedRecipeConsumer, Block block, Item item) {
        ShapedRecipeBuilder.shaped(block)
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', item)
                .unlockedBy("has_item", has(item))
                .save(finishedRecipeConsumer, modLocation(getName(block) + "_from_" + getName(item)));
        ShapelessRecipeBuilder.shapeless(item, 9)
                .requires(block)
                .unlockedBy("has_block", has(block))
                .save(finishedRecipeConsumer, modLocation(getName(item) + "_from_" + getName(block)));
    }

    private void nuggets(Consumer<IFinishedRecipe> finishedRecipeConsumer, Item item, Item nugget) {
        ShapedRecipeBuilder.shaped(item)
                .pattern("X")
                .pattern("X")
                .pattern("X")
                .define('X', item)
                .unlockedBy("has_item", has(item))
                .save(finishedRecipeConsumer, modLocation(getName(item) + "_from_" + getName(nugget)));
        ShapelessRecipeBuilder.shapeless(nugget, 9)
                .requires(item)
                .unlockedBy("has_item", has(item))
                .save(finishedRecipeConsumer, modLocation(getName(nugget) + "_from_" + getName(item)));
    }

    private String getName(ForgeRegistryEntry<?> entry) {
        return entry.getRegistryName().getPath();
    }
}
