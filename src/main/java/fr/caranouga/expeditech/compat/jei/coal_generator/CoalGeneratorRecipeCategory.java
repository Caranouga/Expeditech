package fr.caranouga.expeditech.compat.jei.coal_generator;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModBlocks;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class CoalGeneratorRecipeCategory implements IRecipeCategory<FurnaceRecipe> {
    public static final ResourceLocation UID = modLocation("coal_generator");
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/jei/coal_generator.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CoalGeneratorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 76, 23);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.COAL_GENERATOR.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends FurnaceRecipe> getRecipeClass() {
        return null;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei." + Expeditech.MODID + ".coal_generator").getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FurnaceRecipe recipe, IIngredients ingredients) {

    }

    @Override
    public void setIngredients(FurnaceRecipe recipe, IIngredients ingredients) {

    }
}
