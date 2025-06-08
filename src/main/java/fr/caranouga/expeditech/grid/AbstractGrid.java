package fr.caranouga.expeditech.grid;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.tiles.pipes.AbstractPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;

public abstract class AbstractGrid<C> {
    private final Set<AbstractPipeTile<C>> pipes = new HashSet<>();
    private final Set<C> producers = new HashSet<>();
    private final Set<C> consumers = new HashSet<>();

    public AbstractGrid<C> rebuildFrom(AbstractPipeTile<C> startPipe){
        AbstractGrid<C> grid = getGrid();

        Queue<BlockPos> toProcess = new LinkedList<>();
        Set<BlockPos> processed = new HashSet<>();
        toProcess.add(startPipe.getBlockPos());

        World world = startPipe.getLevel();
        if(world == null) {
            Expeditech.LOGGER.error("World is null while rebuilding grid from pipe at {}", startPipe.getBlockPos());
            return grid;
        }

        while(!toProcess.isEmpty()){
            BlockPos pos = toProcess.poll();
            if(processed.contains(pos)) continue;
            processed.add(pos);

            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof AbstractPipeTile) {
                AbstractPipeTile<C> pipe = (AbstractPipeTile<C>) tileEntity;

                grid.pipes.add(pipe);
                pipe.setGrid(grid);

                for (Direction dir : Direction.values()) {
                    BlockPos adjacentPos = pos.relative(dir);
                    TileEntity neighbour = world.getBlockEntity(adjacentPos);
                    if (!processed.contains(adjacentPos)) {
                        toProcess.add(adjacentPos);
                    }

                    if (neighbour != null) {
                        LazyOptional<C> capability = neighbour.getCapability(getCapability(), dir.getOpposite());
                        capability.ifPresent(storage -> {
                            if (isProducer(storage)) grid.producers.add(storage);
                            if (isConsumer(storage)) grid.consumers.add(storage);
                        });
                    }
                }
            }
        }

        return grid;
    }

    public void tick(){
        int totalAvailable = producers.stream().mapToInt(pipe -> extractFrom(pipe, Integer.MAX_VALUE, true)).sum();
        if(totalAvailable <= 0) return;

        int numberOfConsumers = consumers.size();
        if(numberOfConsumers == 0) return;

        int totalPerConsumer = totalAvailable / numberOfConsumers;

        for(C consumer : consumers){
            for(C producer : producers) {
                int extracted = extractFrom(producer, totalPerConsumer, false);
                int accepted = receiveTo(consumer, extracted);
                if(accepted < extracted) {
                    // If the consumer didn't accept all, we need to return the excess to the producer
                    receiveTo(producer, extracted - accepted);
                }
            }
        }
    }

    public void invalidate() {
        for (AbstractPipeTile<C> pipe : pipes) {
            pipe.setGrid(null);
        }
        pipes.clear();
        producers.clear();
        consumers.clear();
    }

    protected abstract AbstractGrid<C> getGrid();
    protected abstract Capability<C> getCapability();
    protected abstract boolean isProducer(C storage);
    protected abstract boolean isConsumer(C storage);
    protected abstract int extractFrom(C producer, int amount, boolean simulate);
    protected abstract int receiveTo(C consumer, int amount);
}
