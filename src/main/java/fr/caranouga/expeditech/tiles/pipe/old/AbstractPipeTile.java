package fr.caranouga.expeditech.tiles.pipe.old;

import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractPipeTile extends TileEntity implements ITickableTileEntity {
    protected final CustomEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<CustomEnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> energyStorage);

    public AbstractPipeTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    // region Data Saving (World load/save)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        // Capability
        if(nbt.contains("energy")) {
            energyStorage.deserializeNBT(nbt.getCompound("energy"));
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        // Capability
        pCompound.put("energy", energyStorage.serializeNBT());

        return super.save(pCompound);
    }
    // endregion

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY){
            return lazyEnergyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();

        lazyEnergyStorage.invalidate();
    }

    protected abstract CustomEnergyStorage createEnergyStorage();
}
