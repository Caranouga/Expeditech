package fr.caranouga.expeditech.common.content.tiles.machines;

import fr.caranouga.expeditech.common.content.blocks.EnergyStorages;
import fr.caranouga.expeditech.common.capability.energy.CustomEnergyStorage;
import fr.caranouga.expeditech.common.recipes.SandingRecipe;
import fr.caranouga.expeditech.common.registry.ModRecipes;
import fr.caranouga.expeditech.common.registry.ModTileEntities;
import fr.caranouga.expeditech.common.content.tiles.IHasDurability;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SandingMachineTile extends AbstractEnergyMachineTile implements ITickableTileEntity, IHasDurability {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private int polishingTime = 0;
    private int currentPolishingTime = 0;
    private ItemStack outputStack = ItemStack.EMPTY;

    public SandingMachineTile() {
        super(ModTileEntities.SANDING_MACHINE_TILE.get(), 2);
    }

    @Override
    protected ItemStackHandler createItemHandler(int size) {
        return new ItemStackHandler(size){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot){
                    case INPUT_SLOT:
                        // Input slot can only accept items that can be polished
                        Optional<SandingRecipe> recipe = getSandingRecipe(stack);
                        return recipe.isPresent() && !recipe.get().getResultItem().isEmpty();
                    case OUTPUT_SLOT:
                        // Output slot can accept any item
                        return true;
                    default:
                        return false; // Invalid slot
                }
            }
        };
    }

    @Override
    protected CustomEnergyStorage createEnergyStorage() {
        return EnergyStorages.SANDING_MACHINE.createEnergyStorage();
    }

    // region Data Saving
    // region Data Saving (World load/save)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("polishingTime")) {
            polishingTime = nbt.getInt("polishingTime");
        }
        if(nbt.contains("currentPolishingTime")) {
            currentPolishingTime = nbt.getInt("currentPolishingTime");
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        pCompound.putInt("polishingTime", polishingTime);
        pCompound.putInt("currentPolishingTime", currentPolishingTime);

        return super.save(pCompound);
    }
    // endregion
    // endregion

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(hasFinishedPolishing()){
            if(hasOutputed()) {
                if(isItemPolishable()) {
                    if(hasEnoughEnergy()) {
                        if(hasEnoughStorage()) {
                            setOutputStack();
                            setPolishingTimes();
                            pullEnergy();
                            pullItemFromInput();
                        }
                    }
                }
            }else{
                outputItem();
                clearPolishingTimes();
                clearOutputStack();
                setState(false);
            }
        }else{
            advancePolishing();
            setState(true);
        }

        setChanged();
    }

    private void pullEnergy() {
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        Optional<SandingRecipe> recipe = getSandingRecipe(inputStack);

        if(recipe.isPresent()) {
            int energyNeeded = recipe.get().getEnergyNeeded();
            energyStorage.removeEnergy(energyNeeded);
            setChanged();
        }
    }

    private void advancePolishing() {
        currentPolishingTime++;
    }

    private void clearOutputStack() {
        outputStack = ItemStack.EMPTY;
    }

    private void clearPolishingTimes() {
        polishingTime = 0;
        currentPolishingTime = 0;
    }

    private void pullItemFromInput() {
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        if(!inputStack.isEmpty()) {
            inputStack.shrink(1);
            if(inputStack.isEmpty()) {
                itemHandler.setStackInSlot(INPUT_SLOT, ItemStack.EMPTY);
            }
        }
    }

    private void outputItem() {
        ItemStack outputStack = itemHandler.getStackInSlot(OUTPUT_SLOT);
        ItemStack resultItem = this.outputStack.copy();

        if(outputStack.isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, resultItem);
        } else {
            outputStack.grow(resultItem.getCount());
        }
    }

    private void setPolishingTimes() {
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        Optional<SandingRecipe> recipe = getSandingRecipe(inputStack);

        polishingTime = recipe.map(SandingRecipe::getDuration).orElse(0);
        currentPolishingTime = 0;
    }

    private void setOutputStack() {
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        Optional<SandingRecipe> recipe = getSandingRecipe(inputStack);

        recipe.ifPresent(sandingRecipe -> outputStack = sandingRecipe.getResultItem().copy());
    }

    private boolean hasEnoughStorage() {
        ItemStack outputStack = itemHandler.getStackInSlot(OUTPUT_SLOT);
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        Optional<SandingRecipe> recipe = getSandingRecipe(inputStack);

        if(recipe.isPresent()) {
            ItemStack resultItem = recipe.get().getResultItem();
            return outputStack.isEmpty() || (outputStack.getCount() + resultItem.getCount() < outputStack.getMaxStackSize() && outputStack.sameItem(resultItem));
        }
        return false;
    }

    private boolean hasEnoughEnergy() {
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        Optional<SandingRecipe> recipe = getSandingRecipe(inputStack);

        return recipe.isPresent() && energyStorage.getEnergyStored() >= recipe.get().getEnergyNeeded();
    }

    private boolean isItemPolishable() {
        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        Optional<SandingRecipe> recipe = getSandingRecipe(inputStack);

        return recipe.isPresent() && !recipe.get().getResultItem().isEmpty();
    }

    private boolean hasOutputed() {
        return outputStack.isEmpty();
    }

    private boolean hasFinishedPolishing() {
        return currentPolishingTime >= polishingTime;
    }





    private void setState(boolean isBurning) {
        // We have already check if level is null
        BlockState state = level.getBlockState(getBlockPos());
        level.setBlock(getBlockPos(), state.setValue(BlockStateProperties.POWERED, isBurning), Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
    }





    private Optional<SandingRecipe> getSandingRecipe(ItemStack stack) {
        Inventory inv = new Inventory(1);
        inv.setItem(0, stack);

        return this.level.getRecipeManager().getRecipeFor(ModRecipes.SANDING_RECIPE, inv, this.level);
    }


    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbtTag = new CompoundNBT();
        //Write your data into the nbtTag

        nbtTag.putInt("polishingTime", polishingTime);

        return new SUpdateTileEntityPacket(this.worldPosition, -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        //Handle your Data

        if(tag.contains("polishingTime")) {
            polishingTime = tag.getInt("polishingTime");
        }
    }

    @Override
    public int getMaxUses() {
        return 10;
    }

    public int getPolishingTime() {
        return polishingTime;
    }

    public int getCurrentPolishingTime() {
        return currentPolishingTime;
    }

    public void setPolishingTime(int polishingTime) {
        this.polishingTime = polishingTime;
    }

    public void setCurrentPolishingTime(int currentPolishingTime) {
        this.currentPolishingTime = currentPolishingTime;
    }
}