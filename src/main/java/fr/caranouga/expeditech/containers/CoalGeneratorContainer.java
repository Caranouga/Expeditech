package fr.caranouga.expeditech.containers;

import fr.caranouga.expeditech.capability.energy.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModContainers;
import fr.caranouga.expeditech.tiles.machines.CoalGeneratorMachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CoalGeneratorContainer extends AbstractMachineContainer<CoalGeneratorMachineTile> {
    public CoalGeneratorContainer(int pContainerId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(ModContainers.COAL_GENERATOR_CONTAINER.get(), pContainerId, playerInv, player, ModBlocks.COAL_GENERATOR.get(), (CoalGeneratorMachineTile) world.getBlockEntity(pos));

        if(this.tileEntity != null){
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new SlotItemHandler(handler, 0, 80, 35));
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
                return tileEntity.getProgress();
            }

            @Override
            public void set(int value) {
                tileEntity.setProgress(value);
            }
        });

        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getMaxProgress();
            }

            @Override
            public void set(int value) {
                tileEntity.setMaxProgress(value);
            }
        });
    }

    public int getProgress() {
        return tileEntity.getProgress();
    }

    public int getMaxProgress() {
        return tileEntity.getMaxProgress();
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