package fr.caranouga.expeditech.tiles.machines;

import fr.caranouga.expeditech.blocks.EnergyStorages;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

public class CoalGeneratorMachineTile extends AbstractEnergyMachineTile implements ITickableTileEntity {
    private static final int INPUT_SLOT = 0;
    public static final int ENERGY_PER_TICK = 2;

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
    // region Data Saving (World load/save)
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
    // endregion
    // endregion

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(hasFinishedBurning()) {
            if(isItemBurnable(itemHandler.getStackInSlot(INPUT_SLOT)) && !energyStorage.isFull()){
                consumeFuel();
                setState(true);
            }else{
                currentBurnTime = 0;
                burnTime = 0;

                setState(false);
            }
        }else{
            if(!energyStorage.isFull()) {
                energyStorage.addEnergy(ENERGY_PER_TICK);
            }
            currentBurnTime++;
        }

        sendOutEnergy();

        setChanged();
    }

    private void setState(boolean isBurning) {
        // We have already check if level is null
        BlockState state = level.getBlockState(getBlockPos());
        level.setBlock(getBlockPos(), state.setValue(BlockStateProperties.POWERED, isBurning), Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
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

    public int getProgress() {
        return currentBurnTime;
    }

    public int getMaxProgress() {
        return burnTime;
    }

    public void setMaxProgress(int burnTime) {
        this.burnTime = burnTime;
    }

    public void setProgress(int currentBurnTime) {
        this.currentBurnTime = currentBurnTime;
    }


    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbtTag = new CompoundNBT();
        //Write your data into the nbtTag

        nbtTag.putInt("burnTime", burnTime);

        return new SUpdateTileEntityPacket(this.worldPosition, -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        //Handle your Data

        if(tag.contains("burnTime")) {
            burnTime = tag.getInt("burnTime");
        }
    }
}