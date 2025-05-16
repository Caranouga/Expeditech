package fr.caranouga.expeditech.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMachineTile extends TileEntity {
    protected final ItemStackHandler itemHandler;
    private final LazyOptional<IItemHandler> lazyItemHandler;

    private int maxUses;

    public AbstractMachineTile(TileEntityType<?> tileEntityType, int maxUses, int invSize) {
        super(tileEntityType);

        this.maxUses = maxUses;
        this.itemHandler = createItemHandler(invSize);
        this.lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    protected ItemStackHandler createItemHandler(int size) {
        return new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();

        lazyItemHandler.invalidate();
    }

    // region Data Saving (World load/save)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        // Capability
        if(nbt.contains("inv")) {
            itemHandler.deserializeNBT(nbt.getCompound("inv"));
        }

        // Custom data
        if(nbt.contains("maxUses")) {
            maxUses = nbt.getInt("maxUses");
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        // Capability
        pCompound.put("inv", itemHandler.serializeNBT());

        pCompound.putInt("maxUses", maxUses);

        return super.save(pCompound);
    }
    // endregion

    public void drops(){
        if(this.level == null) return;

        Inventory inventory = new Inventory(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        InventoryHelper.dropContents(this.level, this.worldPosition, inventory);
    }
}
