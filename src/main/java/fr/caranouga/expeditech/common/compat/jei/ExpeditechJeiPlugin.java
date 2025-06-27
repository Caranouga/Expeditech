package fr.caranouga.expeditech.common.compat.jei;

import fr.caranouga.expeditech.common.compat.jei.coal_generator.CoalGeneratorFuelRecipe;
import fr.caranouga.expeditech.common.compat.jei.coal_generator.CoalGeneratorRecipeCategory;
import fr.caranouga.expeditech.common.compat.jei.sanding.SandingMachineRecipeCategory;
import fr.caranouga.expeditech.common.compat.jei.sanding.SandingRecipeCategory;
import fr.caranouga.expeditech.common.content.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.common.content.containers.SandingMachineContainer;
import fr.caranouga.expeditech.common.recipes.SandingRecipe;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModItems;
import fr.caranouga.expeditech.common.registry.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;
import static net.minecraftforge.common.ForgeHooks.getBurnTime;

@JeiPlugin
public class ExpeditechJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return modLocation("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SandingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SandingMachineRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new CoalGeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<SandingRecipe> sandingRecipes = recipeManager.getAllRecipesFor(ModRecipes.SANDING_RECIPE).stream()
                        .filter(r -> r instanceof SandingRecipe).collect(Collectors.toList());
        registration.addRecipes(sandingRecipes, SandingRecipeCategory.UID);
        registration.addRecipes(sandingRecipes, SandingMachineRecipeCategory.UID);

        Collection<ItemStack> allItemStacks = registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM);
        List<CoalGeneratorFuelRecipe> fuelRecipes = new ArrayList<>();
        for (ItemStack stack : allItemStacks) {
            int burnTime = getBurnTime(stack, IRecipeType.SMELTING);
            if (burnTime > 0) {
                fuelRecipes.add(new CoalGeneratorFuelRecipe(Collections.singleton(stack), burnTime));
            }
        }

        registration.addRecipes(fuelRecipes, CoalGeneratorRecipeCategory.UID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // No GUI handlers to register
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.SANDING_PAPER.get()), SandingRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SANDING_MACHINE.get()), SandingMachineRecipeCategory.UID);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COAL_GENERATOR.get()), CoalGeneratorRecipeCategory.UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        addRecipeTransferHandler(registration, SandingMachineRecipeCategory.UID, SandingMachineContainer.class, 0, 1);
        addRecipeTransferHandler(registration, CoalGeneratorRecipeCategory.UID, CoalGeneratorContainer.class, 0, 1);
    }

    private <C extends Container> void addRecipeTransferHandler(IRecipeTransferRegistration registration, ResourceLocation recipeCategoryUid, Class<C> containerClass, int recipeStartIndex, int recipeSlotsCount) {
        int vanillaInventorySlots = 36; // Vanilla inventory slots
        registration.addRecipeTransferHandler(containerClass, recipeCategoryUid, vanillaInventorySlots + recipeStartIndex, recipeSlotsCount, 0, vanillaInventorySlots);
    }
}
