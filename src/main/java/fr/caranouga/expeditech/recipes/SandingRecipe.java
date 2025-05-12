package fr.caranouga.expeditech.recipes;

import com.google.gson.JsonObject;
import fr.caranouga.expeditech.registry.ModRecipes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SandingRecipe implements ISandingRecipe {
    private final ResourceLocation id;
    private final ItemStack result;
    private final Ingredient ingredient;

    public SandingRecipe(ResourceLocation id, ItemStack result, Ingredient ingredient) {
        this.id = id;
        this.result = result;
        this.ingredient = ingredient;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, ingredient);
    }

    @Override
    public boolean matches(Inventory pInv, World pLevel) {
        return ingredient.test(pInv.getItem(0));
    }

    @Override
    public ItemStack assemble(Inventory pInv) {
        return result.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.SANDING_SERIALIZER.get();
    }

    public static class Type implements IRecipeType<SandingRecipe> {
        @Override
        public String toString() {
            return SandingRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SandingRecipe> {

        @Override
        public SandingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(pJson, "result"));
            Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(pJson,  "ingredient"));

            return new SandingRecipe(pRecipeId, result, ingredient);
        }

        @Nullable
        @Override
        public SandingRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();

            return new SandingRecipe(pRecipeId, result, ingredient);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, SandingRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);
        }
    }
}
