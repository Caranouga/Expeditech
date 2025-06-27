package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.recipes.SandingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class ModRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Expeditech.MODID);
    private static final HashMap<IRecipeType<?>, ResourceLocation> RECIPE_TYPES = new HashMap<>();

    // Serializers
    public static final RegistryObject<SandingRecipe.Serializer> SANDING_SERIALIZER = RECIPE_SERIALIZERS.register("sanding",
            SandingRecipe.Serializer::new);

    // Recipe Types
    public static IRecipeType<SandingRecipe> SANDING_RECIPE = registerRecipeType(new SandingRecipe.Type(), SandingRecipe.TYPE_ID);

    // region Utility methods
    public static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(IRecipeType<T> type, ResourceLocation id) {
        RECIPE_TYPES.put(type, id);
        return type;
    }

    public static void register(IEventBus eBus) {
        RECIPE_SERIALIZERS.register(eBus);
        RECIPE_TYPES.forEach((type, id) -> {
            Registry.register(Registry.RECIPE_TYPE, id, type);
        });
    }
}