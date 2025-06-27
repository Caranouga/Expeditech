package fr.caranouga.expeditech.common.content.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nullable;

public class FuelItem extends Item {
    private final int burnTime;

    public FuelItem(Item.Properties tab, int burnTime) {
        super(tab);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable IRecipeType<?> recipeType) {
        return this.burnTime;
    }
}
