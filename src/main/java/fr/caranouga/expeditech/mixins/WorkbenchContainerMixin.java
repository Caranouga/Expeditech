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
