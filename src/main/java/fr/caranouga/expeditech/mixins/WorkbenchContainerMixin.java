package fr.caranouga.expeditech.mixins;

import fr.caranouga.expeditech.common.capability.techlevel.TechLevelUtils;
import fr.caranouga.expeditech.common.configs.ServerConfig;
import fr.caranouga.expeditech.common.registry.ModTechLockedItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(WorkbenchContainer.class)
public class WorkbenchContainerMixin {
    @Redirect(
            method = "slotChangedCraftingGrid",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;isPresent()Z"
            )
    )
    private static boolean redirectIsPresent(Optional<ICraftingRecipe> optional, int pContainerId, World pLevel,
                                             PlayerEntity pPlayer, CraftingInventory pContainer,
                                             CraftResultInventory pResultContainer){
        return optional.isPresent() && expeditech$hasCorrectTechXp(optional, pPlayer, pContainer, pLevel);
    }

    /*@Redirect(
            method = "slotChangedCraftingGrid",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;get()Ljava/lang/Object;"
            )
    )
    private static Object redirectGet(Optional<ICraftingRecipe> optional, int pContainerId, World pLevel,
                                      PlayerEntity pPlayer, CraftingInventory pContainer,
                                      CraftResultInventory pResultContainer){
        if(expeditech$hasCorrectTechXp(optional, pPlayer, pContainer, pLevel)){
            return new ICraftingRecipe() {
                @Override
                public boolean matches(CraftingInventory pInv, World pLevel) {
                    return true;
                }

                @Override
                public ItemStack assemble(CraftingInventory pInv) {
                    ItemStack blockedItem = new ItemStack(Items.BARRIER);
                    blockedItem.getOrCreateTag().putBoolean("is" + Expeditech.MODID + "BlockedItem", true);

                    return blockedItem;
                }

                @Override
                public boolean canCraftInDimensions(int pWidth, int pHeight) {
                    return true;
                }

                @Override
                public ItemStack getResultItem() {
                    return new ItemStack(Items.BARRIER);
                }

                @Override
                public ResourceLocation getId() {
                    return modLocation("blocked_recipe");
                }

                @Override
                public IRecipeSerializer<?> getSerializer() {
                    return null;
                }
            };
        }

        return optional.get();
    }*/

    @Unique
    private static boolean expeditech$hasCorrectTechXp(Optional<ICraftingRecipe> optional, PlayerEntity pPlayer, CraftingInventory pContainer, World pLevel){
        if(!ServerConfig.techLevelCraft.get()) return true;

        ICraftingRecipe iCraftingRecipe = optional.get();
        ItemStack stack = iCraftingRecipe.assemble(pContainer);


        if(ModTechLockedItems.isLocked(stack)){
            int currentXp = TechLevelUtils.getTechXp(pPlayer);
            return currentXp >= ModTechLockedItems.getRequiredXp(stack);
        }

        return true;
    }
}
