package fr.caranouga.expeditech.tiles.pipe;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractPipeTile<C, S extends C> extends TileEntity implements ITickableTileEntity {
    protected final S capabilityStorage = createCapabilityStorage();
    private final LazyOptional<S> lazyCapabilityStorage = LazyOptional.of(() -> capabilityStorage);
    private final String capabilityKey;

    public AbstractPipeTile(TileEntityType<?> tileEntityType, String capabilityKey) {
        super(tileEntityType);
        this.capabilityKey = capabilityKey;

        // Check if the capability storage implements INBTSerializable<CompoundNBT>
        if (!(capabilityStorage instanceof INBTSerializable)) {
            throw new IllegalArgumentException("Capability storage must implement INBTSerializable<CompoundNBT>");
        }
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) return;

        for (Direction direction : Direction.values()) {
            TileEntity neighborTile = this.level.getBlockEntity(this.worldPosition.relative(direction));
            if (neighborTile == null) {
                continue;
            }

            LazyOptional<C> neighborCap = neighborTile.getCapability(getCapability(), direction.getOpposite());
            if (!neighborCap.isPresent()) {
                continue;
            }

            /*IEnergyStorage neighborStorage = neighborCap.orElse(null);
            IEnergyStorage pipeStorage = this.energyStorage;*/
            C neighborStorage = neighborCap.orElse(null);
            C pipeStorage = this.capabilityStorage;

            if (neighborStorage == null) {
                continue;
            }

            // Pull from neighbor to pipe
            neighborToPipeTransfer(neighborStorage, pipeStorage);
            /*if (neighborStorage.canExtract() && pipeStorage.canReceive()) {
                int extractable = neighborStorage.extractEnergy(getMaxExtract(neighborStorage), true);
                int received = pipeStorage.receiveEnergy(extractable, true);

                if (received > 0) {
                    neighborStorage.extractEnergy(received, false);
                    pipeStorage.receiveEnergy(received, false);
                }
            }*/

            // Push from pipe to neighbor
            pipeToNeighborTransfer(pipeStorage, neighborStorage);
            /*if (pipeStorage.canExtract() && neighborStorage.canReceive()) {
                int extractable = pipeStorage.extractEnergy(getMaxExtract(pipeStorage), true);
                int receivable = neighborStorage.receiveEnergy(extractable, true);

                if (receivable > 0) {
                    pipeStorage.extractEnergy(receivable, false);
                    neighborStorage.receiveEnergy(receivable, false);
                }
            }*/
        }

        setChanged();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == getCapability()) {
            return lazyCapabilityStorage.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        lazyCapabilityStorage.invalidate();
    }

    // region Data Saving
    @Override
    public void load(BlockState state, CompoundNBT tag) {
        if(tag.contains(this.capabilityKey)) {
            ((INBTSerializable<CompoundNBT>) this.capabilityStorage).deserializeNBT(tag.getCompound(this.capabilityKey));
        }

        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        pCompound.put(this.capabilityKey, ((INBTSerializable<CompoundNBT>) this.capabilityStorage).serializeNBT());

        return super.save(pCompound);
    }
    // endregion

    protected abstract Capability<C> getCapability();
    protected abstract S createCapabilityStorage();
    protected abstract void neighborToPipeTransfer(C neighborStorage, C pipeStorage);
    protected abstract void pipeToNeighborTransfer(C pipeStorage, C neighborStorage);
}
