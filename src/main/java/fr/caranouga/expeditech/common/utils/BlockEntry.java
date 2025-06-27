package fr.caranouga.expeditech.common.utils;


public class BlockEntry {
    private final LootTypeEntry lootType;
    private final BlockStateType blockStateType;

    public BlockEntry(LootTypeEntry lootType, BlockStateType blockStateType) {
        this.lootType = lootType;
        this.blockStateType = blockStateType;
    }

    public LootTypeEntry getLootType() {
        return lootType;
    }

    public BlockStateType getBlockStateType() {
        return blockStateType;
    }
}
