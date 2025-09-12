package fr.caranouga.expeditech.common.content.tiles.mb;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.multiblock.MultiblockShape;
import fr.caranouga.expeditech.common.packets.MultiblockErrorPacket;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.caranouga.expeditech.common.multiblock.MultiblockShape.DIRECTIONS;

public abstract class AbstractMultiblockTile extends TileEntity implements ITickableTileEntity {
    private boolean isFormed = false;
    private final MultiblockShape shape;
    private final Map<BlockPos, BlockState> savedBlocks = new HashMap<>();

    public AbstractMultiblockTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);

        this.shape = getShape();
    }

    @Nonnull
    protected abstract MultiblockShape getShape();
    protected abstract void formedTick();
    protected abstract void unformedTick();

    @Override
    public void tick() {
        if(level == null || level.isClientSide) return;

        if(!isFormed){
            unformedTick();
        }else{
            formedTick();
        }

        setChanged();
    }

    public boolean tryBuild(Direction firstDirection) {
        if(isFormed) return false;

        Map<Direction, Map<BlockPos, ITextComponent>> mismatchesMap = new HashMap<>();
        Direction goodDirection = null;

        for(Direction dir : DIRECTIONS) {
            Map<BlockPos, ITextComponent> mismatches = this.shape.test(dir, level, getBlockPos());
            if(mismatches.isEmpty()) {
                goodDirection = dir;
                break; // No mismatches, we can build
            } else {
                mismatchesMap.put(dir, mismatches);
            }
        }

        if(goodDirection != null) {
            this.shape.saveAndPrepareMultiblock(goodDirection, level, getBlockPos(), savedBlocks);
            build();

            return true;
        } else {
            Map<BlockPos, ITextComponent> goodMasterMismatch = getGoodMasterMismatch(mismatchesMap, firstDirection);
            Map<BlockPos, ITextComponent> mismatches = null;
            if(goodMasterMismatch != null){
                mismatches = goodMasterMismatch;
            }else{
                Map<BlockPos, ITextComponent> minMismatch = getMinMismatch(mismatchesMap);
                if(minMismatch == null) {
                    Expeditech.LOGGER.warn("No mismatches found, but multiblock structure cannot be built at {}", getBlockPos());
                    return false;
                }
                mismatches = minMismatch;
            }

            for(Map.Entry<BlockPos, ITextComponent> entry : mismatches.entrySet()) {
                BlockPos pos = entry.getKey();
                ITextComponent message = entry.getValue();
                // Send packet to client to display the error
                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));
            }
        }

        return false;
    }

    private Map<BlockPos, ITextComponent> getMinMismatch(Map<Direction, Map<BlockPos, ITextComponent>> mismatchesMap) {
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

        List<Map<BlockPos, ITextComponent>> minMismatch = getMinMismatch(mismatchesList);
        for(Map<BlockPos, ITextComponent> entry : minMismatch) {
            if(goodDirections.get(mismatchesList.indexOf(entry)) == firstDirection) {
                return entry;
            }
        }

        return null;
    }

    private void build(){
        this.isFormed = true;

        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    public void slaveBroken(SlaveMbTile slaveTile) {
        // Handle the case when a slave tile is broken
        if(!unform()) return;

        BlockPos slavePos = slaveTile.getBlockPos();
        World world = slaveTile.getLevel();

        world.destroyBlock(slavePos, true);
    }

    public void registerNewSlave(SlaveMbTile slaveTile) {
        savedBlocks.put(slaveTile.getBlockPos(), slaveTile.getOriginalBlockState());
    }

    private boolean unform() {
        if(!isFormed) return false;

        // Logic to unform the multiblock structure
        this.isFormed = false;
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
        replaceOriginalBlocks();

        // Clear saved blocks after unforming
        savedBlocks.clear();

        return true;
    }

    private void replaceOriginalBlocks(){
        this.savedBlocks.forEach((pos, block) -> {
            if(level.getBlockState(pos).getBlock() != block.getBlock()) {
                level.setBlockAndUpdate(pos, block);
            }
        });
    }

    @Override
    public void setRemoved() {
        unform();
        super.setRemoved();
    }

    // region Data Saving (World load/save)
    /*@Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbtTag = new CompoundNBT();
        //Write your data into the nbtTag

        nbtTag.putBoolean("isFormed", isFormed);

        return new SUpdateTileEntityPacket(this.worldPosition, -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        //Handle your Data

        if(tag.contains("isFormed")) {
            isFormed = tag.getBoolean("isFormed");
        }
    }*/

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        if(nbt.contains("isFormed")){
            isFormed = nbt.getBoolean("isFormed");
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        CompoundNBT tag = super.save(pCompound);

        tag.putBoolean("isFormed", isFormed);

        return tag;
    }
    // endregion
}
