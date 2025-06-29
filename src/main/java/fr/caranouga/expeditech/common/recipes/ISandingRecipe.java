package fr.caranouga.expeditech.common.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public interface ISandingRecipe extends IRecipe<Inventory> {
    ResourceLocation TYPE_ID = modLocation("sanding");

    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    default boolean isSpecial() {
        return true;
    }
}
