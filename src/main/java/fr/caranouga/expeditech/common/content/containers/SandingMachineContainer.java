package fr.caranouga.expeditech.common.content.containers;

import fr.caranouga.expeditech.common.capability.energy.CustomEnergyStorage;
import fr.caranouga.expeditech.common.content.containers.slots.SlotOutput;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModContainers;
import fr.caranouga.expeditech.common.content.tiles.machines.SandingMachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SandingMachineContainer extends AbstractMachineContainer<SandingMachineTile> {
    public SandingMachineContainer(int pContainerId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(ModContainers.SANDING_MACHINE_CONTAINER.get(), pContainerId, playerInv, player, ModBlocks.SANDING_MACHINE.get(), (SandingMachineTile) world.getBlockEntity(pos));

        if(this.tileEntity != null){
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new SlotItemHandler(handler, 0, 59, 35));
                addSlot(new SlotOutput(handler, 1, 101, 35));
            });
        }

        trackPower();
        trackProgress();
    }

    private void trackPower(){
        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return getEnergyStored() & 0xFFFF;
            }

            @Override
            public void set(int pValue) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                    int energyStored = handler.getEnergyStored() & 0xffff0000;
                    ((CustomEnergyStorage) handler).setEnergy(energyStored + (pValue & 0xFFFF));
                });
            }
        });

        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return (getEnergyStored() >> 16) & 0xFFFF;
            }

            @Override
            public void set(int pValue) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                    int energyStored = handler.getEnergyStored() & 0x0000FFFF;
                    ((CustomEnergyStorage) handler).setEnergy(energyStored | (pValue << 16));
                });
            }
        });
    }

    private void trackProgress() {
        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getCurrentPolishingTime();
            }

            @Override
            public void set(int value) {
                tileEntity.setCurrentPolishingTime(value);
            }
        });

        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getPolishingTime();
            }

            @Override
            public void set(int value) {
                tileEntity.setPolishingTime(value);
            }
        });
    }

    public int getProgress() {
        return tileEntity.getCurrentPolishingTime();
    }

    public int getMaxProgress() {
        return tileEntity.getPolishingTime();
    }

    public int getEnergyStored() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getMaxEnergyStored() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }

    public float getScaledProgress() {
        int progress = getProgress();
        int maxProgress = getMaxProgress();

        return maxProgress == 0 ? 0 : (float) progress / (float) maxProgress;
    }

    public float getScaledEnergy() {
        int energy = getEnergyStored();
        int maxEnergy = getMaxEnergyStored();

        return maxEnergy == 0 ? 0 : (float) energy / (float) maxEnergy;
    }
}