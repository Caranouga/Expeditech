package fr.caranouga.expeditech.common.compat.jei.coal_generator;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoalGeneratorFuelRecipe {
    private final List<ItemStack> inputs;
    private final int burnTime;

    public CoalGeneratorFuelRecipe(Collection<ItemStack> input, int burnTime) {
        this.inputs = new ArrayList<>(input);
        this.burnTime = burnTime;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public int getBurnTime() {
        return burnTime;
    }
}
