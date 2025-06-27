package fr.caranouga.expeditech.common.content.containers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class AbstractMachineContainer<T extends TileEntity> extends Container {
    protected final T tileEntity;
    private final PlayerEntity player;
    private final IItemHandler playerInv;
    private final Block block;

    public AbstractMachineContainer(ContainerType<?> containerType, int pContainerId, PlayerInventory playerInv, PlayerEntity player, Block block, T tileEntity) {
        super(containerType, pContainerId);

        this.tileEntity = tileEntity;
        this.block = block;
        this.player = player;
        this.playerInv = new InvWrapper(playerInv);
        layoutPlayerInv(8, 86);
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return this.tileEntity.getLevel() != null &&
                stillValid(IWorldPosCallable.create(this.tileEntity.getLevel(), this.tileEntity.getBlockPos()),
                        pPlayer, this.block);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + getNumberOfSlots(), false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + getNumberOfSlots()) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(this.player, sourceStack);
        return copyOfSourceStack;
    }

    private void layoutPlayerInv(int leftCol, int topRow) {
        addSlotBox(playerInv, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInv, 0, leftCol, topRow, 9, 18);
    }

    private int addSlotRange(IItemHandler handler, int idx, int x, int y, int amount, int dx){
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, idx, x, y));
            x += dx;
            idx++;
        }
        return idx;
    }

    private int addSlotBox(IItemHandler handler, int idx, int x, int y, int horizontalAmount, int dx, int verticalAmount, int dy){
        for (int i = 0; i < verticalAmount; i++) {
            idx = addSlotRange(handler, idx, x, y, horizontalAmount, dx);
            y += dy;
        }
        return idx;
    }

    protected int getNumberOfSlots(){
        return this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .orElseThrow(() -> new IllegalArgumentException("TileEntity does not have an item handler"))
                .getSlots();
    }
}
