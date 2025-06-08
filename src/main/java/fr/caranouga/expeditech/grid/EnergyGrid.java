package fr.caranouga.expeditech.grid;
/*
import fr.caranouga.expeditech.tiles.pipes.energy.IronEnergyPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.*;

public class EnergyGrid {
    private final Set<IronEnergyPipeTile> pipes = new HashSet<>();
    private final Set<IEnergyStorage> producers = new HashSet<>();
    private final Set<IEnergyStorage> consumers = new HashSet<>();

    public static EnergyGrid rebuildFrom(IronEnergyPipeTile startPipe){
        EnergyGrid grid = new EnergyGrid();
        grid.gridId = UUID.randomUUID();
        Queue<BlockPos> toProcess = new LinkedList<>();
        Set<BlockPos> processed = new HashSet<>();
        toProcess.add(startPipe.getBlockPos());

        World world = startPipe.getLevel();

        while(!toProcess.isEmpty()){
            BlockPos pos = toProcess.poll();
            if(processed.contains(pos)) continue;
            processed.add(pos);

            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof IronEnergyPipeTile) {
                IronEnergyPipeTile pipe = (IronEnergyPipeTile) tileEntity;
                grid.pipes.add(pipe);
                pipe.grid = grid;

                for (Direction dir : Direction.values()) {
                    BlockPos adjacentPos = pos.relative(dir);
                    TileEntity neighbour = world.getBlockEntity(adjacentPos);
                    if (!processed.contains(adjacentPos)) {
                        toProcess.add(adjacentPos);
                    }

                    if (neighbour != null) {
                        LazyOptional<IEnergyStorage> energyStorage = neighbour.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
                        energyStorage.ifPresent(storage -> {
                            if (storage.canExtract()) grid.producers.add(storage);
                            if (storage.canReceive()) {
                                grid.consumers.add(storage);
                            }
                        });
                    }
                }
            }
        }

        return grid;
    }

    public void tick(){
        int totalEnergyAvailable = producers.stream().mapToInt(p -> p.extractEnergy(Integer.MAX_VALUE, true)).sum();
        if(totalEnergyAvailable <= 0) return;

        int numberOfConsumers = consumers.size();
        if(numberOfConsumers == 0) return;

        int energyPerConsumer = totalEnergyAvailable / numberOfConsumers;

        for(IEnergyStorage consumer : consumers){
            for(IEnergyStorage producer : producers) {
                int extracted = producer.extractEnergy(energyPerConsumer, false);
                int accepted = consumer.receiveEnergy(extracted, false);
                if(accepted < extracted) {
                    // If the consumer didn't accept all the energy, we need to return the excess to the producer
                    producer.receiveEnergy(extracted - accepted, false);
                }
            }
        }
    }

    public void invalidate() {
        for (IronEnergyPipeTile pipe : pipes) {
            pipe.grid = null;
        }
        pipes.clear();
        producers.clear();
        consumers.clear();
    }
}
*/

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyGrid extends AbstractGrid<IEnergyStorage> {
    @Override
    protected AbstractGrid<IEnergyStorage> getGrid() {
        return new EnergyGrid();
    }

    @Override
    protected Capability<IEnergyStorage> getCapability() {
        return CapabilityEnergy.ENERGY;
    }

    @Override
    protected boolean isProducer(IEnergyStorage storage) {
        return storage.canExtract();
    }

    @Override
    protected boolean isConsumer(IEnergyStorage storage) {
        return storage.canReceive();
    }

    @Override
    protected int extractFrom(IEnergyStorage producer, int amount, boolean simulate) {
        return producer.extractEnergy(amount, simulate);
    }

    @Override
    protected int receiveTo(IEnergyStorage consumer, int amount) {
        return consumer.receiveEnergy(amount, false);
    }
}