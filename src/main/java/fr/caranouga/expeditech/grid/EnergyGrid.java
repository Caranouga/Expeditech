package fr.caranouga.expeditech.grid;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.tiles.pipes.AbstractEnergyPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class EnergyGrid {
    private final Set<AbstractEnergyPipeTile> pipes = new HashSet<>();
    private final Set<IEnergyStorage> producers = new HashSet<>();
    private final Set<IEnergyStorage> consumers = new HashSet<>();

    public static EnergyGrid rebuildFrom(AbstractEnergyPipeTile startPipe){
        EnergyGrid grid = new EnergyGrid();
        Queue<BlockPos> toProcess = new LinkedList<>();
        Set<BlockPos> processed = new HashSet<>();
        toProcess.add(startPipe.getBlockPos());

        World world = startPipe.getLevel();

        while(!toProcess.isEmpty()){
            BlockPos pos = toProcess.poll();
            if(processed.contains(pos)) continue;
            processed.add(pos);

            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof AbstractEnergyPipeTile) {
                AbstractEnergyPipeTile pipe = (AbstractEnergyPipeTile) tileEntity;
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
                            if (storage.canReceive()) grid.consumers.add(storage);
                        });
                    }
                }
            }
        }

        return grid;
    }

    public void tick(){
        int totalEnergyAvailable = producers.stream().mapToInt(p -> p.extractEnergy(Integer.MAX_VALUE, true)).sum();
        Expeditech.LOGGER.debug("Total energy available in grid: {}", totalEnergyAvailable);
        if(totalEnergyAvailable <= 0) return;

        int numberOfConsumers = consumers.size();
        Expeditech.LOGGER.debug("Number of consumers in grid: {}", numberOfConsumers);
        if(numberOfConsumers == 0) return;

        int energyPerConsumer = totalEnergyAvailable / numberOfConsumers;
        Expeditech.LOGGER.debug("Energy per consumer: {}", energyPerConsumer);

        for(IEnergyStorage consumer : consumers){
            for(IEnergyStorage producer : producers) {
                Expeditech.LOGGER.debug("Transferring energy from producer {} to consumer {}", producer, consumer);
                int extracted = producer.extractEnergy(energyPerConsumer, false);
                int accepted = consumer.receiveEnergy(extracted, false);
                if(accepted < extracted) {
                    Expeditech.LOGGER.debug("Consumer {} did not accept all energy, accepted: {}, extracted: {}", consumer, accepted, extracted);
                    // If the consumer didn't accept all the energy, we need to return the excess to the producer
                    producer.receiveEnergy(extracted - accepted, false);
                }
            }
        }
    }

    public void invalidate() {
        for (AbstractEnergyPipeTile pipe : pipes) {
            pipe.grid = null;
        }
        pipes.clear();
        producers.clear();
        consumers.clear();
    }
}
