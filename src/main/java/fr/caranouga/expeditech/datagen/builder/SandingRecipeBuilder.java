package fr.caranouga.expeditech.datagen.builder;

import com.google.gson.JsonObject;
import fr.caranouga.expeditech.registry.ModRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class SandingRecipeBuilder {
    private final Item result;
    private final int count;
    private Ingredient ingredient;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public SandingRecipeBuilder(IItemProvider pResult, int pCount) {
        this.result = pResult.asItem();
        this.count = pCount;
    }

    public static SandingRecipeBuilder sanding(IItemProvider pResult) {
        return new SandingRecipeBuilder(pResult, 1);
    }
    
    public static SandingRecipeBuilder sanding(IItemProvider pResult, int pCount) {
        return new SandingRecipeBuilder(pResult, pCount);
    }

    // Requires
    public SandingRecipeBuilder requires(ITag<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public SandingRecipeBuilder requires(IItemProvider pItem) {
        this.requires(Ingredient.of(pItem));
        return this;
    }

    public SandingRecipeBuilder requires(Ingredient pIngredient) {
        this.ingredient = pIngredient;
        return this;
    }

    // Unlocked by
    public SandingRecipeBuilder unlockedBy(String name, ICriterionInstance trigger) {
        this.advancement.addCriterion(name, trigger);
        return this;
    }

    // Save
    public void save(Consumer<IFinishedRecipe> finishedRecipeConsumer) {
        this.save(finishedRecipeConsumer, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> finishedRecipeConsumer, String path) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        if ((new ResourceLocation(path)).equals(resourcelocation)) {
            throw new IllegalStateException("Sanding Recipe " + path + " should remove its 'save' argument");
        } else {
            this.save(finishedRecipeConsumer, new ResourceLocation(path));
        }
    }

    public void save(Consumer<IFinishedRecipe> finishedRecipeConsumer, ResourceLocation location) {
        this.ensureValid(location);
        this.advancement.parent(
                new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
                .rewards(AdvancementRewards.Builder.recipe(location))
                .requirements(IRequirementsStrategy.OR);
        finishedRecipeConsumer.accept(new SandingRecipeBuilder.Result(
                location,
                this.result,
                this.count,
                this.ingredient,
                this.advancement,
                modLocation("recipes/" + location.getPath())
        ));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void ensureValid(ResourceLocation pId) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient for recipe " + pId);
        }
    }


    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final Ingredient ingredient;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation pId, Item pResult, int pCount, Ingredient pIngredient, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            this.id = pId;
            this.result = pResult;
            this.count = pCount;
            this.ingredient = pIngredient;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
        }

        public void serializeRecipeData(JsonObject pJson) {

            pJson.add("ingredient", ingredient.toJson());

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }

            pJson.add("result", jsonobject);
        }

        public IRecipeSerializer<?> getType() {
            return ModRecipes.SANDING_SERIALIZER.get();
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
