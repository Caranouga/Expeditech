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
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Exp;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class EnergyGrid {
    private Set<AbstractEnergyPipeTile> pipes = new HashSet<>();
    private Set<IEnergyStorage> producers = new HashSet<>();
    private Set<IEnergyStorage> consumers = new HashSet<>();

    public static EnergyGrid rebuildFrom(AbstractEnergyPipeTile startPipe) {
        EnergyGrid network = new EnergyGrid();
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        World world = startPipe.getLevel();
        queue.add(startPipe.getBlockPos());

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (!visited.add(current)) continue;

            TileEntity te = world.getBlockEntity(current);
            if (te instanceof AbstractEnergyPipeTile) {
                AbstractEnergyPipeTile pipe = (AbstractEnergyPipeTile) te;
                pipe.grid = network;
                network.pipes.add(pipe);

                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = current.relative(dir);
                    TileEntity neighbor = world.getBlockEntity(neighborPos);
                    if (neighbor != null) {
                        LazyOptional<IEnergyStorage> cap = neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
                        cap.ifPresent(storage -> {
                            if (storage.canExtract()) network.producers.add(storage);
                            if (storage.canReceive()) network.consumers.add(storage);
                            if (!(neighbor instanceof AbstractEnergyPipeTile)) {
                                for (Direction adj : Direction.values()) {
                                    TileEntity adjTe = world.getBlockEntity(neighborPos.relative(adj));
                                    if (adjTe instanceof AbstractEnergyPipeTile) {
                                        ((AbstractEnergyPipeTile) adjTe).markGridForRebuild();
                                    }
                                }
                            }
                        });

                        if (!visited.contains(neighborPos)) {
                            if (neighbor instanceof AbstractEnergyPipeTile) {
                                queue.add(neighborPos);
                            }
                        }
                    }
                }
            }
        }

        return network;
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
            pipe.markGridForRebuild();
        }
        pipes.clear();
        producers.clear();
        consumers.clear();
    }
}
