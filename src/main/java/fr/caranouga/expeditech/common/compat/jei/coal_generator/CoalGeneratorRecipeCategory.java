package fr.caranouga.expeditech.common.compat.jei.coal_generator;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.blocks.EnergyStorages;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.screens.widgets.jei.AbstractJeiAnimatableWidget;
import fr.caranouga.expeditech.common.screens.widgets.ProgressBarWidget;
import fr.caranouga.expeditech.common.content.tiles.machines.CoalGeneratorMachineTile;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;
import static net.minecraft.util.math.MathHelper.clamp;

public class CoalGeneratorRecipeCategory implements IRecipeCategory<CoalGeneratorFuelRecipe> {
    public static final ResourceLocation UID = modLocation("coal_generator");
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/jei/coal_generator.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;

    // Energy bar and progress bar depends on the recipe
    private final Map<CoalGeneratorFuelRecipe, AbstractJeiAnimatableWidget> energyBarCache = new HashMap<>();
    private final Map<CoalGeneratorFuelRecipe, AbstractJeiAnimatableWidget> progressBarCache = new HashMap<>();

    public CoalGeneratorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 82, 54);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.COAL_GENERATOR.get()));

        this.helper = guiHelper;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CoalGeneratorFuelRecipe> getRecipeClass() {
        return CoalGeneratorFuelRecipe.class;
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
    public void setRecipe(IRecipeLayout recipeLayout, CoalGeneratorFuelRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 32, 17);
        guiItemStacks.set(ingredients);

        if (!energyBarCache.containsKey(recipe)) {
            int energyStored = clamp(recipe.getBurnTime() * CoalGeneratorMachineTile.ENERGY_PER_TICK, 0, EnergyStorages.COAL_GENERATOR.getCapacity());
            int maxEnergyStored = EnergyStorages.COAL_GENERATOR.getCapacity();
            float energyBarSize = (float) energyStored / (float) maxEnergyStored;

            AbstractJeiAnimatableWidget energyBar = new ProgressBarWidget(1, 45, 0xa02000)
                    .createAnimated(helper, energyBarSize, recipe.getBurnTime());
            energyBarCache.put(recipe, energyBar);
        }

        if (!progressBarCache.containsKey(recipe)) {
            AbstractJeiAnimatableWidget progressBar = new ProgressBarWidget(1, 1, 0x3da000)
                    .createAnimatedWithoutWidth(helper, ProgressBarWidget.WIDTH, recipe.getBurnTime());
            progressBarCache.put(recipe, progressBar);
        }
    }

    @Override
    public void setIngredients(CoalGeneratorFuelRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputs());
    }

    @Override
    public void draw(CoalGeneratorFuelRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        AbstractJeiAnimatableWidget energyBar = energyBarCache.get(recipe);
        if (energyBar != null) {
            energyBar.renderAnimated(matrixStack);
        }

        // Draw progress bar at top
        AbstractJeiAnimatableWidget progressBar = progressBarCache.get(recipe);
        if (progressBar != null) {
            progressBar.renderAnimated(matrixStack);
        }
    }

    @Override
    public List<ITextComponent> getTooltipStrings(CoalGeneratorFuelRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> tooltips = new ArrayList<>();

        if(mouseX >= 0 && mouseX <= 81 && mouseY >= 0 && mouseY <= 7) {
            int inSeconds = recipe.getBurnTime() / 20;
            tooltips.add(new TranslationTextComponent("jei." + Expeditech.MODID + ".coal_generator.tooltips.progress", recipe.getBurnTime(), inSeconds));
        }
        if(mouseX >= 0 && mouseX <= 81 && mouseY >= 44 && mouseY <= 51) {
            tooltips.add(new TranslationTextComponent("jei." + Expeditech.MODID + ".coal_generator.tooltips.energy", clamp(recipe.getBurnTime() * CoalGeneratorMachineTile.ENERGY_PER_TICK, 0, EnergyStorages.COAL_GENERATOR.getCapacity()), EnergyStorages.COAL_GENERATOR.getCapacity()));
        }

        return tooltips;
    }
}
