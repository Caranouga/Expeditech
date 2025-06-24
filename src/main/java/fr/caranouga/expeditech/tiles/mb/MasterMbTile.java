package fr.caranouga.expeditech.tiles.mb;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.multiblock.MultiblockShape;
import fr.caranouga.expeditech.packets.MultiblockErrorPacket;
import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.caranouga.expeditech.multiblock.MultiblockShape.DIRECTIONS;

public class MasterMbTile extends TileEntity implements ITickableTileEntity {
    private boolean isFormed = false;
    private final MultiblockShape shape;

    public MasterMbTile() {
        super(ModTileEntities.MB_MASTER.get());

        BlockState stone = Blocks.STONE.defaultBlockState();
        BlockState thisState = ModBlocks.MB_MASTER.get().defaultBlockState();
        this.shape = new MultiblockShape(new BlockPos(-1, -1, 0),
                new BlockState[][] {
                    {stone, stone, stone},
                    {stone, stone, stone},
                    {stone, stone, stone}
                },
                new BlockState[][] {
                    {stone, thisState, stone},
                    {stone, stone, stone},
                    {stone, stone, stone}
                },
                new BlockState[][] {
                    {stone, stone, stone},
                    {stone, stone, stone},
                    {stone, stone, stone}
                });
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) return;

        if(!isFormed) Expeditech.LOGGER.debug("MasterMbTile is not formed yet at {}", getBlockPos());
        else Expeditech.LOGGER.debug("MasterMbTile is formed at {}", getBlockPos());
    }

    public void tryBuild(Direction firstDirection) {
        Map<Direction, Map<BlockPos, ITextComponent>> mismatchesMap = new HashMap<>();

        for(Direction dir : DIRECTIONS) {
            Map<BlockPos, ITextComponent> mismatches = this.shape.test(dir, level, getBlockPos());
            if(mismatches.isEmpty()) {
                mismatchesMap.clear();
                break; // No mismatches, we can build
            } else {
                mismatchesMap.put(dir, mismatches);
            }
        }

        if(mismatchesMap.isEmpty()) {
            build();
            Expeditech.LOGGER.info("Multiblock structure built successfully at {}", getBlockPos());
        } else {
            Expeditech.LOGGER.warn("Cannot build multiblock structure at {}, shape does not match", getBlockPos());

            Map<BlockPos, ITextComponent> goodMasterMismatch = getGoodMasterMismatch(mismatchesMap, firstDirection);
            Map<BlockPos, ITextComponent> mismatches = null;
            if(goodMasterMismatch != null){
                mismatches = goodMasterMismatch;
            }else{
                Map<BlockPos, ITextComponent> minMismatch = getMinMismatch(mismatchesMap);
                if(minMismatch == null) {
                    Expeditech.LOGGER.warn("No mismatches found, but multiblock structure cannot be built at {}", getBlockPos());
                    return;
                }
                mismatches = minMismatch;
            }

            for(Map.Entry<BlockPos, ITextComponent> entry : mismatches.entrySet()) {
                BlockPos pos = entry.getKey();
                ITextComponent message = entry.getValue();
                Expeditech.LOGGER.warn("Mismatch at {}: {}", pos, message.getString());

                // Send packet to client to display the error
                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));
            }
        }
    }

    private Map<BlockPos, ITextComponent> getMinMismatch(Map<Direction, Map<BlockPos, ITextComponent>> mismatchesMap) {
        /*// Find the minimum number of mismatches
        int minCount = Integer.MAX_VALUE;
        for(Map<BlockPos, ITextComponent> mismatches : mismatchesMap.values()) {
            if(mismatches.size() < minCount) {
                minCount = mismatches.size();
            }
        }

        // Collect all mismatches with the minimum count
        for(Map.Entry<Direction, Map<BlockPos, ITextComponent>> entry : mismatchesMap.entrySet()) {
            Map<BlockPos, ITextComponent> mismatches = entry.getValue();
            if(mismatches.size() == minCount) return mismatches;
        }

        return null;*/
        List<Map<BlockPos, ITextComponent>> mismatchesList = new ArrayList<>(mismatchesMap.values());
        return getMinMismatch(mismatchesList).get(0);
    }

    private List<Map<BlockPos, ITextComponent>> getMinMismatch(List<Map<BlockPos, ITextComponent>> mismatchesList) {
        // Find the minimum number of mismatches
        List<Map<BlockPos, ITextComponent>> minMismatches = new ArrayList<>();

        int minCount = Integer.MAX_VALUE;
        for(Map<BlockPos, ITextComponent> mismatches : mismatchesList) {
            if(mismatches.size() < minCount) {
                minCount = mismatches.size();
            }
        }

        // Collect all mismatches with the minimum count
        for(Map<BlockPos, ITextComponent> entry : mismatchesList) {
            //if(entry.size() == minCount) return entry;
            if(entry.size() == minCount) {
                minMismatches.add(entry);
            }
        }

        return minMismatches;
    }

    private Map<BlockPos, ITextComponent> getGoodMasterMismatch(Map<Direction, Map<BlockPos, ITextComponent>> mismatchesMap, Direction firstDirection) {
        ArrayList<Map<BlockPos, ITextComponent>> mismatchesList = new ArrayList<>();
        ArrayList<Direction> goodDirections = new ArrayList<>();

        for(Map<BlockPos, ITextComponent> mismatches : mismatchesMap.values()) {
            if(this.shape.isMasterAtGoodPos(mismatches.keySet().iterator().next())) {
                mismatchesList.add(mismatches);
                goodDirections.add(mismatchesMap.entrySet().stream()
                        .filter(entry -> entry.getValue() == mismatches)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null));
            }
        }

        mismatchesList.forEach(m -> {
            Expeditech.LOGGER.debug("Good master mismatch found with {} mismatches: ", m.size());
            m.forEach((pos, message) -> Expeditech.LOGGER.debug("Mismatch at {}: {} (dir: {})", pos, message.getString(), goodDirections.get(mismatchesList.indexOf(m))));
        });

        List<Map<BlockPos, ITextComponent>> minMismatch = getMinMismatch(mismatchesList);
        for(Map<BlockPos, ITextComponent> entry : minMismatch) {
            if(goodDirections.get(mismatchesList.indexOf(entry)) == firstDirection) {
                return entry;
            }
        }

        return null;
        //return getMinMismatch(mismatchesList);
    }

    private void build(){
        this.isFormed = true;
    }
}
