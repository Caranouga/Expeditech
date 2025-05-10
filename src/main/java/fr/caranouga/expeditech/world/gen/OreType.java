package fr.caranouga.expeditech.world.gen;

import fr.caranouga.expeditech.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public enum OreType {

    CARANITE(Lazy.of(ModBlocks.CARANITE_ORE), 8, 0, 32);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;

    OreType(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
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
