package fr.caranouga.expeditech.common.compat.jei.sanding;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.blocks.EnergyStorages;
import fr.caranouga.expeditech.common.recipes.SandingRecipe;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.screens.widgets.jei.AbstractJeiAnimatableWidget;
import fr.caranouga.expeditech.common.screens.widgets.ProgressBarWidget;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;
import static net.minecraft.util.math.MathHelper.clamp;

public class SandingMachineRecipeCategory  implements IRecipeCategory<SandingRecipe> {
    public static final ResourceLocation UID = modLocation("sanding_machine");
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/jei/sanding_machine.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper helper;

    // Energy bar and progress bar depends on the recipe
    private final Map<SandingRecipe, AbstractJeiAnimatableWidget> energyBarCache = new HashMap<>();
    private final Map<SandingRecipe, AbstractJeiAnimatableWidget> progressBarCache = new HashMap<>();

    public SandingMachineRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 82, 52);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SANDING_MACHINE.get()));
        this.helper = helper;
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
        return new TranslationTextComponent("jei." + Expeditech.MODID + ".sanding_machine").getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("jei." + Expeditech.MODID + ".sanding_machine");
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
        recipeLayout.getItemStacks().init(0, true, 11, 17);
        recipeLayout.getItemStacks().init(1, false, 53, 17);

        recipeLayout.getItemStacks().set(ingredients);

        if (!energyBarCache.containsKey(recipe)) {
            int energyStored = clamp(recipe.getEnergyNeeded(), 0, EnergyStorages.SANDING_MACHINE.getCapacity());
            int maxEnergyStored = EnergyStorages.SANDING_MACHINE.getCapacity();
            float energyBarSize = (float) energyStored / (float) maxEnergyStored;

            AbstractJeiAnimatableWidget energyBar = new ProgressBarWidget(1, 45, 0xa02000)
                    .createStatic(helper, energyBarSize);
            energyBarCache.put(recipe, energyBar);
        }

        if (!progressBarCache.containsKey(recipe)) {
            AbstractJeiAnimatableWidget progressBar = new ProgressBarWidget(1, 1, 0x3da000)
                    .createAnimatedWithoutWidth(helper, ProgressBarWidget.WIDTH, recipe.getDuration());
            progressBarCache.put(recipe, progressBar);
        }
    }


    @Override
    public void draw(SandingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        AbstractJeiAnimatableWidget energyBar = energyBarCache.get(recipe);
        if (energyBar != null) {
            energyBar.renderStatic(matrixStack);
        }

        // Draw progress bar at top
        AbstractJeiAnimatableWidget progressBar = progressBarCache.get(recipe);
        if (progressBar != null) {
            progressBar.renderAnimated(matrixStack);
        }
    }

    @Override
    public List<ITextComponent> getTooltipStrings(SandingRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> tooltips = new ArrayList<>();

        if(mouseX >= 0 && mouseX <= 81 && mouseY >= 0 && mouseY <= 7) {
            int inSeconds = recipe.getDuration() / 20;
            tooltips.add(new TranslationTextComponent("jei." + Expeditech.MODID + ".sanding_machine.tooltips.progress", recipe.getDuration(), inSeconds));
        }
        if(mouseX >= 0 && mouseX <= 81 && mouseY >= 44 && mouseY <= 51) {
            tooltips.add(new TranslationTextComponent("jei." + Expeditech.MODID + ".sanding_machine.tooltips.energy", clamp(recipe.getEnergyNeeded(), 0, EnergyStorages.SANDING_MACHINE.getCapacity()), EnergyStorages.SANDING_MACHINE.getCapacity()));
        }

        return tooltips;
    }
}
