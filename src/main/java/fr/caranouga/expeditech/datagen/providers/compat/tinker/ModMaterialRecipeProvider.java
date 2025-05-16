package fr.caranouga.expeditech.datagen.providers.compat.tinker;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.crafting.Ingredient;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;

import java.util.function.Consumer;

public class ModMaterialRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper {
    public ModMaterialRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> finishedRecipeConsumer) {
        String folder = "tools/materials/";
        materialRecipe(finishedRecipeConsumer, TinkerCompatMaterials.caranite, Ingredient.of(ModItems.CARANITE.get()), 1, 1, folder + "caranite");
    }

    @Override
    public String getModId() {
        return Expeditech.MODID;
    }
}
