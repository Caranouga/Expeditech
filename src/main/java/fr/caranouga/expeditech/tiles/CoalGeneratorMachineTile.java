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
import net.minecraftforge.common.ForgeHooks;

public class CoalGeneratorMachineTile extends AbstractEnergyMachineTile implements ITickableTileEntity {
    private static final int INPUT_SLOT = 0;
    private static int ENERGY_PER_TICK = 1;

    private int burnTime = -1;
    private int currentBurnTime = 0;

    public CoalGeneratorMachineTile() {
        super(ModTileEntities.COAL_GENERATOR_TILE.get(), 10, 1);
    }

    @Override
    protected CustomEnergyStorage createEnergyStorage() {
        return EnergyStorages.COAL_GENERATOR.createEnergyStorage();
    }

    // region Data Saving


    @Override
    public void load(BlockState state, CompoundNBT nbt) {
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
        pCompound.putInt("burnTime", burnTime);
        pCompound.putInt("currentBurnTime", currentBurnTime);

        return super.save(pCompound);
    }

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