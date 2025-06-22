package fr.caranouga.expeditech.blocks.pipes.energy;

import fr.caranouga.expeditech.blocks.pipes.AbstractPipeBlock;
import fr.caranouga.expeditech.tiles.pipes.energy.AbstractEnergyPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEnergyPipeBlock extends AbstractPipeBlock {
    @Override
    protected boolean canConnect(TileEntity tileEntity, IWorld world, BlockPos pos, Direction direction) {
        if(world == null || tileEntity == null) {
            return false;
        }

        if (tileEntity instanceof AbstractEnergyPipeTile) {
            return true;
        }

        AtomicBoolean shouldBeConnected = new AtomicBoolean(false);
        tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(energyStorage -> {
            shouldBeConnected.set(energyStorage.canExtract() || energyStorage.canReceive());
        });

        return shouldBeConnected.get();
    }
}