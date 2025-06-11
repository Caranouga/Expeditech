package fr.caranouga.expeditech.tiles.machines;

import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEnergyMachineTile extends AbstractMachineTile {
    protected final CustomEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<CustomEnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> energyStorage);

    public AbstractEnergyMachineTile(TileEntityType<?> tileEntityType, int invSize) {
        super(tileEntityType, invSize);
    }

    protected abstract CustomEnergyStorage createEnergyStorage();

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

    protected void sendOutEnergy(){
        AtomicBoolean energySent = new AtomicBoolean(false);

        for(Direction direction : Direction.values()) {
            TileEntity neighbor = level.getBlockEntity(worldPosition.relative(direction));
            if(neighbor == null || neighbor == this) continue;

            neighbor.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(handler -> {
                if (handler.canReceive()) {
                    int extracted = energyStorage.extractEnergy(this.energyStorage.getMaxExtract(), true); // Simule
                    int received = handler.receiveEnergy(extracted, false); // Reçoit
                    if(received > 0) {
                        energyStorage.extractEnergy(received, false); // Consomme
                        energySent.set(true);
                    }
                }
            });
        }

        if (energySent.get()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }
}
