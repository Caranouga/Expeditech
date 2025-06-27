package fr.caranouga.expeditech.common.world.gen;

import fr.caranouga.expeditech.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public enum OreType {

    CARANITE(Lazy.of(ModBlocks.CARANITE_ORE), 8, 0, 32, 4);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;
    private final int veinsPerChunk;

    OreType(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight, int veinsPerChunk) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.veinsPerChunk = veinsPerChunk;
    }

    public Lazy<Block> getBlock() {
        return block;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getVeinsPerChunk() {
        return veinsPerChunk;
    }

    @Nullable
    public static OreType get(Block block){
        for(OreType oreType : values()){
            if(block == oreType.getBlock()){
                return oreType;
            }
        }

        return null;
    }
}
