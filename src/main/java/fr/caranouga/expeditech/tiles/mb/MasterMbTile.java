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

import java.util.Map;

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

    public void tryBuild() {
        Map<BlockPos, ITextComponent> minMismatches = null;

        for(Direction dir : Direction.values()) {
            Map<BlockPos, ITextComponent> mismatches = this.shape.test(dir, level, getBlockPos());
            if(mismatches.isEmpty()) {
                minMismatches = null; // Reset minMismatches if we find a perfect match
                break; // No mismatches, we can build
            } else if (minMismatches == null || mismatches.size() < minMismatches.size()) {
                minMismatches = mismatches; // Found a smaller mismatch set
            }
        }

        if(minMismatches == null) {
            build();
            Expeditech.LOGGER.info("Multiblock structure built successfully at {}", getBlockPos());
        } else {
            Expeditech.LOGGER.warn("Cannot build multiblock structure at {}, shape does not match", getBlockPos());

            for(Map.Entry<BlockPos, ITextComponent> entry : minMismatches.entrySet()) {
                BlockPos pos = entry.getKey();
                ITextComponent message = entry.getValue();

                // Send error packet to client
                Expeditech.NETWORK.send(PacketDistributor.ALL.noArg(), new MultiblockErrorPacket(pos, 0xCCFF0000, message, 5000));

                // Log the mismatch
                Expeditech.LOGGER.warn("Mismatch at {}: {}", pos, message.getString());
            }
        }
    }

    private void build(){
        this.isFormed = true;
    }
}
