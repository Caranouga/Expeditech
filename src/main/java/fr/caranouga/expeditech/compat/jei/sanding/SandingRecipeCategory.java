package fr.caranouga.expeditech.compat.jei.sanding;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.recipes.SandingRecipe;
import fr.caranouga.expeditech.registry.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class SandingRecipeCategory implements IRecipeCategory<SandingRecipe> {
    public static final ResourceLocation UID = modLocation("sanding");
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/jei/sanding.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SandingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 76, 23);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.SANDING_PAPER.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SandingRecipe> getRecipeClass() {
        return SandingRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei." + Expeditech.MODID + ".sanding").getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("jei." + Expeditech.MODID + ".sanding");
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
    public void setIngredients(SandingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SandingRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 0, 3);
        recipeLayout.getItemStacks().init(1, false, 58, 3);

        /*recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));**/
        recipeLayout.getItemStacks().set(ingredients);
    }
}
