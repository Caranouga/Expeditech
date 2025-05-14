package fr.caranouga.expeditech.tiles;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.EnergyStorages;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CoalGeneratorMachineTile extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler itemHandler = createItemHandler();
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    private final CustomEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<CustomEnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> energyStorage);

    private int burnTime = -1;
    private int currentBurnTime = 0;

    private static int INPUT_SLOT = 0;
    private static int ENERGY_PER_TICK = 1;

    public CoalGeneratorMachineTile() {
        super(ModTileEntities.COAL_GENERATOR_TILE.get());
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return isItemBurnable(stack);
            }
        };
    }
    private CustomEnergyStorage createEnergyStorage(){
        return EnergyStorages.COAL_GENERATOR.createEnergyStorage();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return lazyItemHandler.cast();
        }
        if(cap == CapabilityEnergy.ENERGY){
            return lazyEnergyStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    // region Data Saving
    // On world load and save

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("inv")) {
            itemHandler.deserializeNBT(nbt.getCompound("inv"));
        }
        if(nbt.contains("energy")) {
            energyStorage.deserializeNBT(nbt.getCompound("energy"));
        }

        if(nbt.contains("burnTime")) {
            burnTime = nbt.getInt("burnTime");
        }
        if(nbt.contains("currentBurnTime")) {
            currentBurnTime = nbt.getInt("currentBurnTime");
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        pCompound.put("inv", itemHandler.serializeNBT());
        pCompound.put("energy", energyStorage.serializeNBT());

        pCompound.putInt("burnTime", burnTime);
        pCompound.putInt("currentBurnTime", currentBurnTime);

        return super.save(pCompound);
    }
    // endregion

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(energyStorage.isFull()) return;

        Expeditech.LOGGER.debug("Energy: {}/{}", energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored());

        if(hasFinishedBurning()) {
            if(isItemBurnable(itemHandler.getStackInSlot(INPUT_SLOT))){
                consumeFuel();
            }else{
                currentBurnTime = 0;
                burnTime = 0;
            }
        }else{
            currentBurnTime++;
            energyStorage.receiveEnergy(ENERGY_PER_TICK, false);
        }
    }

    private void consumeFuel() {
        ItemStack fuel = itemHandler.getStackInSlot(INPUT_SLOT);
        int burnTime = getItemBurnTime(fuel);

        fuel.shrink(1);
        currentBurnTime = 0;
        this.burnTime = burnTime;
        setChanged();
    }

    private boolean hasFinishedBurning() {
        return currentBurnTime >= burnTime;
    }

    private boolean isItemBurnable(ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }

    private int getItemBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING);
    }
}
