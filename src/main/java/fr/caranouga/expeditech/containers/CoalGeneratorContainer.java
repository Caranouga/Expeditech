package fr.caranouga.expeditech.containers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CoalGeneratorContainer extends AbstractMachineContainer {
    public CoalGeneratorContainer(int pContainerId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(ModContainers.COAL_GENERATOR_CONTAINER.get(), pContainerId, world, pos, playerInv, player, ModBlocks.COAL_GENERATOR.get());

        if(this.tileEntity != null){
            this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new SlotItemHandler(handler, 0, 80, 35));
            });
        }

        Expeditech.LOGGER.debug("Number of slots: (slots): {}", this.slots.size());
        Expeditech.LOGGER.debug("Number of slots: (tileEntity): {}", this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null).getSlots());
    }
}