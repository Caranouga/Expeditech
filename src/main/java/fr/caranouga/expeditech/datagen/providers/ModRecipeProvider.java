package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.datagen.builder.SandingRecipeBuilder;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
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

        ShapelessRecipeBuilder.shapeless(ModItems.SANDING_PAPER.get(), 1)
                .requires(Tags.Items.SAND)
                .requires(Items.PAPER)
                .unlockedBy("has_paper", has(Items.PAPER))
                .unlockedBy("has_sand", has(Tags.Items.SAND))
                .save(finishedRecipeConsumer, modLocation("sanding_paper_from_sand_and_paper"));

        sanding(finishedRecipeConsumer, ModItems.IMPURE_CARANITE.get(), ModItems.CARANITE.get());

        ShapedRecipeBuilder.shaped(ModBlocks.IRON_ENERGY_PIPE.get(), 8)
                .pattern("III")
                .pattern(" R ")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_redstone", has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
                .save(finishedRecipeConsumer, modLocation("iron_energy_pipe"));
    }

    private void sanding(Consumer<IFinishedRecipe> finishedRecipeConsumer, Item input, Item output) {
        SandingRecipeBuilder.sanding(output)
                .requires(input)
                .unlockedBy("has_item", has(input))
                .save(finishedRecipeConsumer, modLocation("sanding_" + getName(output) + "_from_" + getName(input)));
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
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', nugget)
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
