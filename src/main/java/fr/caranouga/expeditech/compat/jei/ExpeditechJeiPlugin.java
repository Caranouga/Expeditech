package fr.caranouga.expeditech.compat.jei;

import fr.caranouga.expeditech.compat.jei.coal_generator.CoalGeneratorRecipeCategory;
import fr.caranouga.expeditech.compat.jei.sanding.SandingRecipeCategory;
import fr.caranouga.expeditech.recipes.SandingRecipe;
import fr.caranouga.expeditech.registry.ModItems;
import fr.caranouga.expeditech.registry.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

@JeiPlugin
public class ExpeditechJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return modLocation("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CoalGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<SandingRecipe> sandingRecipes = recipeManager.getAllRecipesFor(ModRecipes.SANDING_RECIPE).stream()
                        .filter(r -> r instanceof SandingRecipe).collect(Collectors.toList());
        registration.addRecipes(sandingRecipes, SandingRecipeCategory.UID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // No GUI handlers to register
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.SANDING_PAPER.get()), SandingRecipeCategory.UID);
    }
}
